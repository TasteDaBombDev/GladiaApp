package com.zone.app.userScreen.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ZygotePreload;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zone.app.Login;
import com.zone.app.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.zone.app.userScreen.Evenimente.Evenimente;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.zone.app.userScreen.MainScreen;
import com.zone.app.utils.EventsDetails;
import com.zone.app.utils.LocationInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private static MapActivity INSTANCE = null;

    private View view;

    private static GoogleMap map;
    private static ArrayList<LocationInfo> locations = new ArrayList<>();
    private MapView mapView;
    private static final int REQUEST_CODE = 101;
    private static LatLng me;
    private static boolean stillInfo = false;
    private static final int ZOOM = 15;

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
        constructINTERFACE();


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

        if(stillInfo){
            if(me != null){

                MarkerOptions markerOptions = new MarkerOptions().title("me")
                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location, 100,100));
                markerOptions.position(me);

                map.animateCamera(CameraUpdateFactory.newLatLng(me));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(me,ZOOM));

                map.addMarker(markerOptions);

                runServer(getContext());
            } else {
                new Handler().postDelayed(() -> {
                    MarkerOptions markerOptions = new MarkerOptions().title("me")
                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_location_on_24, 100,100));
                    markerOptions.position(me);

                    map.animateCamera(CameraUpdateFactory.newLatLng(me));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(me,ZOOM));

                    map.addMarker(markerOptions);

                    runServer(getContext());
                }, 1000);
            }
        }
    }

    public static void localize(Context c, double lat, double lng){
            me = new LatLng(lat, lng);

            if(map != null){
                stillInfo = false;
                MarkerOptions markerOptions = new MarkerOptions().position(me).title("me")
                        .icon(bitmapDescriptorFromVector(c, R.drawable.location, 100,100));

                map.animateCamera(CameraUpdateFactory.newLatLng(me));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(me, ZOOM));

                map.addMarker(markerOptions);

                runServer(c);
            } else stillInfo = true;


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
                    for (int i = 0; i < jsonObject.length() / 2; i++) {

                        locations.add(new LocationInfo(
                                jsonObject.getDouble("lat" + i),
                                jsonObject.getDouble("lng" + i),
                                jsonObject.getString("name" + i),
                                jsonObject.getString("poza" + i),
                                ""));

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
                    .icon(bitmapDescriptorFromVector(c, R.drawable.ic_baseline_apartment_24, 100,100));
            map.addMarker(markerOptions);
        }
    }
}
