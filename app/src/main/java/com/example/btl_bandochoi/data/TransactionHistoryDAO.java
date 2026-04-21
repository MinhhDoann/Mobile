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
        
        // Sử dụng JOIN để lấy thông tin thực tế từ bảng Invoice
        String query = "SELECT th.id, th.customer_id, th.invoice_id, th.date, " +
                       "i.invoice_code, i.total, " +
                       "(SELECT COUNT(*) FROM InvoiceDetail WHERE invoice_id = i.id) as item_count " +
                       "FROM TransactionHistory th " +
                       "JOIN Invoice i ON th.invoice_id = i.id " +
                       "WHERE th.customer_id = ? " +
                       "ORDER BY th.date DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TransactionHistory h = new TransactionHistory();
                h.setId(cursor.getInt(0));
                h.setCustomerId(cursor.getInt(1));
                // Lấy thông tin từ bảng Invoice thông qua JOIN
                h.setInvoiceCode(cursor.getString(4)); 
                h.setTotalAmount(cursor.getDouble(5));
                h.setItemCount(cursor.getInt(6));
                h.setDate(cursor.getString(3));
                list.add(h);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long insert(int customerId, int invoiceId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("customer_id", customerId);
        v.put("invoice_id", invoiceId);
        return db.insert("TransactionHistory", null, v);
    }

    // Phương thức kiểm tra và tự vá lịch sử nếu thiếu
    public boolean exists(int invoiceId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM TransactionHistory WHERE invoice_id = ?", 
                new String[]{String.valueOf(invoiceId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}
