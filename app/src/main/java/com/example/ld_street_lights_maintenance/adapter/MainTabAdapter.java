package com.example.ld_street_lights_maintenance.adapter;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ld_street_lights_maintenance.act.BuleFragment;
import com.example.ld_street_lights_maintenance.act.MapFragment;
import com.example.ld_street_lights_maintenance.act.NfcFragment;
import com.example.ld_street_lights_maintenance.base.BaseFragment;

import java.util.ArrayList;


public class MainTabAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragments;
    private BaseFragment baseFragment = null;

    public MainTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments = new ArrayList<BaseFragment>();
        fragments.add( new BuleFragment());
        fragments.add( new NfcFragment());
        fragments.add( new MapFragment());
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                baseFragment = fragments.get(0);

           //     baseFragment = new BuleFragment();
                break;
            case 1:
                baseFragment =  fragments.get(1);
            //    baseFragment = new NfcFragment();
                break;
            case 2:
                baseFragment =  fragments.get(2);
             //   baseFragment = new MapFragment();

        }
        return baseFragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }





}
