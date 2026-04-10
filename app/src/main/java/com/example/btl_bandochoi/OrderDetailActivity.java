package com.example.btl_bandochoi;

public class OrderDetailActivity {
}
package com.example.yourapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);   // hoặc order_item.xml nếu bạn giữ tên đó

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        String orderId = getIntent().getStringExtra("order_id");
        // Load chi tiết đơn hàng từ database...
    }
}