/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homedecoreapp;

/**
 *
 * @author Kapnang
 */


public class Product {
    private int id;
    private String name;
    private String description;
    private String category;
    private int quantityInStock;
    private double price;
    private int supplierId; // Foreign key
    private String dateAdded; // Could also be a Date object

    // Constructor
    public Product() {} // For use with ResultSet and setting values individually

    public Product(String name, String description, String category, int quantityInStock, double price, int supplierId) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.supplierId = supplierId;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }



    @Override
    public String toString() { // For debugging and JTable display (if needed)
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", price=" + price +
                ", supplierId=" + supplierId +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}
