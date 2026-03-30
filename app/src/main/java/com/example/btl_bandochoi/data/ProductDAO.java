package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public long insertProduct(String name, double price, int quantity, String image, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("image", image);
        values.put("category_id", categoryId);

        long id = db.insert("Product", null, values);
        db.close();
        return id;
    }

    public int updateProduct(int id, String name, double price, int quantity, String image, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("image", image);
        values.put("category_id", categoryId);

        int rowsAffected = db.update("Product", values, "id=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Product", "id=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Product", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                int quantity = cursor.getInt(3);
                String image = cursor.getString(4);

                list.add(new Product(id, name, price, quantity, image));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}