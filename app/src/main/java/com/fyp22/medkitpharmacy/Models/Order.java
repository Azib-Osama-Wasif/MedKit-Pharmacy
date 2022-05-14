package com.fyp22.medkitpharmacy.Models;

import java.io.Serializable;

public class Order implements Serializable {
    String id;
    String customerId;
    String medicineId;
    String pharmacyId;
    String dateTime;
    String status;
    int amount;

    public Order() {
    }

    public Order(String id, String customerId, String medicineId, String pharmacyId, String dateTime, String status, int amount) {
        this.id = id;
        this.customerId = customerId;
        this.medicineId = medicineId;
        this.pharmacyId = pharmacyId;
        this.dateTime = dateTime;
        this.status = status;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
