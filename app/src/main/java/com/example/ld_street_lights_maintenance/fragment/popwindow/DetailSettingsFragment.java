package com.example.ld_street_lights_maintenance.fragment.popwindow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;


@SuppressLint("ValidFragment")
public class DetailSettingsFragment extends Fragment {
    private String mTitle;

    public static DetailSettingsFragment getInstance(String title) {
        DetailSettingsFragment sf = new DetailSettingsFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_detail_settings, null);

        return v;
    }
}