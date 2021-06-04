package com.example.ld_street_lights_maintenance.fragment.devices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.fragment.popwindow.SimpleCardFragment;


public class DeviceInfoFragment extends Fragment {
    private String mTitle;

    public static DeviceInfoFragment getInstance(String title) {
        DeviceInfoFragment sf = new DeviceInfoFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_info, null);


        return v;
    }
}