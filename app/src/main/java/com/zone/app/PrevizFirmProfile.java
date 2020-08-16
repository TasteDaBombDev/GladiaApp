package com.zone.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class PrevizFirmProfile extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previz_firm_profile);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchData(int ID){
        String url = "http://gladiaholdings.com/PHP/fetchDetails.php";
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String permisiuni = jsonObject.getString("permisiuni");
                    for (int i = 0; i < permisiuni.length(); i++) {

                        Log.w("permisiuni", String.valueOf(permisiuni.charAt(i)));
                        if(permisiuni.charAt(i) == '1')
                        {
                            firmaDetails.add(VIEWS[i]);
                        }
                    }
                    final LinearLayout list = item.findViewById(R.id.listDetails);
                    for (int i = 0; i < permisiuni.length(); i++) {
                        ConstraintLayout c = item.findViewById(CONSTRAINT_VIEWS[i]);
                        CheckBox cb = (CheckBox) c.getChildAt(3);
                        if(permisiuni.charAt(i) == '1')
                            cb.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PrevizFirmProfile.this, "Error: check your internet connection" + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(ID));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
}