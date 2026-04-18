package com.example.btl_bandochoi.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String address;
    private double totalSpent;

    public Customer() {}

    public Customer(int id, String name, String phone, String address, double totalSpent) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.totalSpent = totalSpent;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
}