package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnsp = findViewById(R.id.btnsp);

        btnsp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
            startActivity(intent);
        });
    }
}