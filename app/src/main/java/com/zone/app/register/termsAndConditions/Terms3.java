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

public class Terms3 extends Fragment {

    private static Terms3 INSTANCE = null;
    private View view;

    public Terms3(){

    }

    public static Terms3 getINSTANCE() {
        if(INSTANCE == null)
            return new Terms3();
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
        view = inflater.inflate(R.layout.terms3, container, false);

        return view;
    }
}
