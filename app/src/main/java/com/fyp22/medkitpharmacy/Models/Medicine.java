package com.fyp22.medkitpharmacy.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Medicine implements Serializable, Parcelable {
    String id;
    String name;
    int price;
    String expDate;
    String description;
    String manufacturer;
    String type;
    String image;

    public Medicine() {
    }

    public Medicine(String id, String name, int price, String expDate, String description, String manufacturer, String type, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expDate = expDate;
        this.description = description;
        this.manufacturer = manufacturer;
        this.type = type;
        this.image= image;
    }

    protected Medicine(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readInt();
        expDate = in.readString();
        description = in.readString();
        manufacturer = in.readString();
        type = in.readString();
        image = in.readString();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeString(expDate);
        parcel.writeString(description);
        parcel.writeString(manufacturer);
        parcel.writeString(type);
        parcel.writeString(image);
    }
}
