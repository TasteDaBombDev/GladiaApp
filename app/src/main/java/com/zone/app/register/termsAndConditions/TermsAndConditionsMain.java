package com.zone.app.register.termsAndConditions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.zone.app.MainActivity;
import com.zone.app.R;
import com.zone.app.register.RegisterMainScreen;

public class TermsAndConditionsMain extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions_main);

        viewPager = findViewById(R.id.tcPager);
        EnumFragmentsTerms enumFragmentsTerms = new EnumFragmentsTerms(getSupportFragmentManager(),this);
        viewPager.setAdapter(enumFragmentsTerms);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        final Animation popin = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        popin.setDuration(500);

        final Button toRegister = findViewById(R.id.toRegister), cancelRegister = findViewById(R.id.cancelRegister);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterMainScreen.class);
                startActivity(intent);
            }
        });

        cancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        final TextView text = findViewById(R.id.text);
        final String[] strings = new String[3];
        strings[0] = "You will make a lot of friends and you will interact with them so good";
        strings[1] = "All the events near you are listed so that you can see them in real time.";
        strings[2] = "You can all the time select from the best businesses all the time!";

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        text.setText(strings[0]);
                        break;
                    case 1:
                        text.setText(strings[1]);
                        break;
                    case 2:
                        if(toRegister.getVisibility() != View.VISIBLE)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toRegister.setVisibility(View.VISIBLE);
                                    cancelRegister.setVisibility(View.VISIBLE);
                                    toRegister.startAnimation(popin);
                                    cancelRegister.startAnimation(popin);
                                }
                            },3000);
                        text.setText(strings[2]);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
}

