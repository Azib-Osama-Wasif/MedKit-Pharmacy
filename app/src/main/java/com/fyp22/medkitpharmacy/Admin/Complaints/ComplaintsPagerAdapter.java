package com.fyp22.medkitpharmacy.Admin.Complaints;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ComplaintsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public ComplaintsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CustomerComplaintsFragment();
            case 1:
                return new PharmacyComplaintsFragment();

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
                return "CUSTOMER";
            }
            case 1: {
                return "PHARMACY";
            }
        }
        return null;
    }
}
