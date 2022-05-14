package com.fyp22.medkitpharmacy.Customer.Medicine;

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
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.StockItem;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMedicine extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    MedicineAdapter adapter;
    ImageView itemIcon;
    TextView noItems;

    Customer customer;
    List<StockItem> stockItemList;
    List<Medicine> medicineListAll;
    List<Medicine> medicineList;
    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);

        radioGroup =(RadioGroup)findViewById(R.id.radio_group);

        itemIcon=findViewById(R.id.itemIcon);
        noItems=findViewById(R.id.no_items_title);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        progressBar.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle("Items");

        medicineListAll =new ArrayList<>();
        stockItemList =new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        databaseHandler= new DatabaseHandler(this);
        mcp = new MyCustomerPreferences(this);
        customer = mcp.getCustomer();

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


                            if(medicineListAll.isEmpty()){
                                recyclerView.setVisibility(View.GONE);
                                noItems.setVisibility(View.VISIBLE);
                            }else{
                                updateRecyclerView();
                            }
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

        recyclerView.setVisibility(View.VISIBLE);

        adapter= new MedicineAdapter(ViewMedicine.this, medicineListAll, stockItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        medicineList =new ArrayList<>();
        adapter= new MedicineAdapter(ViewMedicine.this, medicineList,stockItemList);
        recyclerView.setAdapter(adapter);

        if(radioButton.getText().toString().equals(Utilities.type_all)){
//            itemIcon.setImageResource(R.drawable.donate);
            for (Medicine product : medicineListAll) {
                medicineList.add(product);
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_tablets)) {
//            itemIcon.setImageResource(R.drawable.shoes);
            for (Medicine product : medicineListAll) {
                if (product.getType().equals(Utilities.type_tablets)) {
                    medicineList.add(product);
                }
            }
        }
        else if(radioButton.getText().toString().equals(Utilities.type_syrups)) {
//            itemIcon.setImageResource(R.drawable.clothes);
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
        startActivity(new Intent(ViewMedicine.this, Dashboard.class));
        finish();
    }
}