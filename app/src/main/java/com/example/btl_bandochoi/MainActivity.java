package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.btl_bandochoi.database.DatabaseHelper;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);

        btnsp = findViewById(R.id.btnsp);

        btnsp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
            startActivity(intent);
        });
    }
}