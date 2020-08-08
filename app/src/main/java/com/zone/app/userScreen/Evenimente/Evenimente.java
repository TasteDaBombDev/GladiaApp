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
import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zone.app.R;
import com.zone.app.utils.AVLtree;
import com.zone.app.utils.EventsDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Evenimente extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static Evenimente INSTANCE = null;

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
    private TextView filtersText, distanceAfis;
    private RecyclerView recyclerView;

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
        StaggeredGridLayoutManager managerListing = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(managerListing);

        ArrayList<String> personNames = new ArrayList<>();
        personNames.add("Nume1");
        personNames.add("nume2");
        personNames.add("nume3");

        ArrayList<String> personImages = new ArrayList<>();
        personImages.add("https://lp-cms-production.imgix.net/2019-06/3cb45f6e59190e8213ce0a35394d0e11-nice.jpg");
        personImages.add("https://wallpapercave.com/wp/wp2550666.jpg");
        personImages.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRWbe0WDr9S3d09ASHjriu0DDnoM_pQeFaXNA&usqp=CAU");

        CustomAdapter customAdapter = new CustomAdapter(getContext(), personNames, personImages);
        recyclerView.setAdapter(customAdapter);

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
            }
        });

        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                distanceAfis.setText("Departarea fata de mine: " + i + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public static void setEventsNearMe(final Context c, final double lat, final double lng){
        String urlUpload = "http://gladiaholdings.com/PHP/utilizatori/findEventsNearMe.php";

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i < jsonObject.length() / 7; i++) {
                        database.addVal(new EventsDetails(i,
                                jsonObject.getDouble("distance" + i),
                                jsonObject.getInt("id" + i),
                                jsonObject.getString("poza" + i),
                                jsonObject.getString("nume" + i),
                                jsonObject.getString("date" + i),
                                jsonObject.getString("hours" + i),
                                jsonObject.getString("type" + i)));
                    }
                    Log.e("database", database.toString());
                } catch (JSONException e) {
                    Toast.makeText(c, "Error loading your event" + response, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, "Check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));
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
    }
}
