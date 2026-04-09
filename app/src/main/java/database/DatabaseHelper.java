package database;
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

        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL DEFAULT 0," +
                "quantity INTEGER NOT NULL DEFAULT 0," +
                "image TEXT," +
                "category_id INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES Category(id) ON DELETE SET NULL)");

        db.execSQL("CREATE TABLE Customer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "phone TEXT)");

        db.execSQL("CREATE TABLE Invoice (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT NOT NULL," +
                "total REAL DEFAULT 0," +
                "customer_id INTEGER," +
                "FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE SET NULL)");

        db.execSQL("CREATE TABLE InvoiceDetail (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoice_id INTEGER," +
                "product_id INTEGER," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY(invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE," +
                "FOREIGN KEY(product_id) REFERENCES Product(id))");

        db.execSQL("CREATE TABLE StockHistory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER," +
                "type TEXT," +
                "quantity INTEGER," +
                "date TEXT," +
                "FOREIGN KEY(product_id) REFERENCES Product(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Product");
        onCreate(db);
    }
}