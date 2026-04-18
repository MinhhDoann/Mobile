import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_bandochoi.model.Invoice;// Thêm hóa đơn mới
public long insertInvoice(Invoice invoice) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("invoice_code", invoice.getInvoiceCode());
    values.put("customer_id", invoice.getCustomerId());
    values.put("payment_method", invoice.getPaymentMethod());
    values.put("notes", invoice.getNotes());
    values.put("status", invoice.getStatus() != null ? invoice.getStatus() : "Pending");
    values.put("total", invoice.getTotal());

    long id = db.insert("Invoice", null, values);
    db.close();
    return id;
}

// Lấy tất cả hóa đơn (kết hợp tên khách hàng)
public List<Invoice> getAllInvoices() {
    List<Invoice> list = new ArrayList<>();
    SQLiteDatabase db = dbHelper.getReadableDatabase();

    String query = "SELECT i.id, i.invoice_code, i.date, i.total, i.status, " +
            "c.name as customer_name, c.address as customer_address " +
            "FROM Invoice i LEFT JOIN Customer c ON i.customer_id = c.id " +
            "ORDER BY i.date DESC";

    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
        do {
            Invoice inv = new Invoice();
            inv.setId(cursor.getInt(0));
            inv.setInvoiceCode(cursor.getString(1));
            inv.setDate(cursor.getString(2));
            inv.setTotal(cursor.getDouble(3));
            inv.setStatus(cursor.getString(4));
            inv.setCustomerName(cursor.getString(5));
            inv.setCustomerAddress(cursor.getString(6));
            list.add(inv);
        } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return list;
}

// Lấy 1 hóa đơn theo ID (dùng cho chi tiết)
public Invoice getInvoiceById(int id) {
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    String query = "SELECT i.*, c.name as customer_name, c.address as customer_address " +
            "FROM Invoice i LEFT JOIN Customer c ON i.customer_id = c.id WHERE i.id = ?";

    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

    if (cursor.moveToFirst()) {
        Invoice inv = new Invoice();
        inv.setId(cursor.getInt(cursor.getColumnIndex("id")));
        inv.setInvoiceCode(cursor.getString(cursor.getColumnIndex("invoice_code")));
        inv.setDate(cursor.getString(cursor.getColumnIndex("date")));
        inv.setTotal(cursor.getDouble(cursor.getColumnIndex("total")));
        inv.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        inv.setPaymentMethod(cursor.getString(cursor.getColumnIndex("payment_method")));
        inv.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
        inv.setCustomerId(cursor.getInt(cursor.getColumnIndex("customer_id")));
        inv.setCustomerName(cursor.getString(cursor.getColumnIndex("customer_name")));
        inv.setCustomerAddress(cursor.getString(cursor.getColumnIndex("customer_address")));
        cursor.close();
        db.close();
        return inv;
    }
    cursor.close();
    db.close();
    return null;
}