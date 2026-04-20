package com.example.btl_bandochoi;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnsp, btnCategory, btnCustomer, layoutLowStock, btnhd;
    SharedPreferences prefs;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);

        btnsp = findViewById(R.id.btnsp);
        btnCategory = findViewById(R.id.btnCategory);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnhd = findViewById(R.id.btnhd);
        layoutLowStock = findViewById(R.id.layoutLowStock);

        Button btnLogin = findViewById(R.id.btnlogin);
        TextView txtName = findViewById(R.id.txtName);

        prefs = getSharedPreferences("USER", MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLogin", false);

        updateUI(btnLogin, txtName);

        btnLogin.setOnClickListener(v -> {
            if (!isLoggedIn) {
                showLoginDialog();
            } else {
                prefs.edit().putBoolean("isLogin", false).apply();
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });

        btnsp.setOnClickListener(v ->
                startActivity(new Intent(this, SanPhamActivity.class)));

        btnCategory.setOnClickListener(v ->
                startActivity(new Intent(this, CategoryActivity.class)));

        btnCustomer.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerActivity.class)));

        btnhd.setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceActivity.class)));

        loadLowStock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLowStock();
    }

    private void updateUI(Button btnLogin, TextView txtName) {
        if (isLoggedIn) {
            btnLogin.setText("Đăng xuất");
            txtName.setText("Xin chào admin");
        } else {
            btnLogin.setText("Đăng nhập");
            txtName.setText("Chế độ xem");
        }
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

    private void showLoginDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_login);

        EditText edtUser = dialog.findViewById(R.id.edtUser);
        EditText edtPass = dialog.findViewById(R.id.edtPass);
        Button btnLogin = dialog.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String user = edtUser.getText().toString();
            String pass = edtPass.getText().toString();

            if (user.equals("admin") && pass.equals("123")) {
                prefs.edit().putBoolean("isLogin", true).apply();
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                recreate();
            } else {
                Toast.makeText(this, "Sai tài khoản", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}