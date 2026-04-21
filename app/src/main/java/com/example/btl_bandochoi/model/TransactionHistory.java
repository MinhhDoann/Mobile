package com.example.btl_bandochoi.model;

public class TransactionHistory {
    private int id;
    private int customerId;
    private String invoiceCode;
    private double totalAmount;
    private int itemCount;
    private String date;

    public TransactionHistory() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getInvoiceCode() { return invoiceCode; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
