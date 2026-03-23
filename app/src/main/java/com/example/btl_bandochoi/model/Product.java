package com.example.btl_bandochoi.model;

public class Product {
    public int id;
    public String name;
    public String image;
    public double price;
    public int stock;
    public int sold;
    public String status;

    public Product(int id, String name, String image, double price, int stock, int sold, String status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.sold = sold;
        this.status = status;
    }
}