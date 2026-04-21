package com.example.btl_bandochoi.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.R;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private final Context context;
    private List<Customer> list;
    private final CustomerDAO dao;

    public CustomerAdapter(Context context, List<Customer> list) {
        this.context = context;
        this.list = list;
        this.dao = new CustomerDAO(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, textGender, txtPhone, txtAddress, txtGmail, txtTotalSpent;
        TextView btnEdit, btnDelete;

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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Customer c = list.get(position);

        h.txtName.setText(c.getName());
        String genderText;
        switch (c.getGender() != null ? c.getGender() : "") {
            case "nam":
                genderText = "Nam";
                break;
            case "nữ":
                genderText = "Nữ";
                break;
            default:
                genderText = "Khác";
        }
        h.textGender.setText(genderText);

        h.txtPhone.setText(c.getPhone());
        h.txtGmail.setText(c.getEmail());
        h.txtAddress.setText(c.getAddress());
        h.txtTotalSpent.setText(String.format("%,.0fđ", c.getTotalSpent()));

        h.btnEdit.setOnClickListener(v -> showDialog(c));

        h.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa khách hàng?")
                    .setMessage("Bạn có chắc muốn xóa khách hàng này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        dao.delete(c.getId());
                        reloadData();
                        Toast.makeText(context, "Đã xóa khách hàng", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void reloadData() {
        list.clear();
        list.addAll(dao.getAll());
        notifyDataSetChanged();
    }

    public void updateList(List<Customer> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    private void showDialog(Customer c) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customer);

        EditText edtName = dialog.findViewById(R.id.edtName);
        Spinner spGender = dialog.findViewById(R.id.spGender);

        String[] genderList = {"nam", "nữ", "other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                genderList
        );
        spGender.setAdapter(genderAdapter);
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        EditText edtAddress = dialog.findViewById(R.id.edtAddress);
        Spinner spStatus = dialog.findViewById(R.id.spStatus);
        TextView txtCreatedDate = dialog.findViewById(R.id.txtCreatedDate);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        String[] statusList = {"active", "inactive"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, statusList);
        spStatus.setAdapter(adapter);

        if (c != null) {
            edtName.setText(c.getName());
            edtPhone.setText(c.getPhone());
            edtEmail.setText(c.getEmail() != null ? c.getEmail() : "");
            edtAddress.setText(c.getAddress() != null ? c.getAddress() : "");
            int genderPos = 0;
            if ("nữ".equals(c.getGender())) genderPos = 1;
            else if ("other".equals(c.getGender())) genderPos = 2;
            spGender.setSelection(genderPos);

            if (c.getCreatedDate() != null) {
                txtCreatedDate.setText("Ngày tạo: " + c.getCreatedDate());
                txtCreatedDate.setVisibility(View.VISIBLE);
            }

            spStatus.setSelection("inactive".equals(c.getStatus()) ? 1 : 0);
        } else {
            txtCreatedDate.setVisibility(View.GONE);
            spStatus.setSelection(0);
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String gender = spGender.getSelectedItem().toString();

            if (gender.isEmpty()) {
                Toast.makeText(context, "Chọn giới tính!", Toast.LENGTH_SHORT).show();
                return;
            }
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            if (c == null) {
                if (!email.isEmpty() && dao.isEmailExists(email)) {
                    edtEmail.setError("Email đã tồn tại!");
                    return;
                }
            } else {
                if (!email.equals(c.getEmail())) {
                    if (!email.isEmpty() && dao.isEmailExists(email)) {
                        edtEmail.setError("Email đã tồn tại!");
                        return;
                    }
                }
            }
            if (!email.isEmpty() && dao.isEmailExists(email)) {
                edtEmail.setError("Email đã tồn tại!");
                return;
            }
            String address = edtAddress.getText().toString().trim();
            String status = spStatus.getSelectedItem().toString();

            if (name.isEmpty()) {
                edtName.setError("Tên khách hàng không được để trống");
                return;
            }

            if (phone.isEmpty()) {
                edtPhone.setError("Số điện thoại bắt buộc");
                return;
            }

            if (!phone.matches("^0\\d{9}$")) {
                edtPhone.setError("SĐT phải bắt đầu bằng 0 và đủ 10 số");
                return;
            }

            boolean success = false;

            if (c == null) {
                Customer newC = new Customer();
                newC.setName(name);
                newC.setGender(gender);
                newC.setPhone(phone);
                newC.setEmail(email.isEmpty() ? null : email);
                newC.setAddress(address.isEmpty() ? null : address);
                newC.setImage(null);
                newC.setStatus(status);

                long id = dao.insert(newC);

                if (id == -2) {
                    Toast.makeText(context, "SĐT đã tồn tại!", Toast.LENGTH_SHORT).show();
                } else if (id == -1) {
                    Toast.makeText(context, "Lỗi khi thêm khách hàng! (Xem Logcat)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Đã thêm thành công! ID=" + id, Toast.LENGTH_SHORT).show();
                    success = true;
                }
            }  else {
                if (!phone.equals(c.getPhone())) {
                    if (dao.insert(new Customer(){{
                        setPhone(phone);
                    }}) == -2) {
                        Toast.makeText(context, "SĐT đã tồn tại!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                c.setName(name);
                c.setGender(gender);
                c.setPhone(phone);
                c.setEmail(email);
                c.setAddress(address);
                c.setStatus(status);

                int rowsAffected = dao.update(c);
                if (rowsAffected > 0) {
                    Toast.makeText(context, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
                    success = true;
                } else {
                    Toast.makeText(context, "Không tìm thấy khách hàng để cập nhật", Toast.LENGTH_SHORT).show();
                }
            }

            if (success) {
                reloadData();
                dialog.dismiss();
            }
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    public void showAddDialog() {
        showDialog(null);
    }
}