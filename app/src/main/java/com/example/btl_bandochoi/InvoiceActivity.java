package com.example.btl_bandochoi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_bandochoi.adapter.InvoiceAdapter;
import com.example.btl_bandochoi.data.CustomerDAO;
import com.example.btl_bandochoi.data.InvoiceDAO;
import com.example.btl_bandochoi.model.Customer;
import com.example.btl_bandochoi.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private ImageView btnBack;
    private Button btnAddOrder;
    private InvoiceDAO invoiceDAO;
    private EditText edtSearch;
    private ImageView btnSearch;
    private CustomerDAO customerDAO;
    private List<Invoice> fullList; // Danh sách gốc để lọc
    private InvoiceAdapter invoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        invoiceDAO = new InvoiceDAO(this);
        customerDAO = new CustomerDAO(this);

        anhXa();
        setupRecyclerView();
        setEvent();
        loadOrders();
    }

    private void anhXa() {
        recyclerOrders = findViewById(R.id.recyclerOrders);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddOrder = findViewById(R.id.btnAddOrder);
        edtSearch = findViewById(R.id.edtSearch); // Ánh xạ edtSearch
    }

    private void setupRecyclerView() {
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrders() {
        fullList = invoiceDAO.getAllInvoices();
        updateAdapter(fullList);
    }

    private void updateAdapter(List<Invoice> list) {
        if (list != null) {
            invoiceAdapter = new InvoiceAdapter(this, list, invoice -> {
                Intent intent = new Intent(InvoiceActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("invoice_id", invoice.getId());
                startActivity(intent);
            });
            recyclerOrders.setAdapter(invoiceAdapter);
        }
    }

    private void setEvent() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnAddOrder != null) {
            btnAddOrder.setOnClickListener(v -> showAddInvoiceDialog());
        }

        // Sự kiện nút tìm kiếm (ẩn/hiện thanh tìm kiếm)
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                if (edtSearch.getVisibility() == View.GONE) {
                    edtSearch.setVisibility(View.VISIBLE);
                    edtSearch.requestFocus();
                } else {
                    edtSearch.setVisibility(View.GONE);
                    edtSearch.setText("");
                    loadOrders(); // Reset lại danh sách khi đóng tìm kiếm
                }
            });
        }

        // Sự kiện lọc dữ liệu khi gõ vào ô tìm kiếm
        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void filter(String text) {
        if (fullList == null) return;
        List<Invoice> filteredList = new ArrayList<>();
        for (Invoice item : fullList) {
            // Lọc theo mã hóa đơn
            if (item.getInvoiceCode() != null && 
                item.getInvoiceCode().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        updateAdapter(filteredList);
    }

    private void showAddInvoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_invoice, null);
        builder.setView(view);

        EditText edtInvoiceCode = view.findViewById(R.id.edtInvoiceCode);
        Spinner spinnerCustomer = view.findViewById(R.id.spinnerCustomer);
        EditText edtNotes = view.findViewById(R.id.edtNotes);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        List<Customer> customers = customerDAO.getAll();
        List<String> customerNames = new ArrayList<>();
        for (Customer c : customers) {
            customerNames.add(c.getName() + " - " + c.getPhone());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCustomer.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String code = edtInvoiceCode.getText().toString().trim();
            String notes = edtNotes.getText().toString().trim();

            if (code.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã đơn hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (customers.isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm khách hàng trước", Toast.LENGTH_SHORT).show();
                return;
            }

            int customerIndex = spinnerCustomer.getSelectedItemPosition();
            Customer selectedCustomer = customers.get(customerIndex);

            Invoice newInvoice = new Invoice();
            newInvoice.setInvoiceCode(code);
            newInvoice.setCustomerId(selectedCustomer.getId());
            newInvoice.setNotes(notes);
            newInvoice.setStatus("Pending");
            newInvoice.setTotal(0.0);

            long result = invoiceDAO.insertInvoice(newInvoice);
            if (result > 0) {
                Toast.makeText(this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                loadOrders();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Thêm đơn hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}