package com.example.btl_bandochoi;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.adapter.ProductAdapter;
import com.example.btl_bandochoi.data.CategoryDAO;
import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.model.Category;
import com.example.btl_bandochoi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    ProductDAO dao;

    TextView conHang, hetHang;

    private boolean isShowInStock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sanpham);

        recyclerView = findViewById(R.id.recyclerView);
        conHang = findViewById(R.id.conhang);
        hetHang = findViewById(R.id.hethang);

        dao = new ProductDAO(this);

        initSampleData();

        highlightButton(conHang, hetHang);
        loadData();

        conHang.setOnClickListener(v -> {
            isShowInStock = true;
            highlightButton(conHang, hetHang);
            loadData();
        });

        hetHang.setOnClickListener(v -> {
            isShowInStock = false;
            highlightButton(hetHang, conHang);
            loadData();
        });

        findViewById(R.id.btnAdd).setOnClickListener(v -> showDialog(null));

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSampleData() {
        CategoryDAO cdao = new CategoryDAO(this);

        if (cdao.getAll().size() == 0) {
            cdao.insert("Xe đồ chơi");
            cdao.insert("Robot");
            cdao.insert("Búp bê");
        }

        if (dao.getAllProducts().size() == 0) {
            List<Category> list = cdao.getAll();

            if (!list.isEmpty()) {
                dao.insertProduct("Xe ô tô", "Xe đẹp", 30000, 10,
                        3, 10, "active", "car", list.get(0).getId());

                dao.insertProduct("Robot", "Robot thông minh", 50000, 0,
                        5, 15, "active", "robot", list.get(1).getId()); // 🔥 test hết hàng
            }
        }
    }

    private void loadData() {
        List<Product> list = dao.getAllProducts();
        List<Product> filteredList = new ArrayList<>();

        for (Product p : list) {
            if (isShowInStock) {
                if (p.getQuantity() > 0) filteredList.add(p);
            } else {
                if (p.getQuantity() == 0) filteredList.add(p);
            }
        }

        adapter = new ProductAdapter(this, filteredList, new ProductAdapter.OnItemClick() {
            @Override
            public void onEdit(Product p) {
                showDialog(p);
            }

            @Override
            public void onDelete(Product p) {
                dao.deleteProduct(p.getId());
                Toast.makeText(SanPhamActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void highlightButton(TextView selected, TextView other) {
        selected.setBackgroundResource(R.drawable.btn_selected);
        selected.setTextColor(getResources().getColor(android.R.color.white));

        other.setBackgroundResource(R.drawable.sanpham);
        other.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void showDialog(Product product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product);

        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        EditText edtAgeFrom = dialog.findViewById(R.id.edtAgeFrom);
        EditText edtAgeTo = dialog.findViewById(R.id.edtAgeTo);

        Spinner spinnerImage = dialog.findViewById(R.id.spinnerImage);
        Spinner spCategory = dialog.findViewById(R.id.spCategory);
        Spinner spStatus = dialog.findViewById(R.id.spStatus);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        String[] imageNames = {"car", "doll", "robot", "lego", "go"};
        spinnerImage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, imageNames));

        CategoryDAO categoryDAO = new CategoryDAO(this);
        List<Category> list = categoryDAO.getAll();
        spCategory.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list));

        String[] statusList = {"active", "inactive"};
        spStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusList));

        if (product != null) {
            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtQuantity.setText(String.valueOf(product.getQuantity()));
            edtDescription.setText(product.getDescription());
            edtAgeFrom.setText(String.valueOf(product.getAgeFrom()));
            edtAgeTo.setText(String.valueOf(product.getAgeTo()));
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            try {
                String name = edtName.getText().toString();
                double price = Double.parseDouble(edtPrice.getText().toString());
                int quantity = Integer.parseInt(edtQuantity.getText().toString());

                String description = edtDescription.getText().toString();
                int ageFrom = edtAgeFrom.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAgeFrom.getText().toString());
                int ageTo = edtAgeTo.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAgeTo.getText().toString());

                String image = spinnerImage.getSelectedItem().toString();
                String status = spStatus.getSelectedItem().toString();

                Category category = (Category) spCategory.getSelectedItem();

                if (product == null) {
                    dao.insertProduct(name, description, price, quantity,
                            ageFrom, ageTo, status, image, category.getId());
                } else {
                    dao.updateProduct(product.getId(), name, description, price, quantity,
                            ageFrom, ageTo, status, image, category.getId());
                }

                dialog.dismiss();
                loadData();

            } catch (Exception e) {
                Toast.makeText(this, "Lỗi nhập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}