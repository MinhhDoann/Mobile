package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.data.ProductDAO;
import com.example.btl_bandochoi.model.Category;
import com.example.btl_bandochoi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> list;
    private OnAction listener;
    private ProductDAO productDAO;
    private int expandedPosition = -1;

    public interface OnAction {
        void onEdit(Category c);
        void onDelete(Category c);
    }

    public CategoryAdapter(Context context, List<Category> list, OnAction listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.productDAO = new ProductDAO(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, btnEdit, btnDelete;
        RecyclerView recyclerProducts;
        View layoutHeader;

        public ViewHolder(View v) {
            super(v);
            layoutHeader = v.findViewById(R.id.layoutHeader);
            txtName = v.findViewById(R.id.txtName);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            recyclerProducts = v.findViewById(R.id.recyclerProducts);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category c = list.get(position);
        holder.txtName.setText(c.getName());

        final boolean isExpanded = position == expandedPosition;
        holder.recyclerProducts.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (isExpanded) {
            List<Product> allProducts = productDAO.getAllProducts();
            List<Product> categoryProducts = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getCategoryId() == c.getId()) {
                    categoryProducts.add(p);
                }
            }

            if (!categoryProducts.isEmpty()) {
                CategoryProductAdapter productAdapter = new CategoryProductAdapter(context, categoryProducts);
                holder.recyclerProducts.setLayoutManager(new LinearLayoutManager(context));
                holder.recyclerProducts.setAdapter(productAdapter);
            }
        }

        holder.layoutHeader.setOnClickListener(v -> {
            int previousExpanded = expandedPosition;
            expandedPosition = isExpanded ? -1 : position;
            notifyItemChanged(previousExpanded);
            notifyItemChanged(expandedPosition);
        });

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(c));

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa")
                    .setMessage("Bạn có chắc muốn xóa danh mục \"" + c.getName() + "\"?")
                    .setPositiveButton("Có", (d, w) -> listener.onDelete(c))
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}