package com.example.btl_bandochoi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ToyStore.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "image TEXT" +
                ")");

        db.execSQL("CREATE TABLE Product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL DEFAULT 0," +
                "quantity INTEGER NOT NULL DEFAULT 0," +
                "age_from INTEGER," +
                "age_to INTEGER," +
                "status TEXT DEFAULT 'active'," +
                "image TEXT," +
                "category_id INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES Category(id) ON DELETE SET NULL" +
                ")");

        db.execSQL("CREATE TABLE Customer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "gender TEXT CHECK(gender IN ('nữ','nam','other'))," +
                "phone TEXT NOT NULL UNIQUE," +
                "email TEXT," +
                "address TEXT," +
                "image TEXT," +
                "created_date TEXT DEFAULT (datetime('now','localtime'))," +
                "total_spent REAL DEFAULT 0," +
                "status TEXT DEFAULT 'active' CHECK(status IN ('active','inactive'))" +
                ")");

        db.execSQL("CREATE TABLE Invoice (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoice_code TEXT," +
                "date TEXT DEFAULT (datetime('now','localtime'))," +
                "total REAL DEFAULT 0," +
                "status TEXT DEFAULT 'Pending'," +
                "payment_method TEXT," +
                "notes TEXT," +
                "customer_id INTEGER," +
                "FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE SET NULL" +
                ")");

        db.execSQL("CREATE TABLE InvoiceDetail (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoice_id INTEGER," +
                "product_id INTEGER," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "discount REAL DEFAULT 0," +
                "FOREIGN KEY(invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE," +
                "FOREIGN KEY(product_id) REFERENCES Product(id) ON DELETE SET NULL" +
                ")");

        db.execSQL("CREATE TABLE StockHistory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER," +
                "type TEXT CHECK(type IN ('import','export','adjust'))," +
                "quantity INTEGER," +
                "date TEXT DEFAULT (datetime('now','localtime'))," +
                "FOREIGN KEY(product_id) REFERENCES Product(id) ON DELETE CASCADE" +
                ")");

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        ContentValues cat1 = new ContentValues();
        cat1.put("name", "Lego");
        cat1.put("description", "Đồ chơi lắp ráp trí tuệ");
        long catId1 = db.insert("Category", null, cat1);

        ContentValues cat2 = new ContentValues();
        cat2.put("name", "Búp bê");
        cat2.put("description", "Đồ chơi dành cho bé gái");
        long catId2 = db.insert("Category", null, cat2);

        ContentValues p1 = new ContentValues();
        p1.put("name", "Lego Phi Thuyền");
        p1.put("price", 550000);
        p1.put("quantity", 2);
        p1.put("category_id", catId1);
        db.insert("Product", null, p1);

        ContentValues p2 = new ContentValues();
        p2.put("name", "Búp bê Barbie");
        p2.put("price", 320000);
        p2.put("quantity", 10);
        p2.put("category_id", catId2);
        db.insert("Product", null, p2);

        ContentValues p3 = new ContentValues();
        p3.put("name", "Lego Xe Đua");
        p3.put("price", 450000);
        p3.put("quantity", 1);
        p3.put("category_id", catId1);
        db.insert("Product", null, p3);

        ContentValues c1 = new ContentValues();
        c1.put("name", "Nguyễn Văn An");
        c1.put("phone", "0987654321");
        c1.put("address", "Hà Nội");
        db.insert("Customer", null, c1);

        ContentValues c2 = new ContentValues();
        c2.put("name", "Trần Thị Bình");
        c2.put("phone", "0123456789");
        c2.put("address", "TP.HCM");
        db.insert("Customer", null, c2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS StockHistory");
        db.execSQL("DROP TABLE IF EXISTS InvoiceDetail");
        db.execSQL("DROP TABLE IF EXISTS Invoice");
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS Customer");
        db.execSQL("DROP TABLE IF EXISTS Category");
        onCreate(db);
    }
}
