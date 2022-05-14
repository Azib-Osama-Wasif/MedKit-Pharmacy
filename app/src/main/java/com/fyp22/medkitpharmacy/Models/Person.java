package com.fyp22.medkitpharmacy.Models;

public class Person {
    String id;
    String name;
    String contact;
    String age;
    String cnic;
    String city;
    String email;
    String password;
    String image;
    String address;

    public Person() {
    }

    public Person(String id, String name, String contact, String age, String cnic, String city, String email, String password, String image, String address) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.age = age;
        this.cnic = cnic;
        this.city = city;
        this.email = email;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
