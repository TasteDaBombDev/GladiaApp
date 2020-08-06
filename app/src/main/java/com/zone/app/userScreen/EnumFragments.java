package com.zone.app.userScreen;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zone.app.userScreen.Evenimente.Evenimente;
import com.zone.app.userScreen.map.MapActivity;
import com.zone.app.userScreen.profile.Profile;

public class EnumFragments extends FragmentPagerAdapter {


    private Context context;

    @SuppressWarnings("deprecation")
    public EnumFragments(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        if(position == 0)
            return Profile.getINSTANCE();
        else if(position == 1)
            return MapActivity.getINSTANCE();
        else if(position == 2)
            return Evenimente.getINSTANCE();
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
