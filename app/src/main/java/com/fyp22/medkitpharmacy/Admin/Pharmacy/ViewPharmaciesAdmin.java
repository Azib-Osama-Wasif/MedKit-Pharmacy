package com.fyp22.medkitpharmacy.Admin.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.fyp22.medkitpharmacy.Admin.Dashboard.DashboardAdmin;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPharmaciesAdmin extends AppCompatActivity {

    TextView title;
    TextView showAllText;
    TextView cityText;
    DrawerLayout drawerLayout;
    SearchView searchView;

    ProgressBar progressBar;

    RecyclerView recyclerView;
    PharmacyAdapterAdmin adapter;
    List<Pharmacy> pharmacyListAll;
    List<Pharmacy> pharmacyList;

    Pharmacy pharmacy;
    Intent intent;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pharmacies_admin);
        title = findViewById(R.id.title);
        showAllText = findViewById(R.id.show_all);
        searchView=findViewById(R.id.search);
        cityText = findViewById(R.id.nearest);
        recyclerView = findViewById(R.id.stores_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);

        title.setText("No stores in " + cityText.getText().toString());

        pharmacyListAll = new ArrayList<>();
        pharmacyList = new ArrayList<>();
        adapter = new PharmacyAdapterAdmin(this, pharmacyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        intent = getIntent();
        pharmacy = (Pharmacy) intent.getSerializableExtra(Utilities.intent_pharmacy);
        databaseHandler = new DatabaseHandler(this);


        databaseHandler.getPharmacyReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Pharmacy store = snapShot.getValue(Pharmacy.class);
                    pharmacyListAll.add(store);
                    pharmacyList.add(store);
                }
                progressBar.setVisibility(View.GONE);
                if (pharmacyListAll.size() == 0) {
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
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                cityText.setText("Show Nearest");
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

    void updateRecyclerView(){
        adapter= new PharmacyAdapterAdmin(ViewPharmaciesAdmin.this, pharmacyList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showCityPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                cityText.setText(item.getTitle().toString());
                pharmacyList = new ArrayList<>();
                for (Pharmacy pharmacy : pharmacyListAll) {
                    if(pharmacy.getCity().equals(item.getTitle().toString()))
                    pharmacyList.add(pharmacy);
                }
                showAllText.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                updateRecyclerView();
                return true;
            }
        });
        popup.inflate(R.menu.city_popup_menu);
        popup.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewPharmaciesAdmin.this, DashboardAdmin.class));
    }
}