package com.zone.app.userScreen.previzFirmProfile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zone.app.utils.EventsDetails;

import java.util.ArrayList;

public class EnumFragmentsProfile extends FragmentPagerAdapter {

    ArrayList<EventsDetails> database;

    public EnumFragmentsProfile(@NonNull FragmentManager fm, ArrayList<EventsDetails> database) {
        super(fm);
        this.database = database;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new EventFragmentForProfile(database.get(position));
    }

    @Override
    public int getCount() {
        return database.size();
    }
}
