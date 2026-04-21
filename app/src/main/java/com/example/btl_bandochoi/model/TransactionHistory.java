package com.example.btl_bandochoi.model;

public class TransactionHistory {
    private int id;
    private int customerId;
    private int invoiceId;
    private String date;
    
    // Thêm các trường hiển thị bổ sung từ JOIN
    private String invoiceCode;
    private double totalAmount;
    private int itemCount;

    public TransactionHistory() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getInvoiceCode() { return invoiceCode; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}