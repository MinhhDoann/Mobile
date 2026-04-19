package com.example.btl_bandochoi.data;

import android.content.ContentValues;
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

    public long insertInvoice(Invoice invoice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("invoice_code", invoice.getInvoiceCode());
        values.put("customer_id", invoice.getCustomerId());
        values.put("payment_method", invoice.getPaymentMethod());
        values.put("notes", invoice.getNotes());
        values.put("status", invoice.getStatus() != null ? invoice.getStatus() : "Pending");
        values.put("total", invoice.getTotal());

        long id = db.insert("Invoice", null, values);
        db.close();
        return id;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT i.id, i.invoice_code, i.date, i.total, i.status, " +
                "c.name as customer_name, c.address as customer_address " +
                "FROM Invoice i " +
                "LEFT JOIN Customer c ON i.customer_id = c.id " +
                "ORDER BY i.date DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Invoice inv = new Invoice();
                inv.setId(cursor.getInt(0));
                inv.setInvoiceCode(cursor.getString(1));
                inv.setDate(cursor.getString(2));
                inv.setTotal(cursor.getDouble(3));
                inv.setStatus(cursor.getString(4));
                inv.setCustomerName(cursor.getString(5));
                inv.setCustomerAddress(cursor.getString(6));

                list.add(inv);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Invoice getInvoiceById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT i.*, c.name as customer_name, c.address as customer_address " +
                "FROM Invoice i " +
                "LEFT JOIN Customer c ON i.customer_id = c.id " +
                "WHERE i.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Invoice inv = new Invoice();
            inv.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            inv.setInvoiceCode(cursor.getString(cursor.getColumnIndexOrThrow("invoice_code")));
            inv.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            inv.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("total")));
            inv.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            inv.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow("payment_method")));
            inv.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
            inv.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow("customer_id")));
            inv.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow("customer_name")));
            inv.setCustomerAddress(cursor.getString(cursor.getColumnIndexOrThrow("customer_address")));

            cursor.close();
            db.close();
            return inv;
        }

        cursor.close();
        db.close();
        return null;
    }

    public int updateTotal(int invoiceId, double newTotal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("total", newTotal);

        int rows = db.update("Invoice", values, "id = ?",
                new String[]{String.valueOf(invoiceId)});
        db.close();
        return rows;
    }

    public int deleteInvoice(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("Invoice", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
}