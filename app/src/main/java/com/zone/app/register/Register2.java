package com.zone.app.register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.zone.app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register2 extends Fragment {

    private static String usern, log;
    private static TextInputEditText login, username;
    private static TextInputLayout username_layout,login_layout;
    private static Animation make_error;
    private static Register2 INSTANCE = null;
    private static boolean doi = false;
    private static ConstraintLayout username_layout_cl, login_layout_cl;

    public static String getLogin() {
        return log;
    }

    public static String getUsername() {
        return usern;
    }

    public static boolean getDoi(){
        return doi;
    }

    private View view;

    public Register2(){
    }

    public static Register2 getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new Register2();
        return INSTANCE;
    }

    public static void resetINSTANCE(){
        INSTANCE = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_p2,container,false);
        make_error = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        username = view.findViewById(R.id.username);
        login = view.findViewById(R.id.login);
        username_layout = view.findViewById(R.id.username_layout);
        login_layout = view.findViewById(R.id.login_layout);
        username_layout_cl = view.findViewById(R.id.username_layout_cl);
        login_layout_cl = view.findViewById(R.id.login_layout_cl);

        return view;
    }


    public static void next() {

        usern = username.getText().toString().trim();
        log = login.getText().toString().trim();

        if (usern.length() == 0) {
            username_layout.setErrorEnabled(true);
            username_layout.setError("Campul este gol");
            username_layout_cl.startAnimation(make_error);
            doi = false;
            return;
        }
        else{
            username_layout.setErrorEnabled(false);
        }

        if (log.length() == 0) {
            login_layout.setErrorEnabled(true);
            login_layout.setError("Campul este gol");
            login_layout_cl.startAnimation(make_error);
            doi = false;
            return;
        } else {
            login_layout.setErrorEnabled(false);
        }

        doi = true;

    }
}

