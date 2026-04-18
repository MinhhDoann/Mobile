package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    Context context;
    List<Customer> list;
    CustomerDAO dao;

    public CustomerAdapter(Context context, List<Customer> list) {
        this.context = context;
        this.list = list;
        dao = new CustomerDAO(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtAddress, txtTotalSpent;
        TextView btnEdit, btnDelete;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtPhone = v.findViewById(R.id.txtPhone);
            txtAddress = v.findViewById(R.id.txtAddress);
            txtTotalSpent = v.findViewById(R.id.txtTotalSpent);

            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.customer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Customer c = list.get(position);

        h.txtName.setText(c.getName());
        h.txtPhone.setText(c.getPhone());
        h.txtAddress.setText(c.getAddress());
        h.txtTotalSpent.setText(String.format("%,.0fđ", c.getTotalSpent()));

        h.btnEdit.setOnClickListener(v -> showDialog(c, h.getAdapterPosition()));

        h.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa khách?")
                    .setMessage("Bạn có chắc muốn xóa?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        int pos = h.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            dao.delete(list.get(pos).getId());
                            list.remove(pos);
                            notifyItemRemoved(pos);
                            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Customer> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    private void showDialog(Customer c, int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customer);

        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        EditText edtAddress = dialog.findViewById(R.id.edtAddress);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (c != null) {
            edtName.setText(c.getName());
            edtPhone.setText(c.getPhone());
            edtAddress.setText(c.getAddress());
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty()) {
                edtName.setError("Không được để trống");
                return;
            }

            if (c == null) {
                Customer newC = new Customer();
                newC.setName(name);
                newC.setPhone(phone);
                newC.setAddress(address);
                newC.setStatus("active");

                long id = dao.insert(newC);
                newC.setId((int) id);

                list.add(newC);
                notifyItemInserted(list.size() - 1);

                Toast.makeText(context, "Đã thêm", Toast.LENGTH_SHORT).show();

            } else {
                c.setName(name);
                c.setPhone(phone);
                c.setAddress(address);

                dao.update(c);
                notifyItemChanged(position);

                Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    public void showAddDialog() {
        showDialog(null, -1);
    }
}