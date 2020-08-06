package com.zone.app.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zone.app.R;
import com.zone.app.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class RegisterMainScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private static String code;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_screen);

        viewPager = findViewById(R.id.registerSlider);
        EnumFragmentsRegister enumFragmentsRegister = new EnumFragmentsRegister(getSupportFragmentManager(),this);
        viewPager.setAdapter(enumFragmentsRegister);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        code = generateCode();
        final int[] position = {0};

        final ImageButton next = findViewById(R.id.toNextSlide);
        final ImageButton prev = findViewById(R.id.toPrevSlide);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (position[0]){
                    case 0:
                        next.setVisibility(View.VISIBLE);
                        Register1.next();
                        if(Register1.getUnu())
                        {
                            prev.setVisibility(View.VISIBLE);
                            viewPager.setCurrentItem(1,false);
                            position[0] = 1;
                        }
                        break;
                    case 1:
                        prev.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        Register2.next();
                        if(Register2.getDoi())
                        {
                            viewPager.setCurrentItem(2,false);
                            next.setVisibility(View.VISIBLE);
                            prev.setVisibility(View.VISIBLE);
                            position[0] = 2;
                        }
                        break;
                    case 2:
                        next.setVisibility(View.VISIBLE);
                        Register3.next();
                        if(Register3.getTrei())
                        {
                            viewPager.setCurrentItem(3,false);
                            next.setVisibility(View.INVISIBLE);
                            prev.setVisibility(View.VISIBLE);
                            position[0] = 3;
                        }
                        break;
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position[0]){
                    case 1:
                        Register2.next();
                        if(Register2.getDoi())
                        {
                            viewPager.setCurrentItem(0,false);
                            position[0] = 0;
                            prev.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        Register3.next();
                        if(Register3.getTrei())
                        {
                            viewPager.setCurrentItem(1,false);
                            next.setVisibility(View.VISIBLE);
                            prev.setVisibility(View.VISIBLE);
                            position[0] = 1;
                        }
                        break;
                    case 3:
                        viewPager.setCurrentItem(2,false);
                        prev.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        position[0] = 2;
                        break;
                }
            }
        });

        viewPager.setOffscreenPageLimit(1);
    }

    private boolean isValid(){
        final boolean[] validUsername = {false};

        Toast.makeText(getApplicationContext(),"asasda",Toast.LENGTH_SHORT).show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            validUsername[0] = true;
                            Toast.makeText(getApplicationContext(),"merge",Toast.LENGTH_SHORT).show();
                        } else {
                            String message = jsonObject.getString("msg");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "JSON_err", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            };

        RequestQueue queue = Volley.newRequestQueue(this);

        String regex = "[0-9]+";
        if (Register2.getLogin().matches(regex)) {
            ServerRequest registerRequest2 = new ServerRequest("0", Register2.getLogin(), Register2.getUsername(), "http://gladiaholdings.com/PHP/checkTelefon.php", responseListener);
            queue.add(registerRequest2);
        } else {
            ServerRequest registerRequest2 = new ServerRequest("0", Register2.getLogin(), Register2.getUsername(), "http://gladiaholdings.com/PHP/checkMail.php", responseListener);
            queue.add(registerRequest2);
        }

        return validUsername[0];
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
