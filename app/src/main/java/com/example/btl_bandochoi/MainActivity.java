package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp, btnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);

        btnsp = findViewById(R.id.btnsp);
        btnCategory = findViewById(R.id.btnCategory);

        btnsp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
            startActivity(intent);
        });

        btnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });
    }
}