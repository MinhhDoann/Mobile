package com.example.btl_bandochoi.adapter;

import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> list;
    private Context context;

    public CustomerAdapter(Context context, List<Customer> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtAddress, txtTotalSpent;
        ImageView imgAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTotalSpent = itemView.findViewById(R.id.txtTotalSpent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Customer c = list.get(position);

        holder.txtName.setText(c.getName());
        holder.txtPhone.setText(c.getPhone());
        holder.txtAddress.setText(c.getAddress());

        holder.txtTotalSpent.setText(String.format("%,.0fđ", c.getTotalSpent()));

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Chọn: " + c.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Customer> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}