package com.zone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.zone.app.register.termsAndConditions.TermsAndConditionsMain;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView welcome = findViewById(R.id.welcome);
        final Button login = findViewById(R.id.login_start), register = findViewById(R.id.register_start);
        login.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
        final Animation anim = AnimationUtils.loadAnimation(this,R.anim.popin), popin = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        anim.setDuration(1000);
        popin.setDuration(500);
        welcome.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                login.startAnimation(popin);
                register.startAnimation(popin);
            }
        },2000);
        login.setBackgroundResource(R.drawable.circle);
        register.setBackgroundResource(R.drawable.circle);

    }


    @Override
    public void onBackPressed() {

    }



    public void openSecondScreen(View view) {
        Intent intent = new Intent(this, TermsAndConditionsMain.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
