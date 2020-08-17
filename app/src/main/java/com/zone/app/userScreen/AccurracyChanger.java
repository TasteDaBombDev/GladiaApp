package com.zone.app.userScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.zone.app.R;

public class AccurracyChanger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accurracy_changer);

        Button done = findViewById(R.id.done);

        done.setEnabled(false);
        new Handler().postDelayed(() -> done.setEnabled(true), 30000);

        done.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}