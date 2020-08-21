package com.zone.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.atomic.AtomicBoolean;

public class Checker extends AppCompatActivity {

    private LocationManager locationManager;
    private ConstraintLayout location, network;
    private Button activate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checker);
        location = findViewById(R.id.location);
        network = findViewById(R.id.network);
        activate = findViewById(R.id.activate);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        AtomicBoolean checkGPS = new AtomicBoolean(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

        if(checkGPS.get()){
            activate.setVisibility(View.GONE);
            Intent intent = new Intent(Checker.this, SplashScreen.class);
            startActivity(intent);
        }


        if(!checkGPS.get()){
            network.setVisibility(View.GONE);
            location.setVisibility(View.VISIBLE);
        }

        activate.setVisibility(View.VISIBLE);

        activate.setOnClickListener(view -> {

            checkGPS.set(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

            if(checkGPS.get()){
                Intent intent = new Intent(Checker.this, SplashScreen.class);
                startActivity(intent);
            }
        });

    }
}