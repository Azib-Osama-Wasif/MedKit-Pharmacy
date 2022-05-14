package com.fyp22.medkitpharmacy.Models;

public class StockItem {

    String id;
    String pharmacyId;
    String medicineId;
    int availableAmount;

    public StockItem() {
    }

    public StockItem(String id, String pharmacyId, String medicineId, int availableAmount) {
        this.id = id;
        this.pharmacyId = pharmacyId;
        this.medicineId = medicineId;
        this.availableAmount = availableAmount;
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

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
}
