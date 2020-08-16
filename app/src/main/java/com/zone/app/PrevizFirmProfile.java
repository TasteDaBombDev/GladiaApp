package com.zone.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class PrevizFirmProfile extends AppCompatActivity {

    private ImageView back;
    private LinearLayout root;
    private ImageView profilePic;
    private TextView descriereF, name, adress, email, temaF, decorF, muzicaF, dresscode;
    private String menu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previz_firm_profile);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        init();
        fetchData(getIntent().getIntExtra("ID", -1));
    }

    private void init(){
        profilePic = findViewById(R.id.profilePic);
        root = findViewById(R.id.root);
        name = findViewById(R.id.name);
        adress = findViewById(R.id.adress);
        email = findViewById(R.id.mail);
        temaF = findViewById(R.id.tema);
        muzicaF = findViewById(R.id.muzica);
        dresscode = findViewById(R.id.dresscode);
        decorF = findViewById(R.id.decor);
        descriereF = findViewById(R.id.descriereFirma);
    }

    private void fetchData(int ID){
        String url = "http://gladiaholdings.com/PHP/utilizatori/fetchFirmDetails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                    String pozaPath = jsonObject.getString("poza");
                    String nume = jsonObject.getString("nume");
                    String mail = jsonObject.getString("mail");
                    String dressCode = jsonObject.getString("dressCode");
                    String decor = jsonObject.getString("decor");
                    String descriere = jsonObject.getString("descriere");
                    String muzica = jsonObject.getString("muzica");
                    String tema = jsonObject.getString("tema");
                    String adresa = jsonObject.getString("adresa");
                    menu = jsonObject.getString("menu");

                    name.setText(nume);
                    adress.setText(adresa);
                    email.setText(mail);
                    temaF.setText(tema);
                    dresscode.setText(dressCode);
                    muzicaF.setText(muzica);
                    decorF.setText(decor);
                    descriereF.setText(descriere);
                    Picasso.get().load(pozaPath).into(profilePic);
                    generateMenu();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(PrevizFirmProfile.this, "Error: check your internet connection" + error, Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(ID));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PrevizFirmProfile.this);
        queue.add(stringRequest);
    }

    private void generateMenu(){
        String[] menus = menu.split(", ");
        for (int i = 0; i < menus.length; i++) {
            TextView t = new TextView(PrevizFirmProfile.this);
            menus[i].replace("#", " - ");
            t.setText(menus[i]);
            root.addView(t);
        }
    }
}