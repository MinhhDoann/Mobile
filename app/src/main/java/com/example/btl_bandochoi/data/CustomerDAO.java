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
        Log.i(TAG, "Database opened successfully");
    }

    public long insert(Customer c) {
        if (c == null || c.getPhone() == null || c.getPhone().trim().isEmpty()) {
            Log.e(TAG, "Insert failed: Phone is null or empty");
            return -1;
        }

        if (isPhoneExists(c.getPhone())) {
            Log.w(TAG, "Phone Đã Tồn Tại: " + c.getPhone());
            return -2;
        }
        if (isEmailExists(c.getEmail())) {
            Log.w(TAG, "Gmail Đã Tồn Tại: " + c.getEmail());
            return -2;
        }

        try {
            ContentValues values = new ContentValues();
            values.put("name", c.getName());
            values.put("gender", c.getGender());
            values.put("phone", c.getPhone().trim());
            values.put("email", c.getEmail() != null ? c.getEmail().trim() : null);
            values.put("address", c.getAddress() != null ? c.getAddress().trim() : null);
            values.put("image", c.getImage());
            values.put("status", c.getStatus() != null ? c.getStatus() : "active");

            long id = db.insertOrThrow("Customer", null, values);

            Log.i(TAG, "=== INSERT THÀNH CÔNG === ID = " + id);
            return id;

        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e(TAG, "Constraint violation (có thể phone trùng hoặc ràng buộc khác): " + e.getMessage(), e);
            return -2;
        } catch (Exception e) {
            Log.e(TAG, "LỖI KHÔNG XÁC ĐỊNH khi insert: " + e.getMessage(), e);
            e.printStackTrace();
            return -1;
        }
    }

    private boolean isPhoneExists(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT 1 FROM Customer WHERE phone = ?",
                    new String[]{phone.trim()});
            boolean exists = cursor.moveToFirst();
            Log.d(TAG, "Check phone " + phone + " exists: " + exists);
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "Error in isPhoneExists: " + e.getMessage(), e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }
    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) return false;

        Cursor cursor = db.rawQuery(
                "SELECT id FROM Customer WHERE email = ?",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        return exists;
    }

    public int update(Customer c) {
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("gender", c.getGender());
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM Customer", null);
            if (cursor.moveToFirst()) {
                do {
                    Customer c = new Customer();
                    c.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    c.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                    c.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                    c.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                    c.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                    c.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    c.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow("created_date")));
                    c.setTotalSpent(cursor.getDouble(cursor.getColumnIndexOrThrow("total_spent")));
                    c.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                    list.add(c);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in getAll(): " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        Log.i(TAG, "getAll() returned " + list.size() + " customers");
        return list;
    }
}