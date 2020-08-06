package com.zone.app.userScreen.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EnumFragmentsForProfile extends FragmentPagerAdapter {


    private Context context;

    @SuppressWarnings("deprecation")
    public EnumFragmentsForProfile(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0)
            return Profile.getINSTANCE();
        else if(position == 1)
            return PrivateEvents.getINSTANCE();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
