package com.example.btl_bandochoi.database;

import android.content.Context;
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

        db.execSQL("CREATE TABLE Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");

        db.execSQL("CREATE TABLE Product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "price REAL," +
                "quantity INTEGER," +
                "image TEXT," +
                "category_id INTEGER)");

        db.execSQL("CREATE TABLE Customer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "phone TEXT)");

        db.execSQL("CREATE TABLE Invoice (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "total REAL," +
                "customer_id INTEGER)");

        db.execSQL("CREATE TABLE InvoiceDetail (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoice_id INTEGER," +
                "product_id INTEGER," +
                "quantity INTEGER," +
                "price REAL)");

        db.execSQL("CREATE TABLE StockHistory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER," +
                "type TEXT," +
                "quantity INTEGER," +
                "date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Product");
        onCreate(db);
    }
}