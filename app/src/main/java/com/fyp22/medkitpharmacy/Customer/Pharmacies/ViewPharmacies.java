package com.fyp22.medkitpharmacy.Customer.Pharmacies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPharmacies extends AppCompatActivity {

    TextView title;
    TextView cityTitle;
    TextView showAllText;
    TextView nearestBtn;
    DrawerLayout drawerLayout;
    SearchView searchView;

    ProgressBar progressBar;

    RecyclerView recyclerView;
    PharmacyAdapter adapter;
    List<Pharmacy> pharmacyListAll;
    List<Pharmacy> pharmacyList;

    Pharmacy pharmacy;
    Intent intent;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pharmacies);

        title=findViewById(R.id.title);
        searchView = findViewById(R.id.search);
        cityTitle=findViewById(R.id.city_title);
        showAllText =findViewById(R.id.show_all);
        nearestBtn =findViewById(R.id.nearest);
        recyclerView =findViewById(R.id.stores_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);

        title.setText("No stores in "+ MyCustomerPreferences.getCustomer().getCity());
        cityTitle.setText("All Pharmacies");

        pharmacyListAll = new ArrayList<>();
        pharmacyList = new ArrayList<>();
        adapter = new PharmacyAdapter(this, pharmacyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        intent = getIntent();
        pharmacy = (Pharmacy) intent.getSerializableExtra(Utilities.intent_pharmacy);
        databaseHandler= new DatabaseHandler(this);


        databaseHandler.getPharmacyReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Pharmacy store = snapShot.getValue(Pharmacy.class);
                    pharmacyListAll.add(store);
                    pharmacyList.add(store);
                }
                progressBar.setVisibility(View.GONE);
                if(pharmacyListAll.size()==0){
                    title.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pharmacyList = new ArrayList<>();
                for (Pharmacy store : pharmacyListAll) {
                    pharmacyList.add(store);
                }
                showAllText.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                cityTitle.setText("All Pharmacies");
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                nearestBtn.setText("Show Nearest");
            }
        });

        nearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nearestBtn.getText().toString().equals("Show Nearest")){
                    cityTitle.setText("Pharmacies in "+MyCustomerPreferences.getCustomer().getCity());
                    pharmacyList = new ArrayList<>();
                    for (Pharmacy pharmacy : pharmacyListAll) {
                        if (pharmacy.getCity().equals(MyCustomerPreferences.getCustomer().getCity())) {
                            pharmacyList.add(pharmacy);
                        }
                    }
                    if(pharmacyList.size()==0){
                        title.setVisibility(View.VISIBLE);
                        showAllText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else{
                        adapter = new PharmacyAdapter(ViewPharmacies.this, pharmacyList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(ViewPharmacies.this, 2));
                        adapter.notifyDataSetChanged();
                    }

                    Snackbar snackbar = Snackbar.make(drawerLayout, "Showing Pharmacies in "+MyCustomerPreferences.getCustomer().getCity(), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (Pharmacy store : pharmacyListAll) {
                                pharmacyList.add(store);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    snackbar.show();
                    nearestBtn.setText("Show All");
                }
                else if(nearestBtn.getText().toString().equals("Show All")){
                    pharmacyList = new ArrayList<>();
                    for (Pharmacy store : pharmacyListAll) {
                        pharmacyList.add(store);
                    }
                    showAllText.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    cityTitle.setText("All Pharmacies");
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    nearestBtn.setText("Show Nearest");
                }
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
                ArrayList<Pharmacy> newList = new ArrayList<>();
                for (Pharmacy pharmacy: pharmacyList){
                    String channelName = pharmacy.getName().toLowerCase();
                    if (channelName.contains(newText)){
                        newList.add(pharmacy);
                    }
                }
                adapter.setFilter(newList);
                return true;
            }
        });
    }

//    void updateRecyclerView(){
//        pharmacyList =new ArrayList<>();
//        adapter= new PharmacyAdapter(ViewPharmacies.this, pharmacyList);
//        recyclerView.setAdapter(adapter);
//
//        for (Pharmacy store : pharmacyListAll) {
//            pharmacyList.add(store);
//        }
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewPharmacies.this, Dashboard.class));
    }
}