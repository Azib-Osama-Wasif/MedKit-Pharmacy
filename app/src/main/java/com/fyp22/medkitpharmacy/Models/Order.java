package com.fyp22.medkitpharmacy.Models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    String id;
    String customerId;
    String medicineId;
    String pharmacyId;
    String dateTime;
    String status;
    int totalAmount;
    List<OrderItem> orderItems;

    public Order() {
    }

    public Order(String id, String customerId, String medicineId, String pharmacyId, String dateTime, String status, int totalAmount, List<OrderItem> orderItems) {
        this.id = id;
        this.customerId = customerId;
        this.medicineId = medicineId;
        this.pharmacyId = pharmacyId;
        this.dateTime = dateTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
}
