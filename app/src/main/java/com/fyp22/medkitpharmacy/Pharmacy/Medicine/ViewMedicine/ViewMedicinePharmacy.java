package com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.fyp22.medkitpharmacy.Pharmacy.Dashboard.DashboardPharmacy;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Select.SelectMedicine;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMedicinePharmacy extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ImageView addBtn;
    ImageView itemIcon;
    FloatingActionButton fab;
    SearchView searchView;
    RelativeLayout table_header;

    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    List<StockItem> stockItemListAll;
    List<StockItem> stockItemList;
    RecyclerView recyclerView;
    MedicineAdapterPharmacy adapter;
    DatabaseHandler databaseHandler;

    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine_pharmacy);
        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        fab =findViewById(R.id.fab);
        addBtn=findViewById(R.id.add_btn);
        searchView=findViewById(R.id.search);
        itemIcon=findViewById(R.id.itemIcon);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        relativeLayout=findViewById(R.id.add_rl);
        table_header = findViewById(R.id.table_header);

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        table_header.setVisibility(View.INVISIBLE);

        mphp= new MyPharmacyPreferences(this);
        pharmacy=mphp.getPharmacy();

        stockItemListAll = new ArrayList<>();
        stockItemList = new ArrayList<>();
        medicineListAll =new ArrayList<>();
        medicineList =new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        databaseHandler= new DatabaseHandler(this);

        databaseHandler.getStockItemsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    StockItem stockItem = snapshot.getValue(StockItem.class);
                    if (stockItem.getPharmacyId().equals(pharmacy.getId())){
                        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                                        Medicine medicine = childSnapShot.getValue(Medicine.class);
                                        if (medicine.getId().equals(stockItem.getMedicineId())){
                                            medicineListAll.add(medicine);
                                            medicineList.add(medicine);
                                            stockItemListAll.add(stockItem);
                                            stockItemList.add(stockItem);
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                System.out.println("LIST SIZE:"+medicineListAll.size());
                                if(medicineListAll.size() == 0){
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }else{
                                    table_header.setVisibility(View.VISIBLE);
                                    updateRecyclerView();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("LIST SIZE:"+medicineListAll.size());
                if(medicineListAll.size() == 0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    table_header.setVisibility(View.VISIBLE);
                    updateRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Medicine> newList = new ArrayList<>();
                for (Medicine medicine: medicineList){
                    String channelName = medicine.getName().toLowerCase();
                    if (channelName.contains(newText)){
                        newList.add(medicine);
                    }
                }
                adapter.setFilter(newList, stockItemListAll);
                return true;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMedicinePharmacy.this, SelectMedicine.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMedicinePharmacy.this, SelectMedicine.class));
            }
        });
    }

    void updateRecyclerView(){

        relativeLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter= new MedicineAdapterPharmacy(ViewMedicinePharmacy.this, medicineList, stockItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        stockItemList =new ArrayList<>();
        adapter= new MedicineAdapterPharmacy(ViewMedicinePharmacy.this, medicineList, stockItemList);
        recyclerView.setAdapter(adapter);

        if(radioButton.getText().toString().equals(Utilities.type_all)){
            itemIcon.setImageResource(R.drawable.logo);
            for (Medicine medicine : medicineListAll) {
                medicineList.add(medicine);
                for (StockItem stockItem: stockItemListAll){
                    if(stockItem.getMedicineId().equals(medicine.getId())){
                        stockItemList.add(stockItem);
                    }
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_tablets)) {
            itemIcon.setImageResource(R.drawable.tablet);
            for (Medicine medicine : medicineListAll) {
                if (medicine.getType().equals(Utilities.type_tablets)) {
                    medicineList.add(medicine);
                    for (StockItem stockItem: stockItemListAll){
                        if(stockItem.getMedicineId().equals(medicine.getId())){
                            stockItemList.add(stockItem);
                        }
                    }
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_syrups)) {
            itemIcon.setImageResource(R.drawable.syrup);
            for (Medicine medicine : medicineListAll) {
                if (medicine.getType().equals(Utilities.type_syrups)) {
                    medicineList.add(medicine);
                    for (StockItem stockItem: stockItemListAll){
                        if(stockItem.getMedicineId().equals(medicine.getId())){
                            stockItemList.add(stockItem);
                        }
                    }
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_capsules)) {
            itemIcon.setImageResource(R.drawable.capsule);
            for (Medicine medicine : medicineListAll) {
                if (medicine.getType().equals(Utilities.type_capsules)) {
                    medicineList.add(medicine);
                    for (StockItem stockItem: stockItemListAll){
                        if(stockItem.getMedicineId().equals(medicine.getId())){
                            stockItemList.add(stockItem);
                        }
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewMedicinePharmacy.this, DashboardPharmacy.class));
        finish();
    }
}