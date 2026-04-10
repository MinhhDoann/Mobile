package com.example.btl_bandochoi.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int ageFrom;
    private int ageTo;
    private String status;
    private String image;
    private int categoryId;

    private boolean expanded = false;

    public Product() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAgeFrom() { return ageFrom; }
    public void setAgeFrom(int ageFrom) { this.ageFrom = ageFrom; }

    public int getAgeTo() { return ageTo; }
    public void setAgeTo(int ageTo) { this.ageTo = ageTo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
}