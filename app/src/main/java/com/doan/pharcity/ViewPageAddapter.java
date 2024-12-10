package com.doan.pharcity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAddapter extends FragmentStatePagerAdapter {
    public ViewPageAddapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPageAddapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new home_Fragment();
            case 1:
                return new Shopping_cart_Fragment();
            case 2:
                return new Support_Fragment();
            case 3:
                return new Account_Fragment();
            default:
                return new home_Fragment();
        }
    }
}
