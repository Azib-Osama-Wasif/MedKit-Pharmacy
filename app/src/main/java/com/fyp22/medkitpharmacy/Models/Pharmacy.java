package com.fyp22.medkitpharmacy.Models;

import java.io.Serializable;
import java.util.List;

public class Pharmacy implements Serializable {
    String id;
    String name;
    String contact;
    String email;
    String address;
    String city;
    String password;
    String dateEstablished;
    String image;
    List<StockItem> stockItemList;

    public Pharmacy() {
    }

    public Pharmacy(String id, String name, String contact, String email, String address, String city, String password, String dateEstablished, String image, List<StockItem> stockItemList) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.city = city;
        this.password = password;
        this.dateEstablished = dateEstablished;
        this.image = image;
        this.stockItemList = stockItemList;
    }


    public List<StockItem> getStockItemList() {
        return stockItemList;
    }

    public void setStockItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateEstablished() {
        return dateEstablished;
    }

    public void setDateEstablished(String dateEstablished) {
        this.dateEstablished = dateEstablished;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
