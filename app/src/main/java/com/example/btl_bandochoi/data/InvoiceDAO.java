package com.example.btl_bandochoi.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    private DatabaseHelper dbHelper;

    public InvoiceDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT i.id, i.invoice_code, i.date, i.total, i.customer_id, c.name " +
                "FROM Invoice i LEFT JOIN Customer c ON i.customer_id = c.id " +
                "ORDER BY i.date DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String invoiceCode = cursor.getString(1);
                String date = cursor.getString(2);
                double total = cursor.getDouble(3);
                int customerId = cursor.getInt(4);
                String customerName = cursor.getString(5);

                list.add(new Invoice(id, invoiceCode, date, total, customerId, customerName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}