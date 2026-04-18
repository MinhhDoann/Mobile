package com.example.btl_bandochoi.model;

public class Invoice {
    private int id;
    private String invoiceCode;
    private String date;
    private double total;
    private int customerId;
    private String customerName;   // dùng để hiển thị

    public Invoice() {}

    public Invoice(int id, String invoiceCode, String date, double total, int customerId, String customerName) {
        this.id = id;
        this.invoiceCode = invoiceCode;
        this.date = date;
        this.total = total;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getId() { return id; }
    public String getInvoiceCode() { return invoiceCode; }
    public String getDate() { return date; }
    public double getTotal() { return total; }
    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }

    public void setId(int id) { this.id = id; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }
    public void setDate(String date) { this.date = date; }
    public void setTotal(double total) { this.total = total; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}