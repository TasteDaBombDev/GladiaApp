package com.zone.app.register.termsAndConditions;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EnumFragmentsTerms extends FragmentPagerAdapter {

    private Context context;

    @SuppressWarnings("deprecation")
    public EnumFragmentsTerms(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Terms1.getINSTANCE();
            case 1:
                return Terms2.getINSTANCE();
            case 2:
                return Terms3.getINSTANCE();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
