package com.zone.app.userScreen.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.zone.app.Login;
import com.zone.app.userScreen.AccurracyChanger;
import com.zone.app.userScreen.Evenimente.Evenimente;
import com.zone.app.userScreen.previzFirmProfile.PrevizFirmProfile;
import com.zone.app.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zone.app.utils.LocationInfo;
import com.zone.app.utils.LocationTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private static MapActivity INSTANCE = null;

    private View view;
    private Marker thisIsMe = null;
    private ArrayList<String> searches = new ArrayList<>();
    private ListView results;
    private SearchView search;
    private boolean overlay = true;
    private boolean openMenu = false;
    private boolean tracking = false;

    private static GoogleMap map;
    private static ArrayList<LocationInfo> locations = new ArrayList<>();
    private static MapView mapView;
    private static LatLng me;
    private static final int ZOOM = 15;
    private ConstraintLayout profile, rootMain, profileComp, searchCanvas;
    private LinearLayout root, rootMore;
    private ImageView im;
    private int currentID = -1;
    private TextView profileName;
    private ImageButton accuracy, more, track;

    public MapActivity() {
    }

    public static MapActivity getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new MapActivity();
        return INSTANCE;
    }

    public static void resetINSTANCE() {
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

        view = inflater.inflate(R.layout.map_fragment, container, false);

        profile = view.findViewById(R.id.profile);
        rootMain = view.findViewById(R.id.root);
        root = view.findViewById(R.id.listAtribues);
        im = view.findViewById(R.id.profilePicImg);
        profileComp = view.findViewById(R.id.profileComp);
        profileName = view.findViewById(R.id.profileName);
        profile.setVisibility(View.GONE);

        results = view.findViewById(R.id.resultsMap);
        search = view.findViewById(R.id.search);
        searchCanvas = view.findViewById(R.id.searchCanvas);

        rootMore = view.findViewById(R.id.rootMore);
        more = view.findViewById(R.id.more);
        track = view.findViewById(R.id.track);

        track.setOnClickListener(view -> {
            Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    if(tracking){
                        localiseWithoutMoving();
                        handler.postDelayed(this, 6000);
                    }
                }
            };
            if(!tracking){
                track.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.rubyred));
                handler.postDelayed(run, 0);
                tracking = true;
            } else {
                track.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                tracking = false;
            }
        });

        more.setOnClickListener(view -> {

            TransitionManager.beginDelayedTransition(searchCanvas);

            if(!openMenu){
                rootMore.setVisibility(View.VISIBLE);
                openMenu = true;
            } else {
                rootMore.setVisibility(View.GONE);
                openMenu = false;
            }
        });

        accuracy = view.findViewById(R.id.accuracy);

        accuracy.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AccurracyChanger.class);
            startActivity(intent);
        });

        results.setAdapter(new ArrayAdapter<>(getContext(), R.layout.text_search_result, R.id.txt, searches));


        search.setOnSearchClickListener(view -> {
            ConstraintSet set2 = new ConstraintSet();
            set2.clone(searchCanvas);

            TransitionManager.beginDelayedTransition(searchCanvas);

            set2.constrainPercentHeight(R.id.resultsMap, 1.0f);
            set2.constrainPercentHeight(R.id.resultsBackground, 1.0f);
            set2.setHorizontalBias(R.id.search, 0.5f);
            set2.applyTo(searchCanvas);

        });

        search.setOnCloseListener(() -> {
            ConstraintSet set2 = new ConstraintSet();
            set2.clone(searchCanvas);

            TransitionManager.beginDelayedTransition(searchCanvas);

            set2.constrainPercentHeight(R.id.resultsMap, 0);
            set2.constrainPercentHeight(R.id.resultsBackground, 0.0f);
            set2.setHorizontalBias(R.id.search, 0f);
            set2.applyTo(searchCanvas);
            return false;
        });

        constructINTERFACE();
        ImageView btnOpen = view.findViewById(R.id.openProfle);
        btnOpen.setOnClickListener(view -> {


            Intent intent = new Intent(getContext(), PrevizFirmProfile.class);
            intent.putExtra("ID", currentID);

            startActivity(intent);

        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (Login.getLoading() != null)
            Login.getLoading().dismiss();
        MapsInitializer.initialize(getContext());
        map = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        map.getUiSettings().setMapToolbarEnabled(false);

        localise();

        googleMap.setOnMarkerClickListener(marker -> {

            for (int i = 0; i < locations.size(); i++) {
                if(marker.getSnippet().equals(locations.get(i).getTAG()) && !locations.get(i).isOpened()){
                    currentID = locations.get(i).getId();

                    locations.get(i).setOpened(true);
                    profile.setVisibility(View.VISIBLE);

                    Picasso.get().load(locations.get(i).getPath()).placeholder(R.drawable.nopic).into(im);

                    profileName.setText(locations.get(i).getName());

                    root.removeAllViews();
                    for (int j = 0; j < locations.get(i).getAtributes().length; j++) {
                        TextView p = new TextView(getContext());
                        p.setText(locations.get(i).getAtributes()[j].trim());
                        p.setPadding(0,10,0,0);
                        root.addView(p);
                    }
                    for (int j = 0; j < 5; j++) {
                        TextView p = new TextView(getContext());
                        p.setText("bla bla bla");
                        p.setPadding(0,10,0,0);
                        root.addView(p);
                    }
                } else {
                    locations.get(i).setOpened(false);
                    profile.setVisibility(View.GONE);
                }
            }
            return false;
        });

        googleMap.setOnMapClickListener(latLng -> {
            TransitionManager.beginDelayedTransition(rootMain);
            if(overlay){
                searchCanvas.setVisibility(View.VISIBLE);
                overlay = false;
            } else {
                searchCanvas.setVisibility(View.GONE);
                overlay = true;
            }
        });

    }

    private void setImageRounded(ImageView pic){
        Bitmap bitmap = ((BitmapDrawable)pic.getDrawable()).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCornerRadius(40);
        pic.setImageDrawable(roundedBitmapDrawable);
    }

    private static BitmapDescriptor bitmapDescriptorFromVector(Context c, int vectorResource, int width, int height){
        Drawable vectorDrawable = ContextCompat.getDrawable(c, vectorResource);
        vectorDrawable.setBounds(0,0, width, height);
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(b);
    }

    private static void runServer(Context c){
        String urlUpload = "http://gladiaholdings.com/PHP/utilizatori/findLocationsNearMe.php";

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i < jsonObject.length() / 6; i++) {

                        locations.add(new LocationInfo(
                                jsonObject.getInt("id" + i),
                                jsonObject.getDouble("lat" + i),
                                jsonObject.getDouble("lng" + i),
                                jsonObject.getString("name" + i),
                                jsonObject.getString("poza" + i),
                                jsonObject.getString("atributes" + i),
                                i + ""));

                    }
                    constructLocations(c);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(c, "Check your internet connection and try again.", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(me.latitude));
                params.put("lng", String.valueOf(me.longitude));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(stringRequest);
    }

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(this, "hey din onCreate", Toast.LENGTH_SHORT).show();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_fragment);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getLastLocation();
//    }
//
//    private void getLastLocation() {
//        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//            return;
//        }
//        Toast.makeText(getContext(), "hey...", Toast.LENGTH_SHORT).show();
//        Task<Location> task = client.getLastLocation();
//        task.addOnSuccessListener(location -> {
//            if(location != null){
//                me = location;
//                SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.mapView);
//                mapFragment.getMapAsync(MapActivity.this);
//            }
//            else {
//                Toast.makeText(getContext(), "Deschide-ti locatia", Toast.LENGTH_SHORT).show();
//                locationRequest = LocationRequest.create();
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                locationRequest.setInterval(20 * 1000);
//                locationCallback = new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        if (locationResult == null) {
//                            return;
//                        }
//                        for (Location location : locationResult.getLocations()) {
//                            if (location != null) {
//                                wayLatitude = location.getLatitude();
//                                wayLongitude = location.getLongitude();
//                                Toast.makeText(getContext(), wayLatitude + " - " + wayLongitude, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                };
//                client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//
//        MapStyleOptions mapStyleOptions= MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style);
//        googleMap.setMapStyle(mapStyleOptions);
//
//        LatLng point = new LatLng(me.getLatitude(),me.getLongitude());
//
//        MarkerOptions markerOptions = new MarkerOptions().position(point).title("Me");
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,16));
//
//        googleMap.addMarker(markerOptions);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }

    public static GoogleMap getMap() {
        return map;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void constructINTERFACE(){
        ConstraintLayout aggroZoneProfile = view.findViewById(R.id.toProfile), aggroZoneEvents = view.findViewById(R.id.toEvents);

        aggroZoneEvents.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                map.getUiSettings().setScrollGesturesEnabled(false);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                map.getUiSettings().setScrollGesturesEnabled(true);
                return false;
            }
            return false;
        });


        aggroZoneProfile.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                map.getUiSettings().setScrollGesturesEnabled(false);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                map.getUiSettings().setScrollGesturesEnabled(true);
                return false;
            }
            return false;
        });

        ImageButton imageButton = view.findViewById(R.id.localizeMe);

        imageButton.setOnClickListener(v -> {
            if(me != null){
                map.animateCamera(CameraUpdateFactory.newLatLng(me));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(me,ZOOM));
            }
        });
    }

    private static void constructLocations(Context c){
        for (int i = 0; i < locations.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(locations.get(i).getLat(), locations.get(i).getLng()))
                    .icon(bitmapDescriptorFromVector(c, R.drawable.ic_baseline_local_bar_24, 100,100))
                    .snippet(locations.get(i).getTAG());
            map.addMarker(markerOptions);
        }
    }

    private void localise(){
        if(thisIsMe != null)
            thisIsMe.remove();

        LocationTrack locationTrack = new LocationTrack(getContext(), getActivity());

        if (locationTrack.canGetLocation()) {

            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            if(latitude == 0 && longitude == 0) {
                LocationTrack locationTrack1 = new LocationTrack(getContext(), getActivity());

                if (locationTrack1.canGetLocation()) {

                    double longitude1 = locationTrack1.getLongitude();
                    double latitude1 = locationTrack1.getLatitude();

                    me = new LatLng(latitude1, longitude1);

                    MarkerOptions markerOptions = new MarkerOptions().position(me).title("me").snippet("")
                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location, 100, 100));

                    map.animateCamera(CameraUpdateFactory.newLatLng(me));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(me, ZOOM));

                    thisIsMe = map.addMarker(markerOptions);

                    runServer(getContext());

                    return;
                }
            }

            me = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions().position(me).title("me").snippet("")
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location, 100, 100));

            map.animateCamera(CameraUpdateFactory.newLatLng(me));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(me, ZOOM));

            thisIsMe = map.addMarker(markerOptions);

            runServer(getContext());

            Evenimente.setEventsNearMe(getContext(), latitude, longitude);
        }
    }

    private void localiseWithoutMoving(){
        if(thisIsMe != null)
            thisIsMe.remove();

        LocationTrack locationTrack = new LocationTrack(getContext(), getActivity());

        if (locationTrack.canGetLocation()) {

            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            me = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions().position(me).title("me").snippet("")
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location, 100, 100));

            thisIsMe = map.addMarker(markerOptions);

            runServer(getContext());
        }
    }
}
