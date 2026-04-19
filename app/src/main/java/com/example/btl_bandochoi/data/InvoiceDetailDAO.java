package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.InvoiceDetail;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {
    private DatabaseHelper dbHelper;

    public InvoiceDetailDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<InvoiceDetail> getDetailsByInvoiceId(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT d.id, d.invoice_id, d.product_id, d.quantity, d.price, d.discount, p.name " +
                       "FROM InvoiceDetail d " +
                       "JOIN Product p ON d.product_id = p.id " +
                       "WHERE d.invoice_id = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});

        if (cursor.moveToFirst()) {
            do {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setId(cursor.getInt(0));
                detail.setInvoiceId(cursor.getInt(1));
                detail.setProductId(cursor.getInt(2));
                detail.setQuantity(cursor.getInt(3));
                detail.setPrice(cursor.getDouble(4));
                detail.setDiscount(cursor.getDouble(5));
                detail.setProductName(cursor.getString(6));
                list.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long insert(InvoiceDetail detail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("invoice_id", detail.getInvoiceId());
        values.put("product_id", detail.getProductId());
        values.put("quantity", detail.getQuantity());
        values.put("price", detail.getPrice());
        values.put("discount", detail.getDiscount());
        return db.insert("InvoiceDetail", null, values);
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("InvoiceDetail", "id = ?", new String[]{String.valueOf(id)});
    }

    public InvoiceDetail getDetailByInvoiceAndProduct(int invoiceId, int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("InvoiceDetail", null, "invoice_id = ? AND product_id = ?",
                new String[]{String.valueOf(invoiceId), String.valueOf(productId)}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setId(cursor.getInt(0));
            detail.setInvoiceId(cursor.getInt(1));
            detail.setProductId(cursor.getInt(2));
            detail.setQuantity(cursor.getInt(3));
            detail.setPrice(cursor.getDouble(4));
            detail.setDiscount(cursor.getDouble(5));
            cursor.close();
            return detail;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public int updateQuantity(int id, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        return db.update("InvoiceDetail", values, "id = ?", new String[]{String.valueOf(id)});
    }
}
