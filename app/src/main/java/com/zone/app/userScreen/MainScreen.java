package com.zone.app.userScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zone.app.R;
import com.zone.app.userScreen.Evenimente.Evenimente;
import com.zone.app.userScreen.map.MapActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainScreen extends AppCompatActivity implements OnMapReadyCallback {

    private static String username;
    private ViewPager viewPager;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;

    private static int userID, afaceri, events, friends;
    private static boolean tutorial;
    private static String nume;
    private static String prenume;
    private static String password;
    private static String mail;
    private static String ziuaDeNastere;
    private static String sex;
    private static String nrtel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_screen);
        getUserData();

        viewPager = findViewById(R.id.mainSlider);

        EnumFragments enumFragments = new EnumFragments(getSupportFragmentManager(), this);
        viewPager.setAdapter(enumFragments);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean scrollStarted = false;


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(false);
                } else {
                    scrollStarted = false;
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
                }
            }
        });
        viewPager.setPageTransformer(false, (page, position) -> {
//            final float normalizedposition = Math.abs(Math.abs(position) - 1);
//            page.setScaleX(normalizedposition / 2 + 0.5f);
//            page.setScaleY(normalizedposition / 2 + 0.5f);
        });

        client = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        userID = extras.getInt("userID");
        username = extras.getString("username");
        nume = extras.getString("nume");
        prenume = extras.getString("prenume");
        password = extras.getString("password");
        mail = extras.getString("mail");
        ziuaDeNastere = extras.getString("ziuaDeNastere");
        sex = extras.getString("sex");
        afaceri = extras.getInt("nrAfaceri");
        events = extras.getInt("nrEvents");
        friends = extras.getInt("friends");
        nrtel = extras.getString("nrtel");
        tutorial = extras.getBoolean("fromRegister");
    }

    public static int getUserID() {
        return userID;
    }

    public static String getUsername() {
        return username;
    }

    public static String getNume() {
        return nume;
    }

    public static String getPrenume() {
        return prenume;
    }

    public static String getPassword() {
        return password;
    }

    public static String getMail() {
        return mail;
    }

    public static String getZiuaDeNastere() {
        return ziuaDeNastere;
    }

    public static String getSex() {
        return sex;
    }

    public static String getNrtel() {
        return nrtel;
    }

    public static boolean startTutorial() {
        return tutorial;
    }

    public static int getAfaceri() {
        return afaceri;
    }

    public static int getEvents() {
        return events;
    }

    public static int getFriends() {
        return friends;
    }

    @Override
    public void onBackPressed() {
        //Nothing
    }

    public void getLocation() {
        requestPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        client.getLastLocation().addOnSuccessListener(MainScreen.this, location -> {
            if(location != null) {
                MapActivity.localize(MainScreen.this, location.getLatitude(), location.getLongitude());
                Evenimente.setEventsNearMe(MainScreen.this, location.getLatitude(), location.getLongitude());
            } else {
                locationRequest = new LocationRequest();
                locationRequest
                        .setFastestInterval(2000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(4000);


                requestPermission();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                client.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        locationResult = null;
                        MapActivity.localize(MainScreen.this, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    }
                }, getMainLooper());
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
