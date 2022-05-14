package com.fyp22.medkitpharmacy.Pharmacy.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Common.ViewProfilePharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Customers.ViewCustomersPharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Medicine.ViewMedicine.ViewMedicinePharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Orders.ViewOrdersPharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Sales.ViewSales;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardPharmacy extends AppCompatActivity {

    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CardView medicineCard;
    CardView customerCard;
    CardView ordersCard;
    CardView salesCard;
    CircleImageView imageView;
    TextView title;

    MyPharmacyPreferences mphp;
    Pharmacy pharmacy;
    View header;
    TextView headerEmail;
    TextView headerName;
    CircleImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pharmacy);
        mphp = new MyPharmacyPreferences(this);
        pharmacy = mphp.getPharmacy();

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        Picasso.get().load(pharmacy.getImage()).into(headerImage);
        headerName.setText(pharmacy.getName());
        headerEmail.setText(pharmacy.getEmail());

        imageView=findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        Picasso.get().load(pharmacy.getImage()).into(imageView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        medicineCard =findViewById(R.id.medicineCard);
        ordersCard =findViewById(R.id.ordersCard);
        customerCard =findViewById(R.id.customersCard);
        salesCard =findViewById(R.id.salesCard);

        title.setText("Hello "+pharmacy.getName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardPharmacy.this, ViewProfilePharmacy.class));
            }
        });

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                if (menuItem.getItemId() == R.id.home) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        finish();
                        startActivity(new Intent(DashboardPharmacy.this, DashboardPharmacy.class));
                    }
                }

                if (menuItem.getItemId() == R.id.profile) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(DashboardPharmacy.this, ViewProfilePharmacy.class));
                    }
                }

                if (menuItem.getItemId() == R.id.orders) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(DashboardPharmacy.this, ViewOrdersPharmacy.class));
                    }
                }

                if (menuItem.getItemId() == R.id.sales) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(DashboardPharmacy.this, ViewSales.class));
                    }
                }

                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Utilities.createLogoutDialog(DashboardPharmacy.this, Utilities.user_pharmacy);

                    }
                }
                return false;
            }
        });

        medicineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardPharmacy.this, ViewMedicinePharmacy.class));
            }
        });
        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardPharmacy.this, ViewCustomersPharmacy.class));
            }
        });
        salesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardPharmacy.this, ViewSales.class));
            }
        });
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardPharmacy.this, ViewOrdersPharmacy.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}