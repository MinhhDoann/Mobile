package com.example.btl_bandochoi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<Product> list;

    // ✅ PHẢI có constructor này
    public ProductAdapter(List<Product> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, info;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            name = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
            info = itemView.findViewById(R.id.txtInfo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Product p = list.get(position);

        h.name.setText(p.name);
        h.price.setText(p.price + "đ");
        h.info.setText("Tồn: " + p.stock + " | Đã bán: " + p.sold);

        h.img.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}