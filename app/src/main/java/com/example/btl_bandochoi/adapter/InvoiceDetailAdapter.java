package com.example.btl_bandochoi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.model.InvoiceDetail;
import java.text.DecimalFormat;
import java.util.List;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder> {

    private final List<InvoiceDetail> details;
    private final DecimalFormat df = new DecimalFormat("#,### ₫");
    private final OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(InvoiceDetail detail);
    }

    public InvoiceDetailAdapter(List<InvoiceDetail> details, OnDeleteClickListener deleteClickListener) {
        this.details = details;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đổi từ item_order_detail sang item_invoice_detail
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceDetail detail = details.get(position);
        holder.txtProductName.setText(detail.getProductName());
        holder.txtQuantityPrice.setText(detail.getQuantity() + " x " + df.format(detail.getPrice()));
        holder.txtSubtotal.setText(df.format(detail.getSubTotal()));
        
        holder.btnDeleteDetail.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return details != null ? details.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtQuantityPrice, txtSubtotal;
        ImageView btnDeleteDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtQuantityPrice = itemView.findViewById(R.id.txtQuantityPrice);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotal);
            btnDeleteDetail = itemView.findViewById(R.id.btnDeleteDetail);
        }
    }
}
