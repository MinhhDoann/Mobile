package com.example.btl_bandochoi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp, btnCategory, layoutLowStock;
    LinearLayout btnCustomer;
    SharedPreferences prefs;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);

        btnsp = findViewById(R.id.btnsp);
        btnCategory = findViewById(R.id.btnCategory);
        layoutLowStock = findViewById(R.id.layoutLowStock);
        btnCustomer = findViewById(R.id.btnCustomer);

        btnsp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SanPhamActivity.class));
        });

        btnCategory.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
        });

        btnCustomer.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CustomerActivity.class));
        });
        loadLowStock();
        prefs = getSharedPreferences("USER", MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLogin", false);

        Button btnLogin = findViewById(R.id.btnlogin);

        if (isLoggedIn) {
            btnLogin.setText("Đăng xuất");
        } else {
            btnLogin.setText("Đăng nhập");
        }
        btnLogin.setOnClickListener(v -> {
            if (!isLoggedIn) {
                showLoginDialog();
            } else {
                prefs.edit().putBoolean("isLogin", false).apply();
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLowStock();
    }

    private void loadLowStock() {
        ProductDAO dao = new ProductDAO(this);
        List<Product> list = dao.getLowStockProducts(3);
        layoutLowStock.removeAllViews();

        if (list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Không có sản phẩm sắp hết");
            layoutLowStock.addView(tv);
            return;
        }

        for (Product p : list) {
            TextView tv = new TextView(this);

            tv.setText("⚠ " + p.getName() + " - còn " + p.getQuantity());
            tv.setTextSize(14);

            if (p.getQuantity() <= 3) {
                tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            layoutLowStock.addView(tv);
        }
    }
}