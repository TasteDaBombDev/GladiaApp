package com.zone.app.userScreen.Evenimente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.drm.DrmUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wefika.flowlayout.FlowLayout;
import com.zone.app.R;
import com.zone.app.utils.AVLtree;
import com.zone.app.utils.EventsDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Evenimente extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static Evenimente INSTANCE = null;
    private FlowLayout muzicaRoot;
    private ArrayList<String> muzica = new ArrayList<>();
    private String date = "", oraS = "", oraE = "";
    private boolean filtersChanged = false;

    private static BiFunction<EventsDetails, EventsDetails, Integer> sortID = (a, b) -> {
        if(a.getID() < b.getID())
            return -1;
        else return 1;
    };
    private static AVLtree<EventsDetails> database = new AVLtree<>(sortID);

    private ConstraintLayout header, root;
    private ConstraintLayout filters;
    private ImageButton filterBtn;
    private boolean filterOpen = false;
    private SeekBar distance;
    private TextView filtersText, distanceAfis, dataAfis, oraStart, oraEnd;
    private static RecyclerView recyclerView;

    private View view;

    public Evenimente(){
    }

    public static Evenimente getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new Evenimente();
        return INSTANCE;
    }

    public static void resetINSTANCE(){
        INSTANCE = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events,container,false);
        init();

        boolean[] toggles = new boolean[muzicaRoot.getChildCount()];
        Arrays.fill(toggles, false);

        for (int i = 0; i < muzicaRoot.getChildCount(); i++) {
            Button b = (Button)muzicaRoot.getChildAt(i);
            int finalI = i;
            b.setOnClickListener(view -> {
                filtersChanged = true;
                if(!toggles[finalI]) {
                    muzica.add(b.getText().toString());
                    toggles[finalI] = true;
                    b.setBackgroundColor(Color.rgb(0,0,0));
                    b.setTextColor(Color.rgb(255,255,255));
                } else {
                    muzica.remove(b.getText().toString());
                    toggles[finalI] = false;
                    b.setBackgroundColor(Color.rgb(255,255,255));
                    b.setTextColor(Color.rgb(0,98,100));
                }
            });
        }

        TransitionManager.beginDelayedTransition(header);

        filterBtn.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(root);
            ConstraintSet set = new ConstraintSet();
            set.clone(root);
            if(!filterOpen){

                filtersText.setText("Filtre");

                set.constrainPercentHeight(R.id.header, 1.0f);

                set.applyTo(root);

                filterOpen = true;

                filters.setVisibility(View.VISIBLE);

            } else {

                filtersText.setText("Evenimente din jurul meu");

                set.constrainPercentHeight(R.id.header, 0.18f);

                set.applyTo(root);

                filterOpen = false;

                filters.setVisibility(View.GONE);

                if(filtersChanged)
                    setFilters();
                filtersChanged = false;
            }
        });

        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                filtersChanged = true;
                distanceAfis.setText("Departarea fata de mine: " + i + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dataAfis.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, day1) -> {
                String mnt = "" + (month1 + 1), dy = "" + day1;
                if((month1 + 1) < 10)
                    mnt = "0" + (month1 + 1);
                if(day1 < 10)
                    dy = "0" + day1;
                date = dy + "/" + mnt + "/" + year1;
                dataAfis.setText("Data evenimentului: " + date);
            },year,month,day);
            datePickerDialog.show();
            filtersChanged = true;
        });

        oraStart.setOnClickListener(view -> {
            filtersChanged = true;

            Calendar calendar = Calendar.getInstance();
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view12, hourOfDay, minute1) -> {
                String min = String.valueOf(minute1);
                String h = String.valueOf(hourOfDay);
                if(hourOfDay < 10)
                    h = "0" + h;
                if(minute1 < 10)
                    min = "0" + min;
                String time = h + ":" + min;
                oraStart.setText("Ora inceperii: " + time);
                oraS = time;
                oraE = time;
                oraEnd.setText("Ora inceperii: " + time);
            },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });

        oraEnd.setOnClickListener(view -> {
            filtersChanged = true;

            Calendar calendar = Calendar.getInstance();
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view12, hourOfDay, minute1) -> {
                String min = String.valueOf(minute1);
                String h = String.valueOf(hourOfDay);
                if(hourOfDay < 10)
                    h = "0" + h;
                if(minute1 < 10)
                    min = "0" + min;
                String time = h + ":" + min;
                oraEnd.setText("Ora inceperii: " + time);
                oraE = time;
            },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });

        return view;
    }

    public static void setEventsNearMe(final Context c, final double lat, final double lng){
        String urlUpload = "http://gladiaholdings.com/PHP/utilizatori/findEventsNearMe.php";

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, urlUpload, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                for (int i = 0; i < (jsonObject.length() - 1) / 8; i++) {
                    database.addVal(new EventsDetails(i,
                            jsonObject.getDouble("distance" + i),
                            jsonObject.getInt("id" + i),
                            jsonObject.getString("poza" + i),
                            jsonObject.getString("nume" + i),
                            jsonObject.getString("date" + i),
                            jsonObject.getString("hours" + i),
                            jsonObject.getString("type" + i),
                            jsonObject.getString("muzica" + i)));
                }
                StaggeredGridLayoutManager managerListing = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(managerListing);
                CustomAdapter customAdapter = new CustomAdapter(c, database);
                recyclerView.setAdapter(customAdapter);
            } catch (JSONException e) {
                Toast.makeText(c, "Error loading your event" + response, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }, error -> Toast.makeText(c, "Check your internet connection and try again.", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));
                Log.e("params", lat + " " + lng);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(stringRequest);
    }

    public static void constructNotExistentEvents(){

    }

    private void init(){
        header = view.findViewById(R.id.header);
        filterBtn = view.findViewById(R.id.filterBtn);
        filters  = view.findViewById(R.id.filters);
        root = view.findViewById(R.id.root);
        filtersText = view.findViewById(R.id.filtersTextSwitch);
        distance = view.findViewById(R.id.distance);
        distanceAfis = view.findViewById(R.id.distanceAfis);
        recyclerView = view.findViewById(R.id.eventsListing);
        dataAfis = view.findViewById(R.id.dataAfis);
        muzicaRoot = view.findViewById(R.id.musicRoot);
        oraEnd = view.findViewById(R.id.oraEnd);
        oraStart = view.findViewById(R.id.oraStart);
    }

    private void setFilters(){
        int myDistance = distance.getProgress();
        ArrayList<EventsDetails> unsorted = database.toArrayList();
        BiFunction<EventsDetails, EventsDetails, Integer> sortDistanceID = (a, b) -> {
            if(a.getDistance() < b.getDistance())
                return -1;
                else if(a.getDistance() > b.getDistance())
                    return 1;
                else if(a.getID() < b.getID())
                    return -1;
                    else return 1;
        };
        AVLtree<EventsDetails> sorted = new AVLtree<>(sortDistanceID);
        for (int i = 0; i < unsorted.size(); i++) {
            if(unsorted.get(i).getDistance() <= myDistance
                    && isContaining(unsorted.get(i).getMuzica())
                    && isDate(unsorted.get(i).getDate())
                    && isHour(unsorted.get(i).getOra())){
                sorted.addVal(unsorted.get(i));
            }
        }
        StaggeredGridLayoutManager managerListing = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(managerListing);
        CustomAdapter c = new CustomAdapter(getContext(), sorted);
        recyclerView.setAdapter(c);
    }

    private boolean isContaining(String muzicap){
        if(muzica.size() == 0)
            return true;
        for (int i = 0; i < muzica.size(); i++) {
            if(muzicap.contains(muzica.get(i)))
                return true;
        }

        return false;
    }

    private boolean isDate(String eventDate){
        if(date.equals(""))
            return true;
            else return date.equals(eventDate);
    }

    private boolean isHour(String eventHours){
        if(oraS.equals("") && oraE.equals(""))
            return true;

        String start = eventHours.substring(0,5);
        String end = eventHours.substring(8);

        Log.e("error", "" + (oraS.compareTo(start) <= 0 && end.compareTo(oraE) <= 0));
        return oraS.compareTo(start) <= 0 && end.compareTo(oraE) <= 0;
    }
}
