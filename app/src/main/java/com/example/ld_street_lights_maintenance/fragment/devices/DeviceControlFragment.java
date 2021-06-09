package com.example.ld_street_lights_maintenance.fragment.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;


public class DeviceControlFragment extends Fragment {
    private DeviceLampJson.DataBean device = null;

    public static DeviceControlFragment getInstance(DeviceLampJson.DataBean device) {
        DeviceControlFragment sf = new DeviceControlFragment();
        sf.device = device;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_control, null);

        initView(v);

        return v;
    }

    private void initView(View v) {

        if (device != null) {
            IndicatorSeekBar sb_quickly_adjustable_lamp =v.findViewById(R.id.sb_quickly_adjustable_lamp);
            sb_quickly_adjustable_lamp.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                    LogUtil.e("xx seekBar = " + seekBar.getProgress());



                }
            });


        }



    }
}