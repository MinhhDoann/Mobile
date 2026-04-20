package com.example.btl_bandochoi;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.adapter.CategoryAdapter;
import com.example.btl_bandochoi.data.CategoryDAO;
import com.example.btl_bandochoi.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ListView listView;
    CategoryDAO dao;
    CategoryAdapter adapter;

    EditText edtSearch;
    ImageView btnSearch;

    List<Category> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = findViewById(R.id.listViewCategory);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);

        dao = new CategoryDAO(this);

        loadData();

        findViewById(R.id.btnAddCategory).setOnClickListener(v -> showDialog(null));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.GONE) {
                edtSearch.setVisibility(View.VISIBLE);
            } else {
                edtSearch.setVisibility(View.GONE);
                edtSearch.setText("");
                loadData();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadData() {
        fullList = dao.getAll();

        adapter = new CategoryAdapter(this, fullList, new CategoryAdapter.OnAction() {
            @Override
            public void onEdit(Category c) {
                showDialog(c);
            }

            @Override
            public void onDelete(Category c) {
                dao.delete(c.getId());
                Toast.makeText(CategoryActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        listView.setAdapter(adapter);
    }

    private void filter(String keyword) {
        List<Category> filtered = new ArrayList<>();

        for (Category c : fullList) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(c);
            }
        }

        adapter = new CategoryAdapter(this, filtered, new CategoryAdapter.OnAction() {
            @Override
            public void onEdit(Category c) {
                showDialog(c);
            }

            @Override
            public void onDelete(Category c) {
                dao.delete(c.getId());
                Toast.makeText(CategoryActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        listView.setAdapter(adapter);
    }

    private void showDialog(Category c) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_category);

        EditText edtName = dialog.findViewById(R.id.edtName);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        if (c != null) {
            edtName.setText(c.getName());
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            if (c == null) {
                dao.insert(name);
            } else {
                dao.update(c.getId(), name);
            }

            dialog.dismiss();
            loadData();
        });

        dialog.show();

        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }
}