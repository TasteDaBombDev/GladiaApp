package com.zone.app.userScreen.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zone.app.R;
import com.zone.app.userScreen.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PrivateEvents extends Fragment {

    private static PrivateEvents INSTANCE = null;
    private Bitmap bitmap;
    private EditText dateStart, timeStart, dateEnd, timeEnd, title, categorie,descriere,tinuta;
    private TextView codeEvent;
    private ConstraintLayout code_overlay;
    private ImageView previz;
    private String urlUpload = "http://gladiaholdings.com/PHP/createEvent.php";
    private Button createEvent;
    private ProgressDialog loading;
    private static final int IMG_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    View view;

    public PrivateEvents(){

    }

    public static PrivateEvents getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new PrivateEvents();
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
        view = inflater.inflate(R.layout.private_events_fragment,container,false);
        init();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        View.OnClickListener dateListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String dateOn = day + "/" + month + "/" + year;
                        dateStart.setText(dateOn);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        };
        View.OnClickListener timeListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeStart.setText(hourOfDay + ":" + minute);
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        };
        View.OnClickListener dateListenerEnd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String dateOn = day + "/" + month + "/" + year;
                        dateEnd.setText(dateOn);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        };
        View.OnClickListener timeListenerEnd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEnd.setText(hourOfDay + ":" + minute);
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        };
        View.OnClickListener createPrivate = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // serverConnection

                StringRequest stringRequest =  new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String msg = jsonObject.getString("msg");

                            if(success){
                                String code = jsonObject.getString("code");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                resetFields();
                                codeEvent.setText(code);
                                code_overlay.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProfileMainClass.getVerticalViewPager().setCurrentItem(0);
                                        code_overlay.setVisibility(View.INVISIBLE);
                                    }
                                },7000);

                            }
                            else{
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Json Err", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getContext(), "Eroare de procesare! Va rugam sa incercati din nou!", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String imageData = imgToString(bitmap);
                        params.put("file", imageData);
                        params.put("numeEvent",title.getText().toString().trim());
                        params.put("descriere",descriere.getText().toString().trim());
                        params.put("categorie",categorie.getText().toString().trim());
                        params.put("imbracaminte",tinuta.getText().toString().trim());
                        params.put("dataStart",dateStart.getText().toString().trim());
                        params.put("oraStart",timeStart.getText().toString().trim());
                        params.put("dataEnd",dateEnd.getText().toString().trim());
                        params.put("oraEnd",timeEnd.getText().toString().trim());
                        params.put("longitudine","0.1");
                        params.put("latitudine","0.1");
                        params.put("IDorganizator", String.valueOf(MainScreen.getUserID()));


                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                createDialog();
            }
        };
        View.OnClickListener pickImage =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,PERMISSION_CODE);
                }
                else{
                    pickImageFromGalley();
                }
            }
        };


        dateStart.setFocusable(false);
        dateStart.setOnClickListener(dateListenerStart);


        timeStart.setFocusable(false);
        timeStart.setOnClickListener(timeListenerStart);

        dateEnd.setFocusable(false);
        dateEnd.setOnClickListener(dateListenerEnd);

        timeEnd.setFocusable(false);
        timeEnd.setOnClickListener(timeListenerEnd);

        createEvent.setOnClickListener(createPrivate);

        previz.setOnClickListener(pickImage);


        return view;
    }

    private void pickImageFromGalley() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMG_PICK_CODE){
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                previz.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickImageFromGalley();
        }
    }

    private void init(){
        dateStart = view.findViewById(R.id.dateStartEventPrivate);
        timeStart = view.findViewById(R.id.timeStartEventPrivate);
        dateEnd = view.findViewById(R.id.dateEndEventPrivate);
        timeEnd = view.findViewById(R.id.timeEndDurationEventPrivate);
        createEvent = view.findViewById(R.id.createEventPrivate);
        previz = view.findViewById(R.id.previsualisationEventPrivate);
        title = view.findViewById(R.id.titluEventPrivat);
        categorie = view.findViewById(R.id.categorieEventPrivat);
        descriere = view.findViewById(R.id.descriereEventPrivat);
        tinuta = view.findViewById(R.id.tinutaEventPrivat);
        codeEvent = view.findViewById(R.id.eventCode);
        code_overlay = view.findViewById(R.id.codeConf);
        code_overlay.setVisibility(View.INVISIBLE);

        loading = new ProgressDialog(getContext());
    }

    private void createDialog(){
        loading.setCancelable(false);
        loading.setTitle("Creating event");
        loading.setMessage("We are creating the event. Please wait...");
        loading.show();
    }

    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }

    private void resetFields(){
        title.setText("");
        descriere.setText("");
        categorie.setText("");
        tinuta.setText("");
        dateStart.setText("");
        dateEnd.setText("");
        timeStart.setText("");
        timeEnd.setText("");
        previz.setImageResource(R.drawable.nopic);
    }
}
