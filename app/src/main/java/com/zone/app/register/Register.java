package com.zone.app.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zone.app.userScreen.MainScreen;
import com.zone.app.R;
import com.zone.app.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Register extends AppCompatActivity {

    private static final String FILE_NAME = "data.txt";
    private String nume = Register1.getNume() + "";
    private String prenume = Register1.getPrenume() + "";
    private String date = Register1.getBirthDate() + "";
    private String username = Register2.getUsername() + "";
    private String login = Register2.getLogin() + "";
    private String pass = Register3.getPassword() + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        openApp();
    }

    public void openApp() {
        //register


        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.getString("msg");
                String username = jsonObject.getString("username");
                int userID = jsonObject.getInt("userID");

                String nume = jsonObject.getString("nume");
                String prenume = jsonObject.getString("prenume");
                String password = jsonObject.getString("password");
                String mail = jsonObject.getString("mail");
                String ziuaDeNastere = jsonObject.getString("ziuaDeNastere");
                String sex = jsonObject.getString("sex");
                String nrtel = jsonObject.getString("nrtel");

                if(success){

                    createFile();

                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainScreen.class);
                    intent.putExtra("idUser", userID);
                    intent.putExtra("username",username);

                    intent.putExtra("nume", nume);
                    intent.putExtra("prenume", prenume);
                    intent.putExtra("password", password);
                    intent.putExtra("mail", mail);
                    intent.putExtra("ziuaDeNastere", ziuaDeNastere);
                    intent.putExtra("sex", sex);
                    intent.putExtra("nrtel", nrtel);
                    intent.putExtra("pannel",2);
                    intent.putExtra("fromRegister", true);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"JSON_err",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        };

        ServerRequest serverRequest = new ServerRequest(username,nume,prenume,pass,login,date,"http://gladiaholdings.com/PHP/register.php",responseListener);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(serverRequest);
//        finish();

    }

    @Override
    public void onBackPressed() {

    }

    private void createFile() {
        FileOutputStream fos = null;
        String text = login + "\n" + pass;
        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
