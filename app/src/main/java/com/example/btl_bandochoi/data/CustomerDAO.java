package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String TAG = "CustomerDAO";
    private SQLiteDatabase db;

    public CustomerDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Customer c) {
        if (c == null || c.getPhone() == null || c.getPhone().trim().isEmpty()) return -1;
        if (isPhoneExists(c.getPhone())) return -2;

        try {
            ContentValues values = new ContentValues();
            values.put("name", c.getName());
            values.put("phone", c.getPhone().trim());
            values.put("email", c.getEmail());
            values.put("address", c.getAddress());
            values.put("image", c.getImage());
            values.put("status", c.getStatus() != null ? c.getStatus() : "active");

            return db.insertOrThrow("Customer", null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error in insert(): " + e.getMessage());
            return -1;
        }
    }

    private boolean isPhoneExists(String phone) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM Customer WHERE phone = ?", new String[]{phone.trim()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public int update(Customer c) {
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("phone", c.getPhone());
        values.put("email", c.getEmail());
        values.put("address", c.getAddress());
        values.put("image", c.getImage());
        values.put("status", c.getStatus());
        return db.update("Customer", values, "id=?", new String[]{String.valueOf(c.getId())});
    }

    public int delete(int id) {
        return db.delete("Customer", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        // Sử dụng Subquery để tính tổng chi tiêu thực tế từ bảng Invoice
        String query = "SELECT c.id, c.name, c.phone, c.email, c.address, c.image, c.created_date, c.status, " +
                       "(SELECT IFNULL(SUM(total), 0) FROM Invoice WHERE customer_id = c.id) as real_spent " +
                       "FROM Customer c";
        
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Customer c = new Customer();
                    c.setId(cursor.getInt(0));
                    c.setName(cursor.getString(1));
                    c.setPhone(cursor.getString(2));
                    c.setEmail(cursor.getString(3));
                    c.setAddress(cursor.getString(4));
                    c.setImage(cursor.getString(5));
                    c.setCreatedDate(cursor.getString(6));
                    c.setStatus(cursor.getString(7));
                    
                    // Lấy tổng chi tiêu được tính toán trực tiếp từ các hóa đơn
                    double spent = cursor.getDouble(8);
                    c.setTotalSpent(spent);
                    
                    list.add(c);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in getAll(): " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
    
    public Customer getCustomerById(int id) {
        String query = "SELECT c.id, c.name, c.phone, c.email, c.address, c.image, c.created_date, c.status, " +
                       "(SELECT IFNULL(SUM(total), 0) FROM Invoice WHERE customer_id = c.id) as real_spent " +
                       "FROM Customer c WHERE c.id = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                Customer c = new Customer();
                c.setId(cursor.getInt(0));
                c.setName(cursor.getString(1));
                c.setPhone(cursor.getString(2));
                c.setEmail(cursor.getString(3));
                c.setAddress(cursor.getString(4));
                c.setImage(cursor.getString(5));
                c.setCreatedDate(cursor.getString(6));
                c.setStatus(cursor.getString(7));
                c.setTotalSpent(cursor.getDouble(8));
                return c;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in getCustomerById(): " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
