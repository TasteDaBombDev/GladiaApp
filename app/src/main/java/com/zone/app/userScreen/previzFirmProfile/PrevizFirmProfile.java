package com.zone.app.userScreen.previzFirmProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zone.app.R;
import com.zone.app.utils.EventsDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrevizFirmProfile extends AppCompatActivity {

    private ImageView back;
    private LinearLayout root;
    private ImageView profilePic, coverPic1;
    private TextView descriereF, name, adress, email, temaF, decorF, muzicaF, dresscode, eventName1, type, location, hour;
    private String menu = "";
    private ConstraintLayout rootEvent1, loading;
    private CoordinatorLayout sudo;
    private ViewPager rootFutureEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previz_firm_profile);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {finish(); onBackPressed();});

        init();
        loading.setVisibility(View.VISIBLE);
        fetchData(getIntent().getIntExtra("ID", -1));
    }

    private void init(){
        profilePic = findViewById(R.id.profilePic);
        root = findViewById(R.id.root);
        name = findViewById(R.id.name);
        adress = findViewById(R.id.adress);
        email = findViewById(R.id.mail);
        temaF = findViewById(R.id.tema);
        muzicaF = findViewById(R.id.muzica);
        dresscode = findViewById(R.id.dresscode);
        decorF = findViewById(R.id.decor);
        descriereF = findViewById(R.id.descriereFirma);
        //---------------------------------------------
        coverPic1 = findViewById(R.id.coverPic1);
        rootEvent1 = findViewById(R.id.rootEvent1);
        eventName1 = findViewById(R.id.eventName1);
        type = findViewById(R.id.type1);
        location = findViewById(R.id.location1);
        hour = findViewById(R.id.hour1);
        loading = findViewById(R.id.loading);
        sudo = findViewById(R.id.sudo);
        rootFutureEvents = findViewById(R.id.rootFutureEvents);
        rootFutureEvents.setPadding(100, 0, 100, 0);
        rootFutureEvents.setClipToPadding(false);
        rootFutureEvents.setPageMargin(50);

    }

    private void fetchData(int ID){
        String url = "http://gladiaholdings.com/PHP/utilizatori/fetchFirmDetails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                new Handler().postDelayed(() -> {
                    TransitionManager.beginDelayedTransition(sudo);
                    loading.setVisibility(View.GONE);
                }, 500);

                String pozaPath = jsonObject.getString("poza");
                String nume = jsonObject.getString("nume");
                String mail = jsonObject.getString("mail");
                String dressCode = jsonObject.getString("dressCode");
                String decor = jsonObject.getString("decor");
                String descriere = jsonObject.getString("descriere");
                String muzica = jsonObject.getString("muzica");
                String tema = jsonObject.getString("tema");
                String adresa = jsonObject.getString("adresa");
                menu = jsonObject.getString("menu");

                name.setText(nume);
                adress.setText(adresa);
                email.setText(mail);
                temaF.setText(tema);
                dresscode.setText(dressCode);
                muzicaF.setText(muzica);
                decorF.setText(decor);
                descriereF.setText(descriere);
                Picasso.get().load(pozaPath).into(profilePic);
                generateMenu();

                if(jsonObject.getBoolean("hasEvent")) {
                    eventName1.setText(jsonObject.getString("eventName"));
                    location.setText(jsonObject.getString("eventLocation"));
                    hour.setText(jsonObject.getString("eventHours"));
                    type.setText(jsonObject.getString("type"));
                    Picasso.get().load(jsonObject.getString("eventPic")).placeholder(R.drawable.nopic_round).into(coverPic1, new Callback() {
                        @Override
                        public void onSuccess() {
                            setImageRounded(coverPic1);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                } else {
                    rootEvent1.setVisibility(View.GONE);
                }

                ArrayList<EventsDetails> database = new ArrayList<>();
                database.add(new EventsDetails(
                        "Eveniment de viitor",
                        "12/05/2020 | 10:15 - 12:16",
                        "Petrecere",
                        "https://vibereevents.ro/wp-content/uploads/vibere4.jpg",
                        "Sector 3, Bucuresti, Romania"));
                database.add(new EventsDetails(
                        "Eveniment de viitor",
                        "12/05/2020 | 10:15 - 12:16",
                        "Petrecere",
                        "https://vibereevents.ro/wp-content/uploads/vibere4.jpg",
                        "Sector 3, Bucuresti, Romania"));
                database.add(new EventsDetails(
                        "Eveniment de viitor",
                        "12/05/2020 | 10:15 - 12:16",
                        "Petrecere",
                        "https://vibereevents.ro/wp-content/uploads/vibere4.jpg",
                        "Sector 3, Bucuresti, Romania"));
                database.add(new EventsDetails(
                        "Eveniment de viitor",
                        "12/05/2020 | 10:15 - 12:16",
                        "Petrecere",
                        "https://vibereevents.ro/wp-content/uploads/vibere4.jpg",
                        "Sector 3, Bucuresti, Romania"));

                EnumFragmentsProfile enumFragmentsProfile = new EnumFragmentsProfile(getSupportFragmentManager(), database);
                rootFutureEvents.setAdapter(enumFragmentsProfile);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(PrevizFirmProfile.this, "Error: check your internet connection" + error, Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(ID));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PrevizFirmProfile.this);
        queue.add(stringRequest);
    }

    private void generateMenu(){
        String[] menus = menu.split(", ");
        for (int i = 0; i < menus.length; i++) {
            TextView t = new TextView(PrevizFirmProfile.this);
            menus[i].replace("#", " - ");
            t.setText(menus[i]);
            root.addView(t);
        }
    }

    private void setImageRounded(ImageView pic){
        Bitmap bitmap = ((BitmapDrawable)pic.getDrawable()).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        pic.setImageDrawable(roundedBitmapDrawable);
    }
}