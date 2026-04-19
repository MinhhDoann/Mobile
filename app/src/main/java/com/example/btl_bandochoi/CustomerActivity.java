package com.example.btl_bandochoi;

import android.app.Dialog;
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
    TextView btnAdd;

    CustomerDAO dao;
    List<Customer> list;
    CustomerAdapter adapter;
    List<Customer> originalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_customer);

        recyclerCustomers = findViewById(R.id.recyclerCustomers);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dao = new CustomerDAO(this);

        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        btnAdd.setOnClickListener(v -> {
            adapter.showAddDialog();
        });

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
    }

    private void loadData() {
        list = dao.getAll();
        originalList = new ArrayList<>(list);

        adapter = new CustomerAdapter(this, list);
        recyclerCustomers.setAdapter(adapter);
    }

    private void filter(String text) {
        List<Customer> filtered = new ArrayList<>();

        for (Customer c : originalList) {
            if (c.getName().toLowerCase().contains(text.toLowerCase())
                    || c.getPhone().contains(text)) {
                filtered.add(c);
            }
        }

        adapter.updateList(filtered);
    }
}