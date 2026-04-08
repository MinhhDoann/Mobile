package com.example.btl_bandochoi;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_bandochoi.adapter.CategoryAdapter;
import com.example.btl_bandochoi.data.CategoryDAO;
import com.example.btl_bandochoi.model.Category;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ListView listView;
    CategoryDAO dao;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = findViewById(R.id.listViewCategory);
        dao = new CategoryDAO(this);

        loadData();

        findViewById(R.id.btnAddCategory).setOnClickListener(v -> showDialog(null));
    }

    private void loadData() {
        List<Category> list = dao.getAll();

        adapter = new CategoryAdapter(this, list, new CategoryAdapter.OnAction() {
            @Override
            public void onEdit(Category c) {
                showDialog(c);
            }

            @Override
            public void onDelete(Category c) {
                dao.delete(c.getId());
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
    }
}