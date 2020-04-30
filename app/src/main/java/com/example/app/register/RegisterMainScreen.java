package com.example.app.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.app.R;

import java.util.Random;

public class RegisterMainScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private static String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_screen);

        viewPager = findViewById(R.id.registerSlider);
        EnumFragmentsRegister enumFragmentsRegister = new EnumFragmentsRegister(getSupportFragmentManager(),this);
        viewPager.setAdapter(enumFragmentsRegister);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean scrollStop = true;
            private int positioning = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(positionOffsetPixels > 200 && scrollStop)
                    viewPager.setCurrentItem(position, true);
            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onPageSelected(int position) {
                positioning = position;

            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING){
                    switch (positioning){
                        case 0:
                            RegisterDoi.next();
                            scrollStop = !RegisterDoi.getDoi();
                            break;
                        case 1:
                            RegisterTrei.next();
                            scrollStop = !RegisterTrei.getTrei();
                            break;
                        case 2:
                            RegisterFour.next();
                            scrollStop = !RegisterFour.getPatru();
                            break;
                    }
                }
            }
        });

        code = generateCode();

//        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onBackPressed() {

    }

    private String generateCode(){
        Random r = new Random( System.currentTimeMillis() );
        int a = 10000 + r.nextInt(20000);
        return String.valueOf(a);
    }

    public static String getCode() {
        return code;
    }
}
