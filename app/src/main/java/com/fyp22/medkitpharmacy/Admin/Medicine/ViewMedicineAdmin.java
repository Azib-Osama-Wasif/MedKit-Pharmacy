package com.fyp22.medkitpharmacy.Admin.Medicine;

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

import com.fyp22.medkitpharmacy.Admin.Dashboard.DashboardAdmin;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMedicineAdmin extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    MedicineAdapterAdmin adapter;
    RelativeLayout relativeLayout;
    RelativeLayout tableHeader;
    ImageView addBtn;
    ImageView itemIcon;
    FloatingActionButton fab;
    SearchView searchView;

    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine_admin);
        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        fab =findViewById(R.id.fab);
        addBtn=findViewById(R.id.add_btn);

        itemIcon=findViewById(R.id.itemIcon);
        tableHeader=findViewById(R.id.table_header);
        searchView= findViewById(R.id.search);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        relativeLayout=findViewById(R.id.add_rl);

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        tableHeader.setVisibility(View.INVISIBLE);

        medicineListAll =new ArrayList<>();
        medicineList =new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        databaseHandler= new DatabaseHandler(this);
        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Medicine medicine = childSnapShot.getValue(Medicine.class);
                        medicineListAll.add(medicine);
                        medicineList.add(medicine);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);

                if(medicineListAll.isEmpty()){
                    relativeLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    updateRecyclerView();
                    tableHeader.setVisibility(View.VISIBLE);
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
                adapter.setFilter(newList);
                return true;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMedicineAdmin.this, AddMedicine.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewMedicineAdmin.this, AddMedicine.class));
            }
        });
    }

    void updateRecyclerView(){

        relativeLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter= new MedicineAdapterAdmin(ViewMedicineAdmin.this, medicineList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        adapter= new MedicineAdapterAdmin(ViewMedicineAdmin.this, medicineList);
        recyclerView.setAdapter(adapter);

        if(radioButton.getText().toString().equals(Utilities.type_all)){
            itemIcon.setImageResource(R.drawable.logo);
            for (Medicine product : medicineListAll) {
                medicineList.add(product);
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_tablets)) {
            itemIcon.setImageResource(R.drawable.tablet);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_tablets)) {
                    medicineList.add(product);
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_syrups)) {
            itemIcon.setImageResource(R.drawable.syrup);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_syrups)) {
                    medicineList.add(product);
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_capsules)) {
            itemIcon.setImageResource(R.drawable.capsule);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_capsules)) {
                    medicineList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewMedicineAdmin.this, DashboardAdmin.class));
        finish();
    }
}