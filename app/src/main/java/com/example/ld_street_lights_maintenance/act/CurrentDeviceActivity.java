package com.example.ld_street_lights_maintenance.act;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.fragment.popwindow.SimpleCardFragment;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

public class CurrentDeviceActivity extends BaseActivity {
    private MyPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private DeviceLampJson.DataBean device;
    private final String[] mTitles = {
            "查看", "控制",
            "历史消息"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.current_device);

        // 获取传递过来的设备参数
        device  = (DeviceLampJson.DataBean) this.getIntent().getSerializableExtra("device_info");

        LogUtil.e("DeviceLampJson.DataBean = " + device.toString());

        // 根据类型显示界面
        if (device.getTYPE() == 1) {
            // 电箱类型


        } else if (device.getTYPE() == 2) {
            // 路灯类型

        }

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }

        SlidingTabLayout st_device = this.findViewById(R.id.st_device);
        ViewPager vp = this.findViewById(R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        st_device.setViewPager(vp);


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
