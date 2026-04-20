package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.*;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    List<Category> list;
    OnAction listener;

    public interface OnAction {
        void onEdit(Category c);
        void onDelete(Category c);
    }

    public CategoryAdapter(Context context, List<Category> list, OnAction listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int i) { return list.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        }

        Category c = list.get(i);

        TextView txtName = view.findViewById(R.id.txtName);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        txtName.setText(c.getName());

        btnEdit.setOnClickListener(v -> listener.onEdit(c));

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa")
                    .setMessage("Bạn có chắc muốn xóa?")
                    .setPositiveButton("Có", (d, w) -> listener.onDelete(c))
                    .setNegativeButton("Không", null)
                    .show();
        });

        return view;
    }
}