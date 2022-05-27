package com.fyp22.medkitpharmacy.Common;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;

import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Models.Admin;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    Context context;
    MyAdminPreferences adminPreferences;
    MyPharmacyPreferences pharmacyPreferences;
    MyCustomerPreferences customerPreferences;

    Admin admin;
    Pharmacy pharmacy;
    Customer customer;
    Medicine medicine;

    List<Pharmacy> pharmacies;
    List<Customer> customers;
    List<Medicine> medicines;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser user;

    Resources resources;

    public DatabaseHandler(Context context) {

        this.customers = new ArrayList<>();
        this.pharmacies = new ArrayList<>();
        this.medicines = new ArrayList<>();

        this.context=context;
        this.database= FirebaseDatabase.getInstance();
        this.reference= database.getReference();
        this.storage= FirebaseStorage.getInstance();
        this.auth= FirebaseAuth.getInstance();

        this.adminPreferences = new MyAdminPreferences(context);
        this.customerPreferences = new MyCustomerPreferences(context);
        this.pharmacyPreferences = new MyPharmacyPreferences(context);

        this.resources=context.getResources();
    }

    public Admin loginAdmin(String email, String password){
        admin = new Admin();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    getAdminReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            admin = dataSnapshot.getValue(Admin.class);
                            adminPreferences.saveAdmin(admin);
                            adminPreferences.setLogin(true);
                            Login.getAdminLoginData();
                            Login.progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Login.progressBar.setVisibility(View.INVISIBLE);
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility, task.getException().getLocalizedMessage());
                }
            }
        });

        return admin;
    }

    public Pharmacy loginPharmacy(String email, String password){
        pharmacy = new Pharmacy();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();
                    if (user.isEmailVerified()) {

                        getPharmacyReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    pharmacy = new Pharmacy();
                                    pharmacy.setId(snapShot.getKey());
                                    if (pharmacy.getId().equals(auth.getCurrentUser().getUid())) {

                                        pharmacy.setName(snapShot.child("name").getValue().toString());
                                        pharmacy.setAddress(snapShot.child("address").getValue().toString());
                                        pharmacy.setEmail(snapShot.child("email").getValue().toString());
                                        pharmacy.setContact(snapShot.child("contact").getValue().toString());
                                        pharmacy.setPassword(snapShot.child("password").getValue().toString());
                                        pharmacy.setCity(snapShot.child("city").getValue().toString());
                                        pharmacy.setImage(snapShot.child("image").getValue().toString());
                                        pharmacy.setStockItemList(new ArrayList<>());

//                                        List<StockItem> stockItemList = new ArrayList<>();
//                                        for (DataSnapshot snap:snapShot.child("stockItemList").getChildren()) {
//                                            StockItem stockItem = snap.getValue(StockItem.class);
//                                            stockItemList.add(stockItem);
//                                        }

//                                        pharmacy.setStockItemList(stockItemList);

                                        pharmacyPreferences.savePharmacy(pharmacy);
                                        pharmacyPreferences.setLogin(true);
                                        Login.getPharmacyLoginData();
                                        Login.progressBar.setVisibility(View.INVISIBLE);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Utilities.createAlertDialog(context, "Error", "Please verify this email first");
                        Login.progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Login.progressBar.setVisibility(View.INVISIBLE);
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility, task.getException().getLocalizedMessage());

                }
            }
        });

        return pharmacy;
    }

    public Customer loginCustomer(String email, String password){
        customer = new Customer();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();
                    if (user.isEmailVerified()) {

                        getCustomersReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    customer = snapShot.getValue(Customer.class);
                                    customer.setId(snapShot.getKey());
                                    if (customer.getId().equals(auth.getCurrentUser().getUid())) {
                                        customerPreferences.saveCustomer(customer);
                                        customerPreferences.setLogin(true);
                                        Login.getCustomerLoginData();
                                        Login.progressBar.setVisibility(View.INVISIBLE);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Utilities.createAlertDialog(context, "Error", "Please verify this email first");
                        Login.progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Login.progressBar.setVisibility(View.INVISIBLE);
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility, task.getException().getLocalizedMessage());

                }
            }
        });

        return customer;
    }

    public DatabaseReference get_reference_level_1(String child1){
        return reference.child(child1);
    }
    public DatabaseReference getAdminReference(){
        return get_reference_level_1(Utilities.node_admin);
    }
    public DatabaseReference getPharmacyReference(){
        return get_reference_level_1(Utilities.node_pharmacies);
    }
    public DatabaseReference getCustomersReference(){
        return get_reference_level_1(Utilities.node_customers);
    }
    public DatabaseReference getMedicinesReference(){
        return get_reference_level_1(Utilities.node_medicine);
    }
    public DatabaseReference getOrdersReference(){
        return get_reference_level_1(Utilities.node_orders);
    }
    public DatabaseReference getCartReference(){
        return get_reference_level_1(Utilities.node_cart);
    }
    public DatabaseReference getComplaintsReference(){
        return get_reference_level_1(Utilities.node_complaints);
    }

    public DatabaseReference getStockItemsReference(){
        return get_reference_level_1(Utilities.node_stock_items);
    }
}
