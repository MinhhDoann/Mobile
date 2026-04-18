package com.example.btl_bandochoi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.btl_bandochoi.adapter.CustomerAdapter;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerCustomers;
    EditText etSearch;
    ImageView btnSearch, btnBack;
    TextView btnAddCustomer;

    CustomerDAO dao;
    List<Customer> list;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerCustomers = findViewById(R.id.recyclerCustomers);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);

        dao = new CustomerDAO(this);

        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            if (etSearch.getVisibility() == View.GONE) {
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
            } else {
                etSearch.setVisibility(View.GONE);
                etSearch.setText("");
                loadData();
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

        btnAddCustomer.setOnClickListener(v -> {
            Customer c = new Customer();
            c.setName("Khách mới");
            c.setPhone("09xxxxxxx");
            c.setAddress("Hà Nội");
            c.setStatus("active");

            dao.insert(c);
            loadData();
        });
    }

    private void loadData() {
        list = dao.getAll();

        adapter = new CustomerAdapter(this, list);
        recyclerCustomers.setAdapter(adapter);
    }

    private void filter(String text) {
        List<Customer> filtered = new ArrayList<>();

        for (Customer c : list) {
            if (c.getName().toLowerCase().contains(text.toLowerCase())
                    || c.getPhone().contains(text)) {
                filtered.add(c);
            }
        }

        adapter.updateList(filtered);
    }
}