package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    // Khai báo các nút
    private LinearLayout btnsp;     // Sản phẩm
    private LinearLayout btnhd;     // Hóa đơn
    private LinearLayout btnkh;     // Khách hàng
    private LinearLayout btnkho;    // Kho
    private LinearLayout btntk;     // Thống kê
    private LinearLayout btnht;     // Hỗ trợ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Database (chỉ cần 1 lần)
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();   // Tạo database nếu chưa có

        // Ánh xạ các nút
        anhXa();

        // Gán sự kiện click cho các nút
        setEvents();
    }

    private void anhXa() {
        btnsp  = findViewById(R.id.btnsp);   // Sản phẩm
        btnhd  = findViewById(R.id.btnhd);   // Hóa đơn
        btnkh  = findViewById(R.id.btnkh);   // Khách hàng
        btnkho = findViewById(R.id.kho);     // Kho (ID trong XML là "kho")
        btntk  = findViewById(R.id.btntk);   // Thống kê
        btnht  = findViewById(R.id.btnht);   // Hỗ trợ
    }

    private void setEvents() {

        // === SẢN PHẨM ===
        btnsp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);
            startActivity(intent);
        });

        // === HÓA ĐƠN ===
        btnhd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
            startActivity(intent);
        });

        // === KHÁCH HÀNG ===
        btnkh.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
            startActivity(intent);
        });

        // === KHO ===
        btnkho.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KhoActivity.class);   // Bạn có thể đổi tên sau
            startActivity(intent);
        });

        // === THỐNG KÊ ===
        btntk.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });

        // === HỖ TRỢ ===
        btnht.setOnClickListener(v -> {
            // Tạm thời để trống hoặc mở Activity hỗ trợ sau
            // Intent intent = new Intent(MainActivity.this, HoTroActivity.class);
            // startActivity(intent);
        });
    }
}