package com.example.btl_bandochoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;

    public CategoryProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity;
        ImageView imgProduct;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtQuantity = v.findViewById(R.id.txtQuantity);
            imgProduct = v.findViewById(R.id.imgProduct);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);
        holder.txtName.setText(p.getName());
        holder.txtQuantity.setText("SL: " + p.getQuantity());

        String imgName = p.getImage();
        int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        if (resId != 0) {
            holder.imgProduct.setImageResource(resId);
        } else {
            holder.imgProduct.setImageResource(R.drawable.car);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}