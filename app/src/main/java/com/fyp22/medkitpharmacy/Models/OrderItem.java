package com.fyp22.medkitpharmacy.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OrderItem implements Serializable, Parcelable {
    String productId;
    String storeId;
    int quantity;

    public OrderItem() {
    }

    public OrderItem(String productId, String storeId, int quantity) {
        this.productId = productId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    protected OrderItem(Parcel in) {
        productId = in.readString();
        storeId = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(storeId);
        parcel.writeInt(quantity);
    }
}
