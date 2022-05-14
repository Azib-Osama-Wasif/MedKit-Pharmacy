package com.fyp22.medkitpharmacy.Models;

public class CartItem {
    String id;
    String medicineId;
    String customerId;
    String pharmacyId;

    public CartItem() {
    }

    public CartItem(String id, String medicineId, String customerId, String pharmacyId) {
        this.id = id;
        this.medicineId = medicineId;
        this.customerId = customerId;
        this.pharmacyId = pharmacyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
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
