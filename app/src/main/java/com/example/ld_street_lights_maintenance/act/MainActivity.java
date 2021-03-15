package com.example.ld_street_lights_maintenance.act;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.adapter.MainTabAdapter;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private RadioGroup mTabRadioGroup;
    private ArrayList<View> viewList;
    private View blueView, nfcView, mapView;
    private ViewPager viewPager;
    private TabHost tabHost;

    private RadioButton rb_order_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);


        initView();

        // 设置 getViewPager 添加适配器
        MainTabAdapter adapter = new MainTabAdapter(this.getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);


    }


    private void initView() {

        /// 初始化 tab 切换的 View 界面
        initViewPager();

        /// 初始化头部Tab
        initViewTab();

        /// 初始化底部Tab
        mTabRadioGroup = findViewById(R.id.tabs_rg);
      //  mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

         rb_order_tab = findViewById(R.id.order_tab);
        rb_order_tab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e("x", "isChecked = " + rb_order_tab.isChecked() );
                Log.e("x", "isSelected = " + rb_order_tab.isSelected() );


            }
        });




    }

    private void initViewTab() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        //初始化TabHost容器
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BLUE").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("NFC").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("MAP").setContent(R.id.tab3));


        //TabHost的监听事件
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.e("x", ">>>>>>>>>>>>>>>>>>>>>>>>>> onTabChanged tabId = " + tabId);
                if (tabId.equals("tab1")) {
                    viewPager.setCurrentItem(0);
                } else if (tabId.equals("tab2")) {
                    viewPager.setCurrentItem(1);
                } else if (tabId.equals("tab3")) {
                    viewPager.setCurrentItem(2);
                }
            }
        });


    }



    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("x", ">>>>>>>>>>>>>>>>>>>>>>>>>> 当前position = " + position);
                if (tabHost != null) {
                    tabHost.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("x", ">>>>>>>>>>>>>>>>>>>>>>>>>> onPageScrollStateChanged = " + state);
            }
        });
        LayoutInflater inflater = LayoutInflater.from(this);
        blueView = inflater.inflate(R.layout.layout_blue, null);
        nfcView = inflater.inflate(R.layout.layout_nfc, null);
        mapView = inflater.inflate(R.layout.layout_map, null);

        viewList = new ArrayList<View>();
        viewList.add(blueView);
        viewList.add(nfcView);
        viewList.add(mapView);
    }

    /**
     * 底部 tab 点击监听
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            Log.e("x", "当前id = " + checkedId);
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