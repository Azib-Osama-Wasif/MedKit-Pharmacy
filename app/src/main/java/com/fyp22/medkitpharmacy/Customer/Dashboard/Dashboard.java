package com.fyp22.medkitpharmacy.Customer.Dashboard;

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

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Cart.CartActivity;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Common.ViewProfile;
import com.fyp22.medkitpharmacy.Customer.Medicine.ViewMedicine;
import com.fyp22.medkitpharmacy.Customer.Order.ViewOrders;
import com.fyp22.medkitpharmacy.Customer.Pharmacies.ViewPharmacies;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CardView itemCard;
    CardView cartCard;
    CardView ordersCard;
    CardView pharmaciesCard;
    CircleImageView imageView;
    TextView title;

    MyCustomerPreferences mcp;
    Customer customer;
    View header;
    TextView headerEmail;
    TextView headerName;
    CircleImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mcp = new MyCustomerPreferences(this);
        customer = mcp.getCustomer();
        imageView=findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        Picasso.get().load(customer.getImage()).into(imageView);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        Picasso.get().load(customer.getImage()).into(headerImage);
        headerName.setText(customer.getName());
        headerEmail.setText(customer.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        itemCard =findViewById(R.id.medicineCard);
        ordersCard=findViewById(R.id.ordersCard);
        cartCard=findViewById(R.id.cartCard);
        pharmaciesCard =findViewById(R.id.pharmacyCard);

        title.setText("Hello "+customer.getName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewProfile.class));
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
                        startActivity(new Intent(Dashboard.this, Dashboard.class));
                    }
                }
                if (menuItem.getItemId() == R.id.cart) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(Dashboard.this, CartActivity.class));
                    }
                }

                if (menuItem.getItemId() == R.id.profile) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(Dashboard.this, ViewProfile.class));
                    }
                }

                if (menuItem.getItemId() == R.id.orders) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(Dashboard.this, ViewOrders.class));
                    }
                }

                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Utilities.createLogoutDialog(Dashboard.this, Utilities.user_customer);

                    }
                }
                return false;
            }
        });

        itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewMedicine.class));
            }
        });
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewOrders.class));
            }
        });
        cartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, CartActivity.class));
            }
        });
        pharmaciesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewPharmacies.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}