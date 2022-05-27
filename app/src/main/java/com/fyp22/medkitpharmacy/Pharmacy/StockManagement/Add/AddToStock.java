package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.List;

public class AddToStock extends AppCompatActivity implements QuantityInputListener{

    ProgressBar progressBar;
    RecyclerView recyclerView;
    AddToStockAdapter adapter;
    Button finish;
    ArrayList<Medicine> medicineList;
    List<StockItem> stockItemList;
    DatabaseHandler databaseHandler;
    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_stock);

        getSupportActionBar().setTitle("Add to Stock");

        finish =findViewById(R.id.finish);
        recyclerView=findViewById(R.id.items_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        progressBar.setVisibility(View.INVISIBLE);

        stockItemList = new ArrayList<>();
        intent=getIntent();
        medicineList=intent.getParcelableArrayListExtra(Utilities.intent_medicine_list);
        mphp= new MyPharmacyPreferences(this);
        pharmacy=mphp.getPharmacy();
        databaseHandler= new DatabaseHandler(this);

        for (int i = 0; i < medicineList.size(); i++) {
            stockItemList.add(new StockItem("", "", "",1));
        }
        updateRecyclerView();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockItem stockItem = new StockItem();
                for (Medicine medicine : medicineList) {
                    stockItem.setMedicineId(medicine.getId());
                    stockItem.setPharmacyId(pharmacy.getId());
                    databaseHandler.getStockItemsReference().push().setValue(stockItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                databaseHandler.getStockItemsReference().addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        StockItem stockItem1 = snapshot.getValue(StockItem.class);
                                        stockItem1.setId(snapshot.getKey().toString());
                                        databaseHandler.getStockItemsReference().child(snapshot.getKey().toString()).setValue(stockItem1);
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
                        }
                    });
                }
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(AddToStock.this, ViewMedicinePharmacy.class));
                finish();
            }
        });

    }

    void updateRecyclerView(){

        recyclerView.setVisibility(View.VISIBLE);
        adapter= new AddToStockAdapter(AddToStock.this,getSupportFragmentManager(), medicineList,stockItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishEditDialog(String inputText, String index) {
        stockItemList.get(Integer.parseInt(index)).setAvailableAmount(Integer.parseInt(inputText));
        updateRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddToStock.this, ViewMedicinePharmacy.class));
        finish();
    }
}