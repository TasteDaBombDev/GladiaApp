package com.zone.app.utils;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;

public class LocationProviderByMe{

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location me;
    private boolean located = false;

    public LocationProviderByMe(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;

    }

    public Location getMe() {
        return me;
    }

    public boolean isLocated() {
        return located;
    }
}
