package com.example.app.userScreen.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.app.Login;
import com.example.app.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private static MapActivity INSTANCE = null;

    private View view;

    private static GoogleMap map;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Location me;

    public MapActivity(){

    }

    public static MapActivity getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new MapActivity();
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
        view = inflater.inflate(R.layout.map_fragment,container,false);

        ConstraintLayout aggroZoneProfile = view.findViewById(R.id.toProfile), aggroZoneEvents = view.findViewById(R.id.toEvents);

        aggroZoneEvents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    map.getUiSettings().setScrollGesturesEnabled(false);
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    map.getUiSettings().setScrollGesturesEnabled(true);
                    return false;
                }
                return false;
            }
        });


        aggroZoneProfile.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    map.getUiSettings().setScrollGesturesEnabled(false);
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    map.getUiSettings().setScrollGesturesEnabled(true);
                    return false;
                }
                return false;
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        ImageButton imageButton = view.findViewById(R.id.localizeMe);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                localise(map);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);

        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        if(Login.getLoading() != null)
            Login.getLoading().dismiss();
        MapsInitializer.initialize(getContext());
        map = googleMap;
        localise(googleMap);
    }

    private void localise(final GoogleMap googleMap){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(me != null) {
                    LatLng myLocation = new LatLng(me.getLatitude(),me.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions().position(myLocation).title("Me");

                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

                    googleMap.addMarker(markerOptions);
                }
            }
        },500);
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null)
                    me = location;
                else
                    Toast.makeText(getContext(), "Deschide-ti locatia", Toast.LENGTH_SHORT).show();
            }
        });

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
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null){
//                    me = location;
//                    SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.mapView);
//                    mapFragment.getMapAsync(MapActivity.this);
//                }
//                else
//                    Toast.makeText(getContext(), "Deschide-ti locatia", Toast.LENGTH_SHORT).show();
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
//
    public static GoogleMap getMap() {
        return map;
    }
}
