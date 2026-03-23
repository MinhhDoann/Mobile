package com.example.btl_bandochoi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import com.example.btl_bandochoi.adapter.ProductAdapter;
import com.example.btl_bandochoi.data.FakeData;

public class SanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sanpham);

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ProductAdapter(FakeData.getProducts());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
    }
}