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
    private SQLiteDatabase db;

    public CustomerDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Customer c) {
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("phone", c.getPhone());
        values.put("email", c.getEmail());
        values.put("address", c.getAddress());
        values.put("status", c.getStatus());
        return db.insert("Customer", null, values);
    }

    public int update(Customer c) {
        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("phone", c.getPhone());
        values.put("email", c.getEmail());
        values.put("address", c.getAddress());
        values.put("status", c.getStatus());
        return db.update("Customer", values, "id=?", new String[]{String.valueOf(c.getId())});
    }

    public int delete(int id) {
        return db.delete("Customer", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer", null);

        while (cursor.moveToNext()) {
            Customer c = new Customer();
            c.setId(cursor.getInt(0));
            c.setName(cursor.getString(1));
            c.setPhone(cursor.getString(2));
            c.setEmail(cursor.getString(3));
            c.setAddress(cursor.getString(4));
            c.setCreatedDate(cursor.getString(5));
            c.setTotalSpent(cursor.getDouble(6));
            c.setStatus(cursor.getString(7));
            list.add(c);
        }
        cursor.close();
        return list;
    }
}