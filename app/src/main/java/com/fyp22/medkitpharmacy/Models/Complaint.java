package com.fyp22.medkitpharmacy.Models;

import java.io.Serializable;

public class Complaint implements Serializable {
    String id;
    String date;
    String customerId;
    String pharmacyId;
    String description;

    public Complaint() {
    }

    public Complaint(String id, String date, String customerId, String pharmacyId, String description) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.pharmacyId = pharmacyId;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
