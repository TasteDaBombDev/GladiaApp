package com.zone.app.register;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EnumFragmentsRegister extends FragmentPagerAdapter {

    private Context context;

    @SuppressWarnings("deprecation")
    public EnumFragmentsRegister(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        if(position == 0)
            return Register1.getINSTANCE();
        else if(position == 1)
            return Register2.getINSTANCE();
        else if(position == 2)
            return Register3.getINSTANCE();
        else
            return Register4.getINSTANCE();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
