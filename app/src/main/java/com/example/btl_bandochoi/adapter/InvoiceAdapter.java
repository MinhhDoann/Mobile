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

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private Context context;
    private List<Invoice> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Invoice invoice);
    }

    public InvoiceAdapter(Context context, List<Invoice> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<Invoice> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = list.get(position);

        holder.txtOrderCode.setText(invoice.getInvoiceCode());
        holder.txtCustomerName.setText(invoice.getCustomerName() != null ? invoice.getCustomerName() : "Khách lẻ");
        holder.txtDate.setText(invoice.getDate() != null ? invoice.getDate() : "Chưa có ngày");
        holder.txtTotal.setText(String.format("%,.0f ₫", invoice.getTotal()));

        // Số loại sản phẩm tạm thời (sẽ cải tiến sau khi có InvoiceDetail)
        holder.txtItemCount.setText("3 sản phẩm");   // TODO: tính thực từ InvoiceDetail

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(invoice);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderCode, txtCustomerName, txtDate, txtTotal, txtItemCount, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.txtOrderCode);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtItemCount = itemView.findViewById(R.id.txtItemCount);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}