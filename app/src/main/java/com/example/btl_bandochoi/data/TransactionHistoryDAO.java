package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.TransactionHistory;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDAO {
    private DatabaseHelper dbHelper;

    public TransactionHistoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<TransactionHistory> getHistoryByCustomerId(int customerId) {
        List<TransactionHistory> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("TransactionHistory", null, "customer_id = ?",
                new String[]{String.valueOf(customerId)}, null, null, "date DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TransactionHistory h = new TransactionHistory();
                // Sửa lỗi: Lấy dữ liệu theo tên cột để tránh lệch index
                h.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                h.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow("customer_id")));
                h.setInvoiceCode(cursor.getString(cursor.getColumnIndexOrThrow("invoice_code")));
                h.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount")));
                h.setItemCount(cursor.getInt(cursor.getColumnIndexOrThrow("item_count")));
                h.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                list.add(h);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long insert(TransactionHistory history) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("customer_id", history.getCustomerId());
        v.put("invoice_code", history.getInvoiceCode());
        v.put("total_amount", history.getTotalAmount());
        v.put("item_count", history.getItemCount());
        return db.insert("TransactionHistory", null, v);
    }

    public int updateHistory(String invoiceCode, double totalAmount, int itemCount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("total_amount", totalAmount);
        v.put("item_count", itemCount);
        return db.update("TransactionHistory", v, "invoice_code = ?", new String[]{invoiceCode});
    }
}
