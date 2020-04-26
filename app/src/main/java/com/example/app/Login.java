package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.app.userScreen.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
    }

    @Override
    public void onBackPressed() {

    }

    public void login(View view) {
        EditText data = findViewById(R.id.cod);
        EditText pass = findViewById(R.id.login_pass);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");

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

                        Button relative = findViewById(R.id.login_btn);

//                        Pair[] pairs = new Pair[1];
//                        pairs[0] = new Pair<View, String>(relative,"imgTransition");
//
//                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);


                        Intent intent = new Intent(Login.this, MainScreen.class);
                        intent.putExtra("userID", ID);
                        intent.putExtra("username",username);
                        intent.putExtra("nume", nume);
                        intent.putExtra("prenume", prenume);
                        intent.putExtra("password", password);
                        intent.putExtra("mail", mail);
                        intent.putExtra("ziuaDeNastere", ziuaDeNastere);
                        intent.putExtra("sex", sex);
                        intent.putExtra("nrtel", nrtel);
                        intent.putExtra("pannel", 2);

                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else{
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ServerRequest serverRequest = new ServerRequest("user", (data.getText().toString().trim()), "pass", (pass.getText().toString().trim()),"http://gladiaholdings.com/PHP/login.php",listener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(serverRequest);


    }
}
