package com.example.btl_bandochoi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.TransactionHistory;
import java.text.DecimalFormat;
import java.util.List;

public class CustomerInvoiceAdapter extends RecyclerView.Adapter<CustomerInvoiceAdapter.ViewHolder> {

    private final List<TransactionHistory> historyList;
    private final DecimalFormat df = new DecimalFormat("#,### ₫");

    public CustomerInvoiceAdapter(List<TransactionHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vẫn sử dụng layout item_customer_invoice vì cấu trúc hiển thị không đổi
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionHistory history = historyList.get(position);
        
        holder.txtInvoiceCode.setText(history.getInvoiceCode());
        holder.txtDate.setText(history.getDate());
        holder.txtItemCount.setText(history.getItemCount() + " sp");
        holder.txtTotal.setText(df.format(history.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInvoiceCode, txtDate, txtItemCount, txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInvoiceCode = itemView.findViewById(R.id.txtInvoiceCode);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtItemCount = itemView.findViewById(R.id.txtItemCount);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
