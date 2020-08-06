package com.zone.app;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ServerRequest extends StringRequest {
    private Map<String,String> params;

    public ServerRequest(String username, String nume, String prenume, String pass, String login, String date, String link, Response.Listener<String> listener){
        super(Method.POST, link, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("nume", nume);
        params.put("prenume", prenume);
        params.put("pass", pass);
        params.put("nastere", date);
        params.put("sex", "male");

        String regex = "[0-9]+";
        if(login.matches(regex)) {
            params.put("nrtel", login);
            params.put("mail", "-null-");
        } else {
            params.put("nrtel", "-null-");
            params.put("mail", login);
        }
    }

    public ServerRequest(String nothing, String data, String data2, String link, Response.Listener<String> listener){
        super(Method.POST, link, listener, null);
        params = new HashMap<>();
        params.put("data", data);
        params.put("data2", data2);
    }

    public ServerRequest(String fieldName1, String logare,  String fieldName2, String pass, String link, Response.Listener<String> listener){
        super(Method.POST, link, listener, null);
        params = new HashMap<>();
        params.put(fieldName1,logare);
        params.put(fieldName2, pass);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
