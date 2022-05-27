package com.fyp22.medkitpharmacy.Customer.Order;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyOrderesFragmentPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public MyOrderesFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingOrdersFragment();
            case 1:
                return new ReceivedOrdersFragment();

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
                return "Pending";
            }
            case 1: {
                return "Received";
            }
        }
        return null;
    }
}
