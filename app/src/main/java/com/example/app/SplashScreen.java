package com.example.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView name;
    private static int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo = (ImageView) findViewById(R.id.logo);
        name = findViewById(R.id.appName);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(logo,"imgTransition");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);


                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent, activityOptions.toBundle());


            }
        },SPLASH_TIME);

    }
}
