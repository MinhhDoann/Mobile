package com.example.btl_bandochoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.Invoice;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Invoice> list;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Invoice invoice);
    }

    public OrderAdapter(Context context, List<Invoice> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderCode, txtCustomerName, txtDate, txtTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.txtOrderCode);      // Bạn cần thêm id này vào order_item.xml
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName); // cần thêm
            txtDate = itemView.findViewById(R.id.txtDate);                 // cần thêm
            txtTotal = itemView.findViewById(R.id.txtTotal);               // cần thêm
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);   // Bạn cần tạo file này
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = list.get(position);

        holder.txtOrderCode.setText(invoice.getInvoiceCode());
        holder.txtCustomerName.setText(invoice.getCustomerName());
        holder.txtDate.setText(invoice.getDate());
        holder.txtTotal.setText(String.format("%,.0fđ", invoice.getTotal()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(invoice);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}