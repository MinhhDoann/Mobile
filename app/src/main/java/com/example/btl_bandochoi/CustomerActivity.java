package com.example.yourapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerCustomers;
    private CustomerAdapter customerAdapter;
    private ImageView btnBack, btnSearch;
    // private List<Customer> customerList;   // sau này sẽ dùng Room hoặc SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        anhXa();
        setupRecyclerView();
        setEvent();
    }

    private void anhXa() {
        recyclerCustomers = findViewById(R.id.recyclerCustomers);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void setupRecyclerView() {
        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));
        // customerAdapter = new CustomerAdapter(this, customerList);
        // recyclerCustomers.setAdapter(customerAdapter);

        // Hiện tạm empty state
        findViewById(R.id.layoutEmpty).setVisibility(View.VISIBLE);
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());

        // Click vào item khách hàng → xem chi tiết
        // customerAdapter.setOnItemClickListener(customer -> {
        //     Intent intent = new Intent(this, CustomerDetailActivity.class);
        //     intent.putExtra("customer_id", customer.getId());
        //     startActivity(intent);
        // });
    }
}