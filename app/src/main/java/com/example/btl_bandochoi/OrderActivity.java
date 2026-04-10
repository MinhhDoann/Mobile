package com.example.yourapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private OrderAdapter orderAdapter;
    private ImageView btnBack, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        anhXa();
        setupRecyclerView();
        setEvent();
    }

    private void anhXa() {
        recyclerOrders = findViewById(R.id.recyclerOrders);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void setupRecyclerView() {
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        // orderAdapter = new OrderAdapter(this, orderList);
        // recyclerOrders.setAdapter(orderAdapter);
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());

        // Click vào một đơn hàng → xem chi tiết
        // orderAdapter.setOnItemClickListener(order -> {
        //     Intent intent = new Intent(this, OrderDetailActivity.class);
        //     intent.putExtra("order_id", order.getId());
        //     startActivity(intent);
        // });
    }
}