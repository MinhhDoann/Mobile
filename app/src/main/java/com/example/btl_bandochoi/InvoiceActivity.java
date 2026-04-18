package com.example.btl_bandochoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.data.InvoiceDAO;
import com.example.btl_bandochoi.model.Invoice;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private ImageView btnBack, btnSearch;
    private InvoiceDAO invoiceDAO;
    private List<Invoice> invoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        invoiceDAO = new InvoiceDAO(this);

        anhXa();
        setupRecyclerView();
        setEvent();
        loadOrders();
    }

    private void anhXa() {
        recyclerOrders = findViewById(R.id.recyclerOrders);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void setupRecyclerView() {
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrders() {
        invoiceList = invoiceDAO.getAllInvoices();
        orderAdapter = new OrderAdapter(this, invoiceList, invoice -> {
            Intent intent = new Intent(InvoiceActivity.this, OrderDetailActivity.class);
            intent.putExtra("invoice_id", invoice.getId());
            startActivity(intent);
        });
        recyclerOrders.setAdapter(orderAdapter);
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnAddOrder).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng tạo đơn hàng mới sẽ được triển khai sau", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}