package com.fyp22.medkitpharmacy.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CartItem implements Serializable, Parcelable {
    String id;
    String stockItemId;
    String customerId;
    String pharmacyId;
    int quantity;

    public CartItem() {
    }

    public CartItem(String id, String stockItemId, String customerId, String pharmacyId, int quantity) {
        this.id = id;
        this.stockItemId = stockItemId;
        this.customerId = customerId;
        this.pharmacyId = pharmacyId;
        this.quantity = quantity;
    }


    protected CartItem(Parcel in) {
        id = in.readString();
        stockItemId = in.readString();
        customerId = in.readString();
        pharmacyId = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(String stockItemId) {
        this.stockItemId = stockItemId;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(stockItemId);
        parcel.writeString(customerId);
        parcel.writeString(pharmacyId);
        parcel.writeInt(quantity);
    }
}
