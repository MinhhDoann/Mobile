package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> list;
    private OnItemClick listener;
    private Context context;

    public interface OnItemClick {
        void onEdit(Product p);
        void onDelete(Product p);
    }

    public ProductAdapter(Context context, List<Product> list, OnItemClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        TextView txtDescription, txtAge, txtStatus;
        ImageView imgProduct;
        TextView btnEdit, btnDelete;
        View layoutExtra;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txtName);
            tvPrice = itemView.findViewById(R.id.txtPrice);
            tvQuantity = itemView.findViewById(R.id.txtQuantity);

            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtStatus = itemView.findViewById(R.id.txtStatus);

            layoutExtra = itemView.findViewById(R.id.layoutExtra);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product p = list.get(position);

        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("%,.0f đ", p.getPrice()));
        holder.tvQuantity.setText("SL: " + p.getQuantity());

        holder.txtDescription.setText("Mô tả: " + p.getDescription());
        holder.txtAge.setText("Độ tuổi: " + p.getAgeFrom() + " - " + p.getAgeTo());
        holder.txtStatus.setText("Trạng thái: " + p.getStatus());

        int resId = context.getResources().getIdentifier(
                p.getImage(), "drawable", context.getPackageName()
        );
        holder.imgProduct.setImageResource(resId != 0 ? resId : R.drawable.car);

        holder.layoutExtra.setVisibility(p.isExpanded() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            p.setExpanded(!p.isExpanded());
            notifyItemChanged(position);
        });

        SharedPreferences prefs = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLogin", false);

        holder.btnEdit.setOnClickListener(v -> {
            if (!isLoggedIn) {
                Toast.makeText(context, "Cần đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) listener.onEdit(p);
        });

        holder.btnDelete.setOnClickListener(v -> {

            if (!isLoggedIn) {
                Toast.makeText(context, "Cần đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn chắc chắn muốn xóa \"" + p.getName() + "\" không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        if (listener != null) listener.onDelete(p);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}