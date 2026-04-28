package com.example.btl_bandochoi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    Spinner spMonth, spYear;
    TextView txtTotalRevenue, txtTotalOrders, txtNewCustomers, txtSoldProducts;
    ImageView btnBack;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        dbHelper = new DatabaseHelper(this);

        spMonth = findViewById(R.id.spMonth);
        spYear = findViewById(R.id.spYear);
        txtTotalRevenue = findViewById(R.id.txtTotalRevenue);
        txtTotalOrders = findViewById(R.id.txtTotalOrders);
        txtNewCustomers = findViewById(R.id.txtNewCustomers);
        txtSoldProducts = findViewById(R.id.txtSoldProducts);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        setupSpinners();
    }

    private void setupSpinners() {
        // 1. Thiết lập Spinner Tháng (Tất cả, 1-12)
        List<String> months = new ArrayList<>();
        months.add("Tất cả");
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthAdapter);

        List<String> years = getYearsFromDB();
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        spMonth.setSelection(currentMonth); 

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStatistics();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        spMonth.setOnItemSelectedListener(listener);
        spYear.setOnItemSelectedListener(listener);
    }

    private List<String> getYearsFromDB() {
        List<String> years = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT DISTINCT strftime('%Y', date) as year FROM Invoice ORDER BY year DESC", null);
        
        while (cursor.moveToNext()) {
            years.add(cursor.getString(0));
        }
        cursor.close();

        if (years.isEmpty()) {
            years.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }
        return years;
    }

    private void updateStatistics() {
        String selectedMonth = spMonth.getSelectedItem().toString();
        String selectedYear = spYear.getSelectedItem().toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String datePattern;
        String[] args;

        if (selectedMonth.equals("Tất cả")) {
            datePattern = selectedYear + "%";
            args = new String[]{datePattern};
        } else {
            String monthFormatted = String.format("%02d", Integer.parseInt(selectedMonth));
            datePattern = selectedYear + "-" + monthFormatted + "%";
            args = new String[]{datePattern};
        }

        Cursor c1 = db.rawQuery("SELECT COUNT(*), SUM(total) FROM Invoice WHERE date LIKE ?", args);
        if (c1.moveToFirst()) {
            txtTotalOrders.setText(String.valueOf(c1.getInt(0)));
            double revenue = c1.getDouble(1);
            txtTotalRevenue.setText(String.format("%,.0fđ", revenue));
        }
        c1.close();

        Cursor c2 = db.rawQuery("SELECT COUNT(*) FROM Customer WHERE created_date LIKE ?", args);
        if (c2.moveToFirst()) {
            txtNewCustomers.setText(String.valueOf(c2.getInt(0)));
        }
        c2.close();

        Cursor c3 = db.rawQuery(
                "SELECT SUM(id.quantity) FROM InvoiceDetail id " +
                "JOIN Invoice i ON id.invoice_id = i.id " +
                "WHERE i.date LIKE ?", args);
        if (c3.moveToFirst()) {
            txtSoldProducts.setText(String.valueOf(c3.getInt(0)));
        }
        c3.close();
    }
}