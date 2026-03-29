package com.example.btl_bandochoi;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.adapter.ProductAdapter;
import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class SanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    ProductDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sanpham);

        recyclerView = findViewById(R.id.recyclerView);
        dao = new ProductDAO(this);

        loadData();

        findViewById(R.id.btnAdd).setOnClickListener(v -> showDialog(null));
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void loadData() {
        List<Product> list = dao.getAllProducts();

        adapter = new ProductAdapter(this, list, new ProductAdapter.OnItemClick() {
            @Override
            public void onEdit(Product p) {
                showDialog(p);
            }

            @Override
            public void onDelete(Product p) {
                dao.deleteProduct(p.getId());
                Toast.makeText(SanPhamActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(Product product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product);

        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Spinner spinnerImage = dialog.findViewById(R.id.spinnerImage);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        String[] imageNames = {"car", "doll", "robot", "lego", "go"};
        ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, imageNames);
        imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImage.setAdapter(imageAdapter);

        if (product != null) {
            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtQuantity.setText(String.valueOf(product.getQuantity()));

            int position = imageAdapter.getPosition(product.getImage());
            if (position >= 0) {
                spinnerImage.setSelection(position);
            }
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String qtyStr = edtQuantity.getText().toString().trim();
            String selectedImage = spinnerImage.getSelectedItem().toString();

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            int quantity;
            try {
                price = Double.parseDouble(priceStr);
                quantity = Integer.parseInt(qtyStr);
            } catch (Exception e) {
                Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (product == null) {
                dao.insertProduct(name, price, quantity, selectedImage);
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                dao.updateProduct(product.getId(), name, price, quantity, selectedImage);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadData();
        });

        dialog.show();

    }
}