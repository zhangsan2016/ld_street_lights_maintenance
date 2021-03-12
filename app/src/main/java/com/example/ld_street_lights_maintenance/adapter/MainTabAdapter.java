package com.example.ld_street_lights_maintenance.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ld_street_lights_maintenance.act.BuleFragment;
import com.example.ld_street_lights_maintenance.act.MapFragment;
import com.example.ld_street_lights_maintenance.act.NfcFragment;
import com.example.ld_street_lights_maintenance.base.BaseFragment;


public class MainTabAdapter extends FragmentStatePagerAdapter {

    public MainTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        BaseFragment baseFragment = null;
        switch (position) {
            case 0:
                baseFragment = new BuleFragment();
                break;
            case 1:
                baseFragment = new NfcFragment();
                break;
            case 2:
                baseFragment = new MapFragment();

        }
        return baseFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }





}
