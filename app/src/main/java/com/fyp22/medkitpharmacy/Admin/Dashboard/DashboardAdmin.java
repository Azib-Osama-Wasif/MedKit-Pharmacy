package com.fyp22.medkitpharmacy.Admin.Dashboard;

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

import com.fyp22.medkitpharmacy.Admin.Common.ViewProfileAdmin;
import com.fyp22.medkitpharmacy.Admin.Customer.ViewCustomersAdmin;
import com.fyp22.medkitpharmacy.Admin.Medicine.ViewMedicineAdmin;
import com.fyp22.medkitpharmacy.Admin.Pharmacy.ViewPharmaciesAdmin;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Customer.Common.ViewProfile;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Models.Admin;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardAdmin extends AppCompatActivity {

    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CardView medicineCard;
    CardView customerCard;
    CardView complaintsCard;
    CardView pharmaciesCard;

    MyAdminPreferences map;
    Admin admin;
    View header;
    TextView headerEmail;
    TextView headerName;
    CircleImageView headerImage;

    CircleImageView imageView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        map = new MyAdminPreferences(this);
        admin = map.getAdmin();

        imageView=findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        Picasso.get().load(R.drawable.admin).into(imageView);

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        Picasso.get().load(R.drawable.admin).into(headerImage);
        headerName.setText(admin.getName());
        headerEmail.setText(admin.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        medicineCard =findViewById(R.id.medicineCard);
        complaintsCard =findViewById(R.id.complaintsCard);
        customerCard =findViewById(R.id.customersCard);
        pharmaciesCard =findViewById(R.id.pharmacyCard);

        title.setText("Hello "+admin.getName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, ViewProfileAdmin.class));
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
                        startActivity(new Intent(DashboardAdmin.this, DashboardAdmin.class));
                    }
                }

                if (menuItem.getItemId() == R.id.profile) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(DashboardAdmin.this, ViewProfileAdmin.class));
                    }
                }

                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Utilities.createLogoutDialog(DashboardAdmin.this, Utilities.user_admin);

                    }
                }
                return false;
            }
        });

        medicineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, ViewMedicineAdmin.class));
            }
        });
        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, ViewCustomersAdmin.class));
            }
        });
        pharmaciesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, ViewPharmaciesAdmin.class));
            }
        });
        complaintsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(DashboardAdmin.this, ViewCompla.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}