package com.example.btl_bandochoi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.data.InvoiceDAO;
import com.example.btl_bandochoi.model.Invoice;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtOrderCode, txtCustomerInfo, txtAddress, txtOrderDate;
    private TextView txtSubtotal, txtShippingFee, txtTotalAmount;
    private RecyclerView recyclerOrderItems;

    private InvoiceDAO invoiceDAO;
    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);   // ← Quan trọng: Dùng layout chi tiết

        invoiceDAO = new InvoiceDAO(this);

        anhXa();
        setEvent();
        loadOrderDetail();
    }

    private void anhXa() {
        btnBack = findViewById(R.id.btnBack);
        txtOrderCode = findViewById(R.id.txtOrderCode);
        txtCustomerInfo = findViewById(R.id.txtCustomerInfo);
        txtAddress = findViewById(R.id.txtAddress);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtShippingFee = findViewById(R.id.txtShippingFee);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadOrderDetail() {
        int invoiceId = getIntent().getIntExtra("invoice_id", -1);

        if (invoiceId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin hóa đơn
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);

        if (invoice == null) {
            Toast.makeText(this, "Đơn hàng không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin đơn hàng
        txtOrderCode.setText("Mã đơn hàng: " + invoice.getInvoiceCode());
        txtOrderDate.setText("Ngày đặt hàng: " + invoice.getDate());
        txtTotalAmount.setText(String.format("%,.0f ₫", invoice.getTotal()));

        // Phí vận chuyển giả định (có thể thay đổi sau)
        double shippingFee = 30000;
        double subtotal = invoice.getTotal() - shippingFee;

        txtSubtotal.setText(String.format("%,.0f ₫", subtotal));
        txtShippingFee.setText(String.format("%,.0f ₫", shippingFee));

        // Thông tin khách hàng
        txtCustomerInfo.setText("Khách hàng: " + invoice.getCustomerName());
        txtAddress.setText("Địa chỉ: " + (invoice.getCustomerAddress() != null
                ? invoice.getCustomerAddress()
                : "Chưa cập nhật"));

        // Hiển thị danh sách sản phẩm trong đơn
        loadOrderItems(invoiceId);
    }

    private void loadOrderItems(int invoiceId) {
        List<InvoiceDetail> itemList = invoiceDAO.getInvoiceDetails(invoiceId);

        if (itemList.isEmpty()) {
            // Có thể hiển thị thông báo không có sản phẩm
            return;
        }

        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
        orderItemAdapter = new OrderItemAdapter(this, itemList);
        recyclerOrderItems.setAdapter(orderItemAdapter);
    }
}