package com.example.btl_bandochoi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.adapter.InvoiceDetailAdapter;
import com.example.btl_bandochoi.data.InvoiceDAO;
import com.example.btl_bandochoi.data.InvoiceDetailDAO;
import com.example.btl_bandochoi.model.Invoice;
import com.example.btl_bandochoi.model.InvoiceDetail;
import java.text.DecimalFormat;
import java.util.List;

public class InvoiceDetailActivity extends AppCompatActivity {

    private TextView txtOrderCode, txtCustomerInfo, txtAddress, txtOrderDate;
    private TextView txtSubtotal, txtShippingFee, txtTotalAmount;
    private LinearLayout layoutOrderItems;
    private RecyclerView recyclerOrderItems;
    private ImageView btnBack;
    
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO detailDAO;
    private int invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoicedetail);

        invoiceDAO = new InvoiceDAO(this);
        detailDAO = new InvoiceDetailDAO(this);
        
        invoiceId = getIntent().getIntExtra("invoice_id", -1);

        anhXa();
        loadInvoiceData();
        setEvent();
    }

    private void anhXa() {
        txtOrderCode = findViewById(R.id.txtOrderCode);
        txtCustomerInfo = findViewById(R.id.txtCustomerInfo);
        txtAddress = findViewById(R.id.txtAddress);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtShippingFee = findViewById(R.id.txtShippingFee);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        layoutOrderItems = findViewById(R.id.layoutOrderItems);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        btnBack = findViewById(R.id.btnBack);

        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadInvoiceData() {
        if (invoiceId == -1) return;

        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        if (invoice != null) {
            DecimalFormat df = new DecimalFormat("#,### ₫");
            
            txtOrderCode.setText(invoice.getInvoiceCode());
            txtCustomerInfo.setText("Khách hàng: " + invoice.getCustomerName());
            txtAddress.setText("Địa chỉ: " + (invoice.getCustomerAddress() != null ? invoice.getCustomerAddress() : "Chưa cập nhật"));
            txtOrderDate.setText("Ngày đặt hàng: " + invoice.getDate());
            
            txtSubtotal.setText(df.format(invoice.getTotal()));
            txtShippingFee.setText("0 ₫");
            txtTotalAmount.setText(df.format(invoice.getTotal()));

            // Load danh sách sản phẩm chi tiết
            List<InvoiceDetail> details = detailDAO.getDetailsByInvoiceId(invoiceId);
            InvoiceDetailAdapter adapter = new InvoiceDetailAdapter(details);
            recyclerOrderItems.setAdapter(adapter);
            
            // Cập nhật số lượng sản phẩm lên tiêu đề
            TextView txtItemHeader = (TextView) ((LinearLayout) layoutOrderItems.getChildAt(0)).getChildAt(0);
            txtItemHeader.setText("Sản phẩm trong đơn hàng (" + details.size() + " sản phẩm)");
        }
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());

        layoutOrderItems.setOnClickListener(v -> {
            if (recyclerOrderItems.getVisibility() == View.GONE) {
                recyclerOrderItems.setVisibility(View.VISIBLE);
                // Bạn có thể đổi icon mũi tên ở đây nếu muốn
            } else {
                recyclerOrderItems.setVisibility(View.GONE);
            }
        });
    }
}
