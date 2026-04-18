package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private DatabaseHelper dbHelper;

    public CustomerDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm khách hàng mới
    public long insertCustomer(String name, String phone, String address) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("address", address);

        long id = db.insert("Customer", null, values);
        db.close();
        return id;
    }

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, name, phone, address, total_spent FROM Customer ORDER BY name", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String address = cursor.getString(3);
                double totalSpent = cursor.getDouble(4);

                list.add(new Customer(id, name, phone, address, totalSpent));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Lấy 1 khách hàng theo ID
    public Customer getCustomerById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer WHERE id = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Customer c = new Customer();
            c.setId(cursor.getInt(0));
            c.setName(cursor.getString(1));
            c.setPhone(cursor.getString(2));
            c.setAddress(cursor.getString(3));
            c.setTotalSpent(cursor.getDouble(4));
            cursor.close();
            db.close();
            return c;
        }
        cursor.close();
        db.close();
        return null;
    }
}