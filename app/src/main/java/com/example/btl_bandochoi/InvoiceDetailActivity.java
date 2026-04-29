package com.example.btl_bandochoi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.adapter.InvoiceDetailAdapter;
import com.example.btl_bandochoi.data.InvoiceDAO;
import com.example.btl_bandochoi.data.InvoiceDetailDAO;
import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.model.Invoice;
import com.example.btl_bandochoi.model.InvoiceDetail;
import com.example.btl_bandochoi.model.Product;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailActivity extends AppCompatActivity {

    private TextView txtOrderCode, txtCustomerInfo, txtAddress, txtOrderDate;
    private TextView txtSubtotal, txtShippingFee, txtTotalAmount, txtProductTitle;
    private LinearLayout layoutOrderItems;
    private RecyclerView recyclerOrderItems;
    private ImageView btnBack, imgExpand;
    private Button btnAddProductItem;
    
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO detailDAO;
    private ProductDAO productDAO;
    private int invoiceId;
    private DecimalFormat df = new DecimalFormat("#,### ₫");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoicedetail);

        invoiceDAO = new InvoiceDAO(this);
        detailDAO = new InvoiceDetailDAO(this);
        productDAO = new ProductDAO(this);
        
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
        txtProductTitle = findViewById(R.id.txtProductTitle);
        layoutOrderItems = findViewById(R.id.layoutOrderItems);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        btnBack = findViewById(R.id.btnBack);
        imgExpand = findViewById(R.id.imgExpand);
        btnAddProductItem = findViewById(R.id.btnAddProductItem);

        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadInvoiceData() {
        if (invoiceId == -1) return;

        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        if (invoice != null) {
            txtOrderCode.setText(invoice.getInvoiceCode());
            txtCustomerInfo.setText("Khách hàng: " + invoice.getCustomerName());
            txtAddress.setText("Địa chỉ: " + (invoice.getCustomerAddress() != null ? invoice.getCustomerAddress() : "Chưa cập nhật"));
            txtOrderDate.setText("Ngày đặt hàng: " + invoice.getDate());
            
            List<InvoiceDetail> details = detailDAO.getDetailsByInvoiceId(invoiceId);
            
            double total = 0;
            for (InvoiceDetail d : details) {
                total += d.getSubTotal();
            }
            
            if (total != invoice.getTotal()) {
                invoiceDAO.updateTotal(invoiceId, total);
            }

            txtSubtotal.setText(df.format(total));
            txtShippingFee.setText("0 ₫");
            txtTotalAmount.setText(df.format(total));

            InvoiceDetailAdapter adapter = new InvoiceDetailAdapter(details, detail -> {
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có muốn xóa sản phẩm này khỏi đơn hàng? Số lượng sẽ được hoàn trả lại kho.")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Khi xóa chi tiết hóa đơn, hoàn trả lại số lượng vào kho
                            productDAO.updateStock(detail.getProductId(), detail.getQuantity());
                            detailDAO.delete(detail.getId());
                            loadInvoiceData();
                            Toast.makeText(this, "Đã xóa và hoàn trả số lượng vào kho", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
            recyclerOrderItems.setAdapter(adapter);
            txtProductTitle.setText("Sản phẩm trong đơn hàng (" + details.size() + ")");
        }
    }

    private void setEvent() {
        btnBack.setOnClickListener(v -> finish());

        imgExpand.setOnClickListener(v -> toggleProductList());
        txtProductTitle.setOnClickListener(v -> toggleProductList());

        btnAddProductItem.setOnClickListener(v -> showAddProductDialog());
    }

    private void toggleProductList() {
        if (recyclerOrderItems.getVisibility() == View.GONE) {
            recyclerOrderItems.setVisibility(View.VISIBLE);
            imgExpand.setRotation(180);
        } else {
            recyclerOrderItems.setVisibility(View.GONE);
            imgExpand.setRotation(0);
        }
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_product_to_invoice, null);
        builder.setView(view);

        Spinner spinnerProduct = view.findViewById(R.id.spinnerProduct);
        EditText edtQuantity = view.findViewById(R.id.edtQuantity);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Load danh sách sản phẩm từ DB
        List<Product> products = productDAO.getAllProducts();
        List<String> productNames = new ArrayList<>();
        for (Product p : products) {
            productNames.add(p.getName() + " (Kho: " + p.getQuantity() + ") - " + df.format(p.getPrice()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String quantityStr = edtQuantity.getText().toString();
            if (quantityStr.isEmpty() || products.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số lượng và chọn sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            int buyQuantity = Integer.parseInt(quantityStr);
            if (buyQuantity <= 0) {
                Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            Product selectedProduct = products.get(spinnerProduct.getSelectedItemPosition());

            // 1. Kiểm tra số lượng tồn kho
            if (buyQuantity > selectedProduct.getQuantity()) {
                Toast.makeText(this, "Số lượng tồn kho không đủ! (Còn: " + selectedProduct.getQuantity() + ")", Toast.LENGTH_LONG).show();
                return;
            }

            // Kiểm tra xem sản phẩm đã có trong đơn hàng chưa
            InvoiceDetail existingDetail = detailDAO.getDetailByInvoiceAndProduct(invoiceId, selectedProduct.getId());
            
            if (existingDetail != null) {
                // Nếu đã có, cộng dồn số lượng (đã check ở trên nhưng check lại cho chắc nếu cần)
                detailDAO.updateQuantity(existingDetail.getId(), existingDetail.getQuantity() + buyQuantity);
            } else {
                // Nếu chưa có, thêm mới
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceId(invoiceId);
                detail.setProductId(selectedProduct.getId());
                detail.setQuantity(buyQuantity);
                detail.setPrice(selectedProduct.getPrice());
                detail.setDiscount(0);
                detailDAO.insert(detail);
            }

            // 2. Trừ số lượng trong kho sản phẩm
            productDAO.updateStock(selectedProduct.getId(), -buyQuantity);

            Toast.makeText(this, "Đã thêm sản phẩm và cập nhật kho", Toast.LENGTH_SHORT).show();
            loadInvoiceData(); // Tải lại giao diện và cập nhật tổng tiền
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
