/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homedecoreapp.AppFunctionalities;

/**
 *
 * @author Kapnang
 */
public class Order {
    private int id;
    private int warehouseManagerId; 
    private int supplierId;       
    private int productId;        
    private int quantityRequested;
    private String orderStatus;    
    private String orderDate;      

    
    public Order() {}

    public Order(int warehouseManagerId, int supplierId, int productId, int quantityRequested) {
        this.warehouseManagerId = warehouseManagerId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.quantityRequested = quantityRequested;
        this.orderStatus = "pending"; // Initial status
    }
    // ... Getters and Setters for all fields (id, warehouseManagerId, supplierId, productId, quantityRequested, orderStatus, orderDate)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWarehouseManagerId() {
        return warehouseManagerId;
    }

    public void setWarehouseManagerId(int warehouseManagerId) {
        this.warehouseManagerId = warehouseManagerId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() { // For debugging
        return "Order{" +
                "id=" + id +
                ", warehouseManagerId=" + warehouseManagerId +
                ", supplierId=" + supplierId +
                ", productId=" + productId +
                ", quantityRequested=" + quantityRequested +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
