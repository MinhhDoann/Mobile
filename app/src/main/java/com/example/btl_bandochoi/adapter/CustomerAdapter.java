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
import com.example.btl_bandochoi.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> list;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }

    public CustomerAdapter(Context context, List<Customer> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName, txtPhone, txtAddress, txtTotalSpent;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTotalSpent = itemView.findViewById(R.id.txtTotalSpent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = list.get(position);

        holder.txtName.setText(customer.getName());
        holder.txtPhone.setText(customer.getPhone());
        holder.txtAddress.setText(customer.getAddress() != null ? customer.getAddress() : "Chưa có địa chỉ");
        holder.txtTotalSpent.setText(String.format("%,.0fđ", customer.getTotalSpent()));

        // Click toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(customer);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}