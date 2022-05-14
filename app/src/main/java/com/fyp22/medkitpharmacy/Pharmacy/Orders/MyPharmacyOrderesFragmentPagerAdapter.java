package com.fyp22.medkitpharmacy.Pharmacy.Orders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fyp22.medkitpharmacy.Models.Pharmacy;


public class MyPharmacyOrderesFragmentPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    Pharmacy pharmacy;
    Context context;

    public MyPharmacyOrderesFragmentPagerAdapter(@NonNull FragmentManager fm, Pharmacy pharmacy, Context context) {
        super(fm);
        this.pharmacy = pharmacy;
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewOrdersFragment(pharmacy, context);
            case 1:
                return new DispatchedOrdersFragment(pharmacy, context);
            case 2:
                return new DeliveredOrdersFragment(pharmacy, context);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "New";
            }
            case 1: {
                return "Dispatched";
            }
            case 2: {
                return "Delivered";
            }
        }
        return null;
    }
}
