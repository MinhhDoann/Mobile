package com.example.btl_bandochoi.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String createdDate;
    private double totalSpent;
    private String status;

    public Customer() {}

    public Customer(int id, String name, String phone, String email,
                    String address, String createdDate,
                    double totalSpent, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdDate = createdDate;
        this.totalSpent = totalSpent;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}