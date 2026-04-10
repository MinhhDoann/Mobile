package com.example.btl_bandochoi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Product> getLowStockProducts(int limit) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Product WHERE quantity > 0 AND quantity <= 10 LIMIT ?",
                new String[]{String.valueOf(limit)}
        );

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();

                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                p.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                p.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));

                list.add(p);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public long insertProduct(String name, String description,
                              double price, int quantity,
                              int ageFrom, int ageTo,
                              String status, String image, int categoryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("age_from", ageFrom);
        values.put("age_to", ageTo);
        values.put("status", status);
        values.put("image", image);
        values.put("category_id", categoryId);

        long id = db.insert("Product", null, values);
        if (id == -1) {
            Log.e("DB", "Insert failed");
        }
        db.close();
        return id;
    }

    public int updateProduct(int id, String name, String description,
                             double price, int quantity,
                             int ageFrom, int ageTo,
                             String status, String image, int categoryId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("age_from", ageFrom);
        values.put("age_to", ageTo);
        values.put("status", status);
        values.put("image", image);
        values.put("category_id", categoryId);

        int rowsAffected = db.update("Product", values, "id=?",
                new String[]{String.valueOf(id)});
        db.close();
        if (rowsAffected == 0) {
            Log.e("DB", "Update failed");
        }
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
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM Product", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product p = new Product();

                    p.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    p.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    p.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    p.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    p.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                    p.setAgeFrom(cursor.getInt(cursor.getColumnIndexOrThrow("age_from")));
                    p.setAgeTo(cursor.getInt(cursor.getColumnIndexOrThrow("age_to")));
                    p.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                    p.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    p.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));

                    list.add(p);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }
}