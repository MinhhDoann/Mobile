package com.example.btl_bandochoi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ToyStore.db";
    private static final int DB_VERSION = 3; 

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

        // BẢNG LỊCH SỬ GIAO DỊCH KHÁCH HÀNG (SỬ DỤNG KHÓA NGOẠI)
        db.execSQL("CREATE TABLE TransactionHistory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_id INTEGER," +
                "invoice_id INTEGER," +
                "date TEXT DEFAULT (datetime('now','localtime'))," +
                "FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE CASCADE," +
                "FOREIGN KEY(invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE" +
                ")");

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        // ... (Giữ nguyên dữ liệu mẫu)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TransactionHistory");
        db.execSQL("DROP TABLE IF EXISTS StockHistory");
        db.execSQL("DROP TABLE IF EXISTS InvoiceDetail");
        db.execSQL("DROP TABLE IF EXISTS Invoice");
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS Customer");
        db.execSQL("DROP TABLE IF EXISTS Category");
        onCreate(db);
    }
}
