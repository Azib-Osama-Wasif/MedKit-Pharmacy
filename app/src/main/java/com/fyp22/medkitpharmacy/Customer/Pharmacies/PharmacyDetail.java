package com.fyp22.medkitpharmacy.Customer.Pharmacies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Medicine.MedicineAdapter;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PharmacyDetail extends AppCompatActivity {

    TextView name;
    TextView city;
    TextView phone;
    ImageView itemIcon;
    CircleImageView imageView;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton radioButton;

    RecyclerView recyclerViewMedicines;
    MedicineAdapter adapter;
    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    List<StockItem> stockItemList;

    Pharmacy pharmacy;
    Intent intent;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_detail);
        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        name=findViewById(R.id.name_val);
        itemIcon=findViewById(R.id.itemIcon);
        city=findViewById(R.id.city_val);
        phone=findViewById(R.id.phone_val);
        imageView=findViewById(R.id.image);
        recyclerViewMedicines=findViewById(R.id.medicines_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        medicineListAll =new ArrayList<>();
        stockItemList =new ArrayList<>();
        recyclerViewMedicines.setLayoutManager(new GridLayoutManager(PharmacyDetail.this,2));

        intent = getIntent();
        pharmacy = (Pharmacy) intent.getSerializableExtra(Utilities.intent_pharmacy);
        getSupportActionBar().setTitle(pharmacy.getName());
        databaseHandler= new DatabaseHandler(this);

        name.setText(pharmacy.getName());
        city.setText(pharmacy.getCity());
        phone.setText(pharmacy.getContact());
        Picasso.get().load(pharmacy.getImage()).into(imageView);
        progressBar.setVisibility(View.VISIBLE);

        databaseHandler.getStockItemsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    StockItem stockItem = snapshot.getValue(StockItem.class);
                    stockItemList.add(stockItem);

                    databaseHandler.getMedicinesReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                                    Medicine medicine = childSnapShot.getValue(Medicine.class);
                                    if (stockItem.getMedicineId().equals(medicine.getId())){
                                        medicineListAll.add(medicine);
                                    }
                                }
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            updateRecyclerView();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void updateRecyclerView(){
        medicineList =new ArrayList<>();
        adapter= new MedicineAdapter(PharmacyDetail.this, medicineList, stockItemList);
        recyclerViewMedicines.setAdapter(adapter);
        recyclerViewMedicines.setLayoutManager(new GridLayoutManager(PharmacyDetail.this,2));

        for (Medicine product : medicineListAll) {
            medicineList.add(product);
        }
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        adapter= new MedicineAdapter(PharmacyDetail.this, medicineList, stockItemList);
        recyclerViewMedicines.setAdapter(adapter);

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
}