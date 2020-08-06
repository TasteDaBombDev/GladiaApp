package com.zone.app.userScreen.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zone.app.R;
import com.zone.app.utils.VerticalViewPager;

public class ProfileMainClass extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static ProfileMainClass INSTANCE = null;

    private View view;
    private static VerticalViewPager verticalViewPager;

    public ProfileMainClass(){
    }

    public static ProfileMainClass getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new ProfileMainClass();
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
        view = inflater.inflate(R.layout.profile_home,container,false);
        verticalViewPager = view.findViewById(R.id.vVP);
        assert getFragmentManager() != null;
        EnumFragmentsForProfile enumFragmentsForProfile = new EnumFragmentsForProfile(getFragmentManager(),getContext());
        verticalViewPager.setAdapter(enumFragmentsForProfile);
        verticalViewPager.setOffscreenPageLimit(2);

        return view;
    }

    public static VerticalViewPager getVerticalViewPager(){
        return verticalViewPager;
    }

}
