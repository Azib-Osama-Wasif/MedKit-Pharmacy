package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Select;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Add.AddToStock;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.ViewMedicinePharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectMedicine extends AppCompatActivity{

    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SelectMedicineAdapter adapter;
    Button finish;
    ImageView itemIcon;
    TextView available;
    static TextView totalSelected;

    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    ArrayList<Medicine> selectedMedicine;
    DatabaseHandler databaseHandler;
    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_medicine);

        getSupportActionBar().setTitle("Select Medicine");

        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        finish =findViewById(R.id.finish);
        itemIcon=findViewById(R.id.itemIcon);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        available = findViewById(R.id.amount_available);
        totalSelected = findViewById(R.id.cartNum);

        progressBar.setVisibility(View.VISIBLE);

        medicineListAll =new ArrayList<>();
        medicineList =new ArrayList<>();
        selectedMedicine = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        totalSelected.setText(Integer.toString(selectedMedicine.size()));

        mphp= new MyPharmacyPreferences(this);
        pharmacy=mphp.getPharmacy();
        databaseHandler= new DatabaseHandler(this);
        databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Medicine medicine = childSnapShot.getValue(Medicine.class);
                        medicineListAll.add(medicine);
                        medicineList.add(medicine);
                        databaseHandler.getStockItemsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                    StockItem stockItem = snapshot1.getValue(StockItem.class);
                                    //checking if a medicine already exists in pharmacy stock, then it will not be displayed for selection
                                    if(stockItem.getPharmacyId().equals(pharmacy.getId()) && stockItem.getMedicineId().equals(medicine.getId())){
                                        medicineListAll.remove(medicine);
                                        medicineList.remove(medicine);
                                    }
                                }
                                updateRecyclerView();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedMedicine.size()>0){
                    startActivity(new Intent(SelectMedicine.this, AddToStock.class).putParcelableArrayListExtra(Utilities.intent_medicine_list,selectedMedicine));
                }
                else{
                    Toast.makeText(SelectMedicine.this, "NO MEDICINE SELECTED", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void updateRecyclerView(){

        recyclerView.setVisibility(View.VISIBLE);
        adapter= new SelectMedicineAdapter(SelectMedicine.this,getSupportFragmentManager(), medicineList, selectedMedicine);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        adapter= new SelectMedicineAdapter(SelectMedicine.this,getSupportFragmentManager(), medicineList, selectedMedicine);
        recyclerView.setAdapter(adapter);

        if(radioButton.getText().toString().equals(Utilities.type_all)){
            itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
            for (Medicine product : medicineListAll) {
                medicineList.add(product);
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_tablets)) {
            itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_tablets)) {
                    medicineList.add(product);
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_syrups)) {
            itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_syrups)) {
                    medicineList.add(product);
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_capsules)) {
            itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
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
        startActivity(new Intent(SelectMedicine.this, ViewMedicinePharmacy.class));
        finish();
    }
}