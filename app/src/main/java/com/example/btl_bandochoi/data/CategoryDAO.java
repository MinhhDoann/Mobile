package com.example.btl_bandochoi.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_bandochoi.database.DatabaseHelper;
import com.example.btl_bandochoi.model.Category;

import java.util.*;

public class CategoryDAO {
    SQLiteDatabase db;

    public CategoryDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM Category", null);

        while (c.moveToNext()) {
            Category cat = new Category();
            cat.setId(c.getInt(0));
            cat.setName(c.getString(1));
            list.add(cat);
        }
        c.close();
        return list;
    }

    public void insert(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert("Category", null, values);
    }

    public void update(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.update("Category", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void delete(int id) {
        db.delete("Category", "id=?", new String[]{String.valueOf(id)});
    }
}