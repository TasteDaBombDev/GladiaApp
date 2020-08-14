package com.zone.app.utils;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.zone.app.R;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private View mymarkerview;

//    CustomInfoAdapter(){
//        mymarkerview = getLayoutInflater()
//                .inflate(R.layout.custominfowindow, null);
//    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mymarkerview);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {

    }
}
