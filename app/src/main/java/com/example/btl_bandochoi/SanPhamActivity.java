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

        if (recyclerView == null) {
            Toast.makeText(this, "Không tìm thấy RecyclerView trong layout!", Toast.LENGTH_LONG).show();
            return;
        }

        dao = new ProductDAO(this);
        initSampleData();
        loadData();
        findViewById(R.id.btnAdd).setOnClickListener(v -> showDialog(null));

        ImageView back = findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(v -> finish());
        }

        recyclerView.setNestedScrollingEnabled(false);
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

                dao.insertProduct("Robot", "Robot thông minh", 50000, 5,
                        5, 15, "active", "robot", list.get(1).getId());
            }
        }
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
                Toast.makeText(SanPhamActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
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
        EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        EditText edtAgeFrom = dialog.findViewById(R.id.edtAgeFrom);
        EditText edtAgeTo = dialog.findViewById(R.id.edtAgeTo);

        Spinner spinnerImage = dialog.findViewById(R.id.spinnerImage);
        Spinner spCategory = dialog.findViewById(R.id.spCategory);
        Spinner spStatus = dialog.findViewById(R.id.spStatus);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);


        String[] imageNames = {"car", "doll", "robot", "lego", "go"};
        ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, imageNames);
        spinnerImage.setAdapter(imageAdapter);

        CategoryDAO categoryDAO = new CategoryDAO(this);
        List<Category> list = categoryDAO.getAll();

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        spCategory.setAdapter(categoryAdapter);


        String[] statusList = {"active", "inactive"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusList);
        spStatus.setAdapter(statusAdapter);

        if (product != null) {
            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtQuantity.setText(String.valueOf(product.getQuantity()));
            edtDescription.setText(product.getDescription());
            edtAgeFrom.setText(String.valueOf(product.getAgeFrom()));
            edtAgeTo.setText(String.valueOf(product.getAgeTo()));

            spinnerImage.setSelection(imageAdapter.getPosition(product.getImage()));
            spStatus.setSelection(product.getStatus().equals("active") ? 0 : 1);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == product.getCategoryId()) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());


        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String qtyStr = edtQuantity.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String ageFromStr = edtAgeFrom.getText().toString().trim();
            String ageToStr = edtAgeTo.getText().toString().trim();

            String image = spinnerImage.getSelectedItem().toString();
            String status = spStatus.getSelectedItem().toString();

            Category selectedCategory = (Category) spCategory.getSelectedItem();
            if (selectedCategory == null) {
                Toast.makeText(this, "Chưa có danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(qtyStr);
                int ageFrom = ageFromStr.isEmpty() ? 0 : Integer.parseInt(ageFromStr);
                int ageTo = ageToStr.isEmpty() ? 0 : Integer.parseInt(ageToStr);

                if (product == null) {
                    dao.insertProduct(name, description, price, quantity,
                            ageFrom, ageTo, status, image, selectedCategory.getId());
                    Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
                } else {
                    dao.updateProduct(product.getId(), name, description, price, quantity,
                            ageFrom, ageTo, status, image, selectedCategory.getId());
                    Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
                loadData();

            } catch (Exception e) {
                Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}