package com.zone.app.register.termsAndConditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zone.app.R;

public class Terms1 extends Fragment {

    private static Terms1 INSTANCE = null;
    private View view;

    public Terms1(){

    }

    public static Terms1 getINSTANCE() {
        if(INSTANCE == null)
            return new Terms1();
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.terms1, container, false);

        return view;
    }
}
