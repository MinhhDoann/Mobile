package com.example.btl_bandochoi.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

        // TRUY VẤN TRỰC TIẾP TỪ BẢNG INVOICE - Chắc chắn có dữ liệu nếu Tổng chi > 0
        String query = "SELECT id, invoice_code, date, total, " +
                "(SELECT SUM(quantity) FROM InvoiceDetail WHERE invoice_id = i.id) as total_qty " +
                "FROM Invoice i " +
                "WHERE customer_id = ? " +
                "ORDER BY date DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TransactionHistory h = new TransactionHistory();
                
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                h.setInvoiceId(id);
                h.setCustomerId(customerId);
                
                String code = cursor.getString(cursor.getColumnIndexOrThrow("invoice_code"));
                h.setInvoiceCode(code != null ? code : "HD" + id);
                
                h.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                h.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("total")));
                
                int qtyIdx = cursor.getColumnIndexOrThrow("total_qty");
                h.setItemCount(cursor.isNull(qtyIdx) ? 0 : cursor.getInt(qtyIdx));
                
                list.add(h);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("HistoryDAO", "Lấy được " + list.size() + " hóa đơn cho khách hàng ID: " + customerId);
        return list;
    }
}