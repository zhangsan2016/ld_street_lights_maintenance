package com.example.ld_street_lights_maintenance.act;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.example.ld_street_lights_maintenance.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mTabRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabHost tab = (TabHost) findViewById(android.R.id.tabhost);

        //初始化TabHost容器
        tab.setup();
        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        tab.addTab(tab.newTabSpec("tab1").setIndicator("BLUE" , null).setContent(R.id.tab1));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("NFC" , null).setContent(R.id.tab2));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("MAP" , null).setContent(R.id.tab3));

        initView();


    }


    private void initView() {
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            Log.e("x","当前id = " + checkedId);
            for (int i = 0; i < group.getChildCount(); i++) {
                Log.e("x", "当前id = " + group.getChildAt(i).getId());
            }
            /*for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }*/
        }
    };




}