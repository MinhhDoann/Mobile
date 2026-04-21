package com.example.btl_bandochoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.data.TransactionHistoryDAO;
import com.example.btl_bandochoi.model.Customer;
import com.example.btl_bandochoi.model.TransactionHistory;
import java.text.DecimalFormat;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> list;
    private OnCustomerClickListener listener;
    private Context context;
    private TransactionHistoryDAO historyDAO;
    private DecimalFormat df = new DecimalFormat("#,### ₫");

    public interface OnCustomerClickListener {
        void onEdit(Customer customer);
        void onDelete(Customer customer);
    }

    public CustomerAdapter(Context context, List<Customer> list, OnCustomerClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.historyDAO = new TransactionHistoryDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = list.get(position);

        holder.txtName.setText(customer.getName());
        holder.txtPhone.setText(customer.getPhone());
        holder.txtAddress.setText(customer.getAddress());
        holder.txtTotalSpent.setText(df.format(customer.getTotalSpent()));

        if (customer.isExpanded()) {
            holder.layoutExtra.setVisibility(View.VISIBLE);
            loadCustomerInvoices(holder.recyclerCustomerInvoices, customer.getId());
        } else {
            holder.layoutExtra.setVisibility(View.GONE);
        }

        holder.layoutMain.setOnClickListener(v -> {
            customer.setExpanded(!customer.isExpanded());
            notifyItemChanged(position);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(customer);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(customer);
        });
    }

    private void loadCustomerInvoices(RecyclerView recyclerView, int customerId) {
        // Sửa lỗi: Gọi đúng phương thức getHistoryByCustomerId thay vì getHistoryByProductId
        List<TransactionHistory> historyList = historyDAO.getHistoryByCustomerId(customerId);
        CustomerInvoiceAdapter adapter = new CustomerInvoiceAdapter(historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtAddress, txtTotalSpent;
        TextView btnEdit, btnDelete;
        LinearLayout layoutMain, layoutExtra;
        RecyclerView recyclerCustomerInvoices;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTotalSpent = itemView.findViewById(R.id.txtTotalSpent);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            layoutExtra = itemView.findViewById(R.id.layoutExtra);
            recyclerCustomerInvoices = itemView.findViewById(R.id.recyclerCustomerInvoices);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
