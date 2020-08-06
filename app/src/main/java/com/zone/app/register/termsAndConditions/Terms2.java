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

public class Terms2 extends Fragment {

    private static Terms2 INSTANCE = null;
    private View view;

    public Terms2(){

    }

    public static Terms2 getINSTANCE() {
        if(INSTANCE == null)
            return new Terms2();
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
        view = inflater.inflate(R.layout.terms2, container, false);

        return view;
    }

}
