package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.data.TransactionHistoryDAO;
import com.example.btl_bandochoi.model.Customer;
import com.example.btl_bandochoi.model.TransactionHistory;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private final Context context;
    private List<Customer> list;
    private final CustomerDAO dao;
    private final TransactionHistoryDAO historyDAO;
    private int expandedPosition = -1;

    public CustomerAdapter(Context context, List<Customer> list) {
        this.context = context;
        this.list = list;
        this.dao = new CustomerDAO(context);
        this.historyDAO = new TransactionHistoryDAO(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, textGender, txtPhone, txtAddress, txtGmail, txtTotalSpent;
        TextView btnEdit, btnDelete;
        LinearLayout layoutExtra;
        RecyclerView recyclerInvoices;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            textGender = v.findViewById(R.id.txtGender);
            txtPhone = v.findViewById(R.id.txtPhone);
            txtGmail = v.findViewById(R.id.txtGmail);
            txtAddress = v.findViewById(R.id.txtAddress);
            txtTotalSpent = v.findViewById(R.id.txtTotalSpent);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            layoutExtra = v.findViewById(R.id.layoutExtra);
            recyclerInvoices = v.findViewById(R.id.recyclerCustomerInvoices);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false));
    }

    // Hàm kiểm tra trạng thái đăng nhập
    private boolean checkLogin() {
        SharedPreferences prefs = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLogin", false);
        if (!isLoggedIn) {
            Toast.makeText(context, "Bạn cần đăng nhập để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
        }
        return isLoggedIn;
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Customer c = list.get(position);
        h.txtName.setText(c.getName());
        h.txtPhone.setText(c.getPhone());
        h.txtAddress.setText(c.getAddress());
        h.txtTotalSpent.setText(String.format("%,.0fđ", c.getTotalSpent()));

        String genderText = "Khác";
        if ("nam".equals(c.getGender())) genderText = "Nam";
        else if ("nữ".equals(c.getGender())) genderText = "Nữ";
        h.textGender.setText(genderText);

        if (c.getEmail() == null || c.getEmail().isEmpty()) {
            h.txtGmail.setVisibility(View.GONE);
        } else {
            h.txtGmail.setText(c.getEmail());
            h.txtGmail.setVisibility(View.VISIBLE);
        }

        final boolean isExpanded = position == expandedPosition;
        if (h.layoutExtra != null) {
            h.layoutExtra.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            if (isExpanded) {
                List<TransactionHistory> histories = historyDAO.getHistoryByCustomerId(c.getId());
                CustomerInvoiceAdapter adapter = new CustomerInvoiceAdapter(histories);
                h.recyclerInvoices.setLayoutManager(new LinearLayoutManager(context));
                h.recyclerInvoices.setAdapter(adapter);
            }
        }

        h.itemView.setOnClickListener(v -> {
            int previousExpanded = expandedPosition;
            expandedPosition = isExpanded ? -1 : position;
            notifyItemChanged(previousExpanded);
            notifyItemChanged(expandedPosition);
        });

        // Chặn nút Sửa nếu chưa đăng nhập
        h.btnEdit.setOnClickListener(v -> {
            if (checkLogin()) {
                showDialog(c);
            }
        });

        // Chặn nút Xóa nếu chưa đăng nhập
        h.btnDelete.setOnClickListener(v -> {
            if (checkLogin()) {
                new AlertDialog.Builder(context).setTitle("Xóa khách hàng?").setMessage("Bạn chắc chắn muốn xóa không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dao.delete(c.getId());
                            reloadData();
                        }).setNegativeButton("Hủy", null).show();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    private void reloadData() {
        list.clear();
        list.addAll(dao.getAll());
        expandedPosition = -1;
        notifyDataSetChanged();
    }

    public void updateList(List<Customer> newList) {
        this.list = newList;
        expandedPosition = -1;
        notifyDataSetChanged();
    }

    private void showDialog(Customer c) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customer);

        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        EditText edtAddress = dialog.findViewById(R.id.edtAddress);
        Spinner spGender = dialog.findViewById(R.id.spGender);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        if (btnClose != null) btnClose.setOnClickListener(v -> dialog.dismiss());

        String[] genders = {"nam", "nữ", "other"};
        spGender.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, genders));

        if (c != null) {
            edtName.setText(c.getName());
            edtPhone.setText(c.getPhone());
            edtEmail.setText(c.getEmail());
            edtAddress.setText(c.getAddress());
            for (int i = 0; i < genders.length; i++) {
                if (genders[i].equals(c.getGender())) spGender.setSelection(i);
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đủ tên và SĐT", Toast.LENGTH_SHORT).show();
                return;
            }

            if (c == null) {
                if (dao.isPhoneExists(phone)) { edtPhone.setError("SĐT đã tồn tại"); return; }
                if (!email.isEmpty() && dao.isEmailExists(email)) { edtEmail.setError("Email đã tồn tại"); return; }
            } else {
                if (!phone.equals(c.getPhone()) && dao.isPhoneExists(phone)) { edtPhone.setError("SĐT đã tồn tại"); return; }
                if (!email.isEmpty() && !email.equals(c.getEmail()) && dao.isEmailExists(email)) { edtEmail.setError("Email đã tồn tại"); return; }
            }

            Customer customer = (c == null) ? new Customer() : c;
            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(edtAddress.getText().toString());
            customer.setGender(spGender.getSelectedItem().toString());
            customer.setStatus("active");

            if (c == null) dao.insert(customer);
            else dao.update(customer);

            reloadData();
            dialog.dismiss();
            Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    public void showAddDialog() {
        if (checkLogin()) {
            showDialog(null);
        }
    }
}