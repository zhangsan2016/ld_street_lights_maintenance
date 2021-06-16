package com.example.ld_street_lights_maintenance.fragment.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;


public class DeviceInfoFragment2 extends Fragment {
    private DeviceLampJson.DataBean device = null;

    public static DeviceInfoFragment2 getInstance(DeviceLampJson.DataBean device) {
        DeviceInfoFragment2 sf = new DeviceInfoFragment2();
        sf.device = device;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_info2, null);

        initView(v);

        return v;
    }

    private void initView(View v) {
        if (device != null) {
            TextView tv_device_info_name = v.findViewById(R.id.tv_device_info_name);
            tv_device_info_name.setText(device.getNAME());
            TextView tv_device_info_UUID = v.findViewById(R.id.tv_device_info_UUID);
            tv_device_info_UUID.setText(device.getUUID());
            TextView tv_device_info_type = v.findViewById(R.id.tv_device_info_type);
            tv_device_info_type.setText(device.getTYPE()+"");
            TextView tv_device_info_longitude = v.findViewById(R.id.tv_device_info_longitude);
            tv_device_info_longitude.setText(device.getLNG());
            TextView tv_device_info_latitude = v.findViewById(R.id.tv_device_info_latitude);
            tv_device_info_latitude.setText(device.getLAT());
            TextView tv_device_info_project = v.findViewById(R.id.tv_device_info_project);
            tv_device_info_project.setText(device.getPROJECT());
            TextView tv_device_info_FUUID = v.findViewById(R.id.tv_device_info_FUUID);
            tv_device_info_FUUID.setText(device.getFUUID());


        }



    }
}