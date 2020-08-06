package com.zone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zone.app.userScreen.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView name;
    private static int SPLASH_TIME = 3000;
    private static final String FILE_NAME = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.appName);

        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String login = br.readLine(), pass = br.readLine();

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if(success){

                            int ID = jsonObject.getInt("userID");
                            String username = jsonObject.getString("username");
                            String nume = jsonObject.getString("nume");
                            String prenume = jsonObject.getString("prenume");
                            String password = jsonObject.getString("password");
                            String mail = jsonObject.getString("mail");
                            String ziuaDeNastere = jsonObject.getString("ziuaDeNastere");
                            String sex = jsonObject.getString("sex");
                            String nrtel = jsonObject.getString("nrtel");


                            Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                            intent.putExtra("userID", ID);
                            intent.putExtra("username",username);
                            intent.putExtra("nume", nume);
                            intent.putExtra("prenume", prenume);
                            intent.putExtra("password", password);
                            intent.putExtra("mail", mail);
                            intent.putExtra("ziuaDeNastere", ziuaDeNastere);
                            intent.putExtra("sex", sex);
                            intent.putExtra("nrtel", nrtel);
                            intent.putExtra("nrAfaceri",jsonObject.getInt("nrAfaceri"));
                            intent.putExtra("nrEvents",jsonObject.getInt("nrEvents"));
                            intent.putExtra("friends", jsonObject.getInt("friends"));
                            intent.putExtra("pannel", 2);


                            startActivity(intent);
                        } else{
                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(logo,"imgTransition");

                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);

                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent, activityOptions.toBundle());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            ServerRequest serverRequest = new ServerRequest("user", login, "pass", pass,"http://gladiaholdings.com/PHP/login.php",listener);
            RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
            queue.add(serverRequest);

        } catch (FileNotFoundException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
