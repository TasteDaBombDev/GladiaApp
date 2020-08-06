package com.zone.app.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zone.app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register3 extends Fragment {

    private static TextInputEditText pass,verifPassword;
    private static TextInputLayout pass_layout, verif_pass_layout;
    private static String password,verifpass;
    private static Animation make_error;
    private static Register3 INSTANCE = null;
    private static boolean trei = false;
    private static ConstraintLayout pass_layout_cl, verif_pass_layout_cl;
    View view;

    public Register3(){

    }

    public static Register3 getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new Register3();
        return INSTANCE;
    }

    public static void resetINSTANCE(){
        INSTANCE = null;
    }

    public static String getPassword() {
        return password;
    }

    public static boolean getTrei(){
        return trei;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_p3,container,false);
        pass = view.findViewById(R.id.pass);
        verifPassword = view.findViewById(R.id.verif_pass);
        pass_layout = view.findViewById(R.id.pass_layout);
        verif_pass_layout = view.findViewById(R.id.verif_pass_layout);
        pass_layout_cl = view.findViewById(R.id.pass_layout_cl);
        verif_pass_layout_cl = view.findViewById(R.id.verif_pass_layout_cl);

        make_error = AnimationUtils.loadAnimation(getContext(),R.anim.shake);

        return view;
    }

    public static void next() {
        password = pass.getText().toString().trim();
        verifpass = verifPassword.getText().toString().trim();

        if (password.length() == 0)
        {
            pass_layout.setErrorEnabled(true);
            pass_layout.setError("Campul este gol");
            pass_layout_cl.startAnimation(make_error);
            trei = false;
            return;
        }
        else{
            pass_layout.setErrorEnabled(false);
        }

        if (verifpass.length() == 0)
        {
            trei = false;
            verif_pass_layout.setErrorEnabled(true);
            verif_pass_layout.setError("Campul este gol");
            verif_pass_layout_cl.startAnimation(make_error);
            return;
        }
        else{
            verif_pass_layout.setErrorEnabled(false);
        }

        if (!password.isEmpty() && !verifpass.isEmpty() && password.compareTo(verifpass) == 0) {
            if (password.length() < 8)
            {
                trei = false;
                pass_layout.setErrorEnabled(true);
                pass_layout.setError("Parola trebuie sa fie de cel putin de 8 caractere");
                pass_layout_cl.startAnimation(make_error);
                return;
            }
            else {
                pass_layout.setErrorEnabled(false);

                boolean letterSmall = false;
                boolean letterBig = false;
                boolean digit = false;
                for (int i = 0; i < password.length(); i++) {
                    if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z')
                        letterSmall = true;
                    if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                        letterBig = true;
                    if (password.charAt(i) >= '0' && password.charAt(i) <= '9')
                        digit = true;
                }
                if (letterBig && letterSmall && digit) {
                    trei = true;
                } else
                {
                    trei = false;
                    pass_layout.setErrorEnabled(true);
                    pass_layout.setError("Parola trebuie sa contina minim litere mari, mici si cifre");
                    pass_layout_cl.startAnimation(make_error);
                    return;
                }
            }
        }
        else
        {
            trei = false;
            verif_pass_layout.setErrorEnabled(true);
            verif_pass_layout.setError("Parolele nu coincid!");
            verif_pass_layout_cl.startAnimation(make_error);
            return;
        }

    }
}
