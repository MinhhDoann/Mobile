package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp, btnCategory, layoutLowStock;

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

        btnsp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SanPhamActivity.class));
        });

        btnCategory.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
        });

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