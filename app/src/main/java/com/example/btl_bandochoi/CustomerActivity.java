package com.example.btl_bandochoi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.adapter.CustomerAdapter;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerCustomers;
    EditText etSearch;
    ImageView btnSearch, btnBack;
    TextView btnAdd;

    CustomerDAO dao;
    List<Customer> list;
    CustomerAdapter adapter;
    List<Customer> originalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerCustomers = findViewById(R.id.recyclerCustomers);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);

        dao = new CustomerDAO(this);
        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        btnAdd.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.showAddDialog();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            if (etSearch.getVisibility() == View.GONE) {
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
            } else {
                etSearch.setVisibility(View.GONE);
                etSearch.setText("");
                loadData(); // Làm mới dữ liệu khi đóng tìm kiếm
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadData() {
        list = dao.getAll();
        originalList = new ArrayList<>(list);
        adapter = new CustomerAdapter(this, list);
        recyclerCustomers.setAdapter(adapter);
    }

    private void filter(String text) {
        if (originalList == null) return;
        
        List<Customer> filtered = new ArrayList<>();
        String query = text.toLowerCase().trim();
        
        for (Customer c : originalList) {
            // Kiểm tra null an toàn trước khi so sánh để tránh văng app
            String name = (c.getName() != null) ? c.getName().toLowerCase() : "";
            String phone = (c.getPhone() != null) ? c.getPhone() : "";
            
            if (name.contains(query) || phone.contains(query)) {
                filtered.add(c);
            }
        }

        if (adapter != null) {
            adapter.updateList(filtered);
        }
    }
}