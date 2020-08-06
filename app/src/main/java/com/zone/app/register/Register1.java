package com.zone.app.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.zone.app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Register1 extends Fragment {

    private static TextInputEditText nume,prenume,date;
    private static TextInputLayout prenume_layout,nume_layout;
    private static ConstraintLayout nume_layout_cl,prenume_layout_cl;
    private static String num,prenum,birthDate;
    private static Animation make_error,slideIn;
    private static boolean unu = false;
    private static Register1 INSTANCE;

    private View view;

    public Register1(){
    }

    public static Register1 getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new Register1();
        return INSTANCE;
    }

    public static void resetINSTANCE(){
        INSTANCE = null;
    }

    public static String getNume(){
        return num;
    }

    public static String getPrenume() {
        return prenum;
    }

    public static String getBirthDate() {
        return birthDate;
    }

    public static boolean getUnu(){
        return unu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_p1,container,false);

        make_error = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
        slideIn = AnimationUtils.loadAnimation(getContext(),R.anim.popin);


        nume = view.findViewById(R.id.nume);
        prenume = view.findViewById(R.id.prenume);
//        TextInputLayout date_outline = view.findViewById(R.id.date_outline_cl);
        date = view.findViewById(R.id.zi_nastere);
        prenume_layout = view.findViewById(R.id.prenume_layout);
        nume_layout = view.findViewById(R.id.nume_layout);
        nume_layout_cl = view.findViewById(R.id.nume_layout_cl);
        prenume_layout_cl = view.findViewById(R.id.prenume_layout_cl);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    date.setFocusable(false);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String dateOn = day + "/" + month + "/" + year;
                        date.setText(dateOn);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        return view;
    }

    public static void next(){

        num = nume.getText().toString().trim();
        prenum = prenume.getText().toString().trim();
        birthDate = date.getText().toString().trim();

        if(num.length() == 0)
        {
            nume_layout.setErrorEnabled(true);
            nume_layout.setError("Campul este gol");
            nume_layout_cl.startAnimation(make_error);
            return;
        }
        else{
            nume_layout.setErrorEnabled(false);
        }

        if(prenum.length() == 0)
        {
            prenume_layout.setErrorEnabled(true);
            prenume_layout.setError("Campul este gol");
            prenume_layout_cl.startAnimation(make_error);
            return;
        }
        else{
            prenume_layout.setErrorEnabled(false);
        }

        if(!num.isEmpty() && !prenum.isEmpty())
        {
            unu = true;
        }
    }
}

