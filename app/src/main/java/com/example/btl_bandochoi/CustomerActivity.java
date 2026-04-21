package com.example.btl_bandochoi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.adapter.CustomerAdapter;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerCustomers;
    private EditText etSearch;
    private ImageView btnSearch, btnBack;
    private TextView btnAdd;

    private CustomerDAO dao;
    private List<Customer> list;
    private CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        anhXa();
        dao = new CustomerDAO(this);
        loadData();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Tải lại dữ liệu mỗi khi quay lại màn hình này
    }

    private void anhXa() {
        recyclerCustomers = findViewById(R.id.recyclerCustomers);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        
        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        list = dao.getAll();
        adapter = new CustomerAdapter(this, list, new CustomerAdapter.OnCustomerClickListener() {
            @Override
            public void onEdit(Customer customer) {
                showCustomerDialog(customer);
            }

            @Override
            public void onDelete(Customer customer) {
                new AlertDialog.Builder(CustomerActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa khách hàng này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dao.delete(customer.getId());
                            loadData();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        recyclerCustomers.setAdapter(adapter);
    }

    private void setEvent() {
        btnAdd.setOnClickListener(v -> showCustomerDialog(null));
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            if (etSearch.getVisibility() == View.GONE) {
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
            } else {
                etSearch.setVisibility(View.GONE);
                etSearch.setText("");
                loadData();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filter(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String text) {
        List<Customer> filtered = new ArrayList<>();
        for (Customer c : list) {
            if (c.getName().toLowerCase().contains(text.toLowerCase()) || c.getPhone().contains(text)) {
                filtered.add(c);
            }
        }
        adapter = new CustomerAdapter(this, filtered, new CustomerAdapter.OnCustomerClickListener() {
            @Override public void onEdit(Customer customer) { showCustomerDialog(customer); }
            @Override public void onDelete(Customer customer) { dao.delete(customer.getId()); loadData(); }
        });
        recyclerCustomers.setAdapter(adapter);
    }

    private void showCustomerDialog(Customer customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_customer, null);
        builder.setView(view);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        Button btnSave = view.findViewById(R.id.btnSave);
        ImageView btnClose = view.findViewById(R.id.btnClose);

        if (customer != null) {
            edtName.setText(customer.getName());
            edtPhone.setText(customer.getPhone());
            edtAddress.setText(customer.getAddress());
        }

        AlertDialog dialog = builder.create();
        
        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String phone = edtPhone.getText().toString();
            String address = edtAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên và số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (customer == null) {
                Customer newCustomer = new Customer();
                newCustomer.setName(name);
                newCustomer.setPhone(phone);
                newCustomer.setAddress(address);
                dao.insert(newCustomer);
            } else {
                customer.setName(name);
                customer.setPhone(phone);
                customer.setAddress(address);
                dao.update(customer);
            }
            
            dialog.dismiss();
            loadData();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
