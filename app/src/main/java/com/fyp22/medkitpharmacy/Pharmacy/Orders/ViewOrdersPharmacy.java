package com.fyp22.medkitpharmacy.Pharmacy.Orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Dashboard.DashboardPharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.tabs.TabLayout;

public class ViewOrdersPharmacy extends AppCompatActivity {

    Pharmacy pharmacy;
    ViewPager viewPager;
    TabLayout tabLayout;
    MyPharmacyOrderesFragmentPagerAdapter adapter;
    MyPharmacyPreferences mphp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_pharmacy);

        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mphp = new MyPharmacyPreferences(this);
        pharmacy = mphp.getPharmacy();

        tabLayout = findViewById(R.id.ordered_items_tab_layout);
        viewPager = findViewById(R.id.ordered_items_view_pager);

        adapter = new MyPharmacyOrderesFragmentPagerAdapter(getSupportFragmentManager(), pharmacy, this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        // Define listeners
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener()
                {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewOrdersPharmacy.this, DashboardPharmacy.class));
        finish();
    }
}