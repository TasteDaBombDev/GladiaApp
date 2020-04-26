package com.example.app.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class RegisterDoi extends Fragment {


    private static RegisterDoi INSTANCE = null;
    private static TextInputEditText nume,prenume,date;
    private static TextInputLayout prenume_layout,nume_layout;
    private static ConstraintLayout doi_layout;
    private static String num,prenum,birthDate;
    private static Animation make_error,slideIn;
    private static boolean doi = false;

    View view;

    public RegisterDoi(){
    }

    public static RegisterDoi getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new RegisterDoi();
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

    public static boolean getDoi(){
        return doi;
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
        TextInputLayout date_outline = view.findViewById(R.id.date_outline);
        date = view.findViewById(R.id.zi_nastere);
        prenume_layout = view.findViewById(R.id.prenume_layout);
        nume_layout = view.findViewById(R.id.nume_layout);
        doi_layout = view.findViewById(R.id.doi);

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


//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.register_p1);
//        make_error = AnimationUtils.loadAnimation(this,R.anim.shake);
//
//        nume = findViewById(R.id.nume);
//        prenume = findViewById(R.id.prenume);
//        date_outline = findViewById(R.id.date_outline);
//        prenume_outline = findViewById(R.id.prenume_outline);
//        nume_outline = findViewById(R.id.nume_outline);
//        date = findViewById(R.id.zi_nastere);
//
//        nume.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                {
//                    nume_outline.setHasFocus(true);
//                    prenume_outline.setHasFocus(false);
//                    date_outline.setHasFocus(false);
//                }
//            }
//        });
//
//        prenume.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                {
//                    nume_outline.setHasFocus(false);
//                    prenume_outline.setHasFocus(true);
//                    date_outline.setHasFocus(false);
//                }
//            }
//        });
//
//        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                {
//                    nume_outline.setHasFocus(false);
//                    prenume_outline.setHasFocus(false);
//                    date_outline.setHasFocus(true);
//                }
//            }
//        });
//
//
//        nume_outline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!nume_outline.hasFocus()){
//                    prenume_outline.setHasFocus(false);
//                    date_outline.setHasFocus(false);
//                    nume_outline.setHasFocus(true);
//                }
//                else nume_outline.setHasFocus(false);
//            }
//        });
//
//        prenume_outline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!prenume_outline.hasFocus()){
//                    nume_outline.setHasFocus(false);
//                    date_outline.setHasFocus(false);
//                    prenume_outline.setHasFocus(true);
//                }
//                else prenume_outline.setHasFocus(false);
//            }
//        });
//
//
//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                    date.setFocusable(false);
//            }
//        });
//
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterDoi.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        month = month + 1;
//                        String dateOn = day + "/" + month + "/" + year;
//                        date.setText(dateOn);
//                    }
//                },year,month,day);
//                datePickerDialog.show();
//            }
//        });
//
//        date_outline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!date_outline.hasFocus()){
//                    prenume_outline.setHasFocus(false);
//                    date_outline.setHasFocus(true);
//                    nume_outline.setHasFocus(false);
//                }
//                else date_outline.setHasFocus(false);
//            }
//        });
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//    }
//
//    private float x1,x2,y1,y2;
//
//


    public static void animate(){
        doi_layout.startAnimation(slideIn);
    }

    public static void next(){

        num = nume.getText().toString().trim();
        prenum = prenume.getText().toString().trim();
        birthDate = date.getText().toString().trim();

        if(num.length() == 0)
        {
            nume_layout.setErrorEnabled(true);
            nume_layout.setError("Campul este gol");
            nume_layout.startAnimation(make_error);
            return;
        }
        else{
            nume_layout.setErrorEnabled(false);
        }

        if(prenum.length() == 0)
        {
            prenume_layout.setErrorEnabled(true);
            prenume_layout.setError("Campul este gol");
            prenume_layout.startAnimation(make_error);
            return;
        }
        else{
            prenume_layout.setErrorEnabled(false);
        }

        if(!num.isEmpty() && !prenum.isEmpty())
        {
            doi = true;
        }
    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
}
