package com.example.ld_street_lights_maintenance.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.fragment.devices.DeviceControlFragment2;
import com.example.ld_street_lights_maintenance.fragment.devices.DeviceHistoryFragment2;
import com.example.ld_street_lights_maintenance.fragment.devices.DeviceInfoFragment2;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

public class CurrentDeviceActivity extends BaseActivity {
    private MyPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private DeviceLampJson.DataBean device;
    private String[] mTitles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.current_device);

        // 获取传递过来的设备参数
        device = (DeviceLampJson.DataBean) this.getIntent().getSerializableExtra("device_info");

        LogUtil.e("DeviceLampJson.DataBean = " + device.toString());


       initView();

        if(device != null){

            // 根据类型显示界面
            if (device.getTYPE() == 1) {
                // 电箱类型
                mTitles = new String[]{"查看","控制","历史消息"};
                for (String title : mTitles) {

                    if (title.equals("查看")){
                        mFragments.add(DeviceInfoFragment2.getInstance(device));
                    }else if(title.equals("控制")) {
                        mFragments.add(DeviceControlFragment2.getInstance(device));
                    }else if(title.equals("历史消息")){
                        mFragments.add(DeviceHistoryFragment2.getInstance(device));
                    }

                }

            } else if (device.getTYPE() == 2) {
                // 路灯类型
                mTitles = new String[]{"查看","控制","历史消息"};
                for (String title : mTitles) {
                    if (title.equals("查看")){
                        mFragments.add(DeviceInfoFragment2.getInstance(device));
                    }else if(title.equals("控制")) {
                        mFragments.add(DeviceControlFragment2.getInstance(device));
                    }else if(title.equals("历史消息")){
                        mFragments.add(DeviceHistoryFragment2.getInstance(device));
                    }

                }
            }
        }


        SlidingTabLayout st_device = this.findViewById(R.id.st_device);
        ViewPager vp = this.findViewById(R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        st_device.setViewPager(vp);


    }

    private void initView() {
        TextView title_text = this.findViewById(R.id.title_text);
        title_text.setText(device.getNAME());
        Button title_back = this.findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
