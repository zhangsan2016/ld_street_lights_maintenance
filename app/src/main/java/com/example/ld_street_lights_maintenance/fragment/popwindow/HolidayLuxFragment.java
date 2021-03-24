package com.example.ld_street_lights_maintenance.fragment.popwindow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;


@SuppressLint("ValidFragment")
public class HolidayLuxFragment extends Fragment {
    private String mTitle;

    public static HolidayLuxFragment getInstance(String title) {
        HolidayLuxFragment sf = new HolidayLuxFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_holiday_lux, null);

        return v;
    }
}