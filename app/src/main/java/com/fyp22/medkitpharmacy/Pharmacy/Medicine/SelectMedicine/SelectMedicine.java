package com.fyp22.medkitpharmacy.Pharmacy.Medicine.SelectMedicine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.ViewMedicinePharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectMedicine extends AppCompatActivity implements SelectListener {

    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SelectMedicineAdapter adapter;
    Button finish;
    ImageView itemIcon;
    EditText available;

    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    List<StockItem> selectedItemList;
    DatabaseHandler databaseHandler;
    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_medicine);
        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        finish =findViewById(R.id.finish);
        itemIcon=findViewById(R.id.itemIcon);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        available = findViewById(R.id.amount_available);


        progressBar.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle("Medicines");

        medicineListAll =new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

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
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);

                if(medicineListAll.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }else{
                    updateRecyclerView();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (StockItem stockItem: selectedItemList) {
                    if (stockItem.getAvailableAmount() == 0) {
                        Toast.makeText(SelectMedicine.this, "Available Amount at Element " + Integer.toString(selectedItemList.indexOf(stockItem) + 1) + " Cannot be 0", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                for (StockItem stockItem: selectedItemList) {
                    progressBar.setVisibility(View.VISIBLE);
                    databaseHandler.getStockItemsReference().push().setValue(stockItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                databaseHandler.getStockItemsReference().addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        StockItem stockItem1 = snapshot.getValue(StockItem.class);
                                        stockItem1.setId(snapshot.getKey());
                                        databaseHandler
                                                .getStockItemsReference()
                                                .child(stockItem1.getId())
                                                .setValue(stockItem1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent intent = new Intent(SelectMedicine.this, ViewMedicinePharmacy.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                Utilities.createAlertDialog(SelectMedicine.this,"Error",task.getException().getLocalizedMessage().toString());
                            }
                        }
                    });
                }
            }
        });
    }

    void updateRecyclerView(){

        recyclerView.setVisibility(View.VISIBLE);
        adapter= new SelectMedicineAdapter(SelectMedicine.this, medicineListAll,this::onSelect);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        adapter= new SelectMedicineAdapter(SelectMedicine.this, medicineList, this::onSelect);
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

    @Override
    public void onSelect(List<StockItem> arrayList) {
        this.selectedItemList =arrayList;
    }
}