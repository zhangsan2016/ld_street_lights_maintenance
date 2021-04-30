package com.example.ld_street_lights_maintenance.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.adapter.MainTabAdapter;
import com.example.ld_street_lights_maintenance.fragment.mainfragment.NfcFragment;
import com.example.ld_street_lights_maintenance.util.DensityUtil;
import com.example.ld_street_lights_maintenance.view.LightingPlanningPopupUtils;
import com.example.ld_street_lights_maintenance.view.OrderPhotoPopupUtils;
import com.example.ld_street_lights_maintenance.view.SettingsPopupUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mTabRadioGroup;
    private ViewPager viewPager;
    private TabHost tabHost;

    private RadioButton rb_order_tab, rb_lighting_planning_tab, rb_settings_tab;
    private OrderPhotoPopupUtils orderPop;
    private List<PopupWindow> popWindows = new ArrayList<>();

    // 当前蓝牙设备
    private BleDevice bleDevice = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        setContentView(R.layout.activity_main);

        initView();


    }




    /// static private NFCTag mTag;
    static private Tag mTag;
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取Tag对象,传递到
        ((NfcFragment) ((MainTabAdapter)viewPager.getAdapter()).getItem(1)).onNewIntent();

    }


    private void initView() {

        /// 设置 toobar
        setToobar();

        /// 初始化 tab 切换的 View 界面
        initViewPager();

        /// 初始化头部Tab
        initViewTab();

        /// 初始化底部Tab
        bottomTab();


    }

    private void bottomTab() {
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        //  mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        orderPop = new OrderPhotoPopupUtils(MainActivity.this);
        orderPop.setOnItemClickListener(new OrderPhotoPopupUtils.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v, int code, String path) {
                orderPop.dismiss();
            }
        });
        popWindows.add(orderPop);


        rb_order_tab = findViewById(R.id.order_tab);
        rb_order_tab.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Log.e("x", "isChecked = " + rb_order_tab.isChecked());
                Log.e("x", "isSelected = " + rb_order_tab.isSelected());

                showPopWindow(rb_order_tab,0);

            }
        });


        LightingPlanningPopupUtils  lighting_planning_Pop = new LightingPlanningPopupUtils(MainActivity.this);
        lighting_planning_Pop.setOnItemClickListener(new LightingPlanningPopupUtils.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v, int code, String path) {
            }
        });
        popWindows.add(lighting_planning_Pop);


        rb_lighting_planning_tab = findViewById(R.id.lighting_planning_tab);
        rb_lighting_planning_tab.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Log.e("x", "isChecked = " + rb_lighting_planning_tab.isChecked());
                Log.e("x", "isSelected = " + rb_lighting_planning_tab.isSelected());
                showPopWindow(rb_lighting_planning_tab,1);
            }
        });


        SettingsPopupUtils settingsPop = new SettingsPopupUtils(MainActivity.this);
        settingsPop.setOnItemClickListener(new SettingsPopupUtils.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v, int code, String path) {

            }
        });
        popWindows.add(settingsPop);

        rb_settings_tab = findViewById(R.id.settings_tab);
        rb_settings_tab.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Log.e("x", "isChecked = " + rb_settings_tab.isChecked());
                Log.e("x", "isSelected = " + rb_settings_tab.isSelected());
                showPopWindow(rb_settings_tab,2);
            }
        });

    }

    private void showPopWindow(RadioButton rb , int index) {

        for (int i = 0; i < popWindows.size(); i++) {
            PopupWindow pop = popWindows.get(i);
            if(index == i){
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    // 设置PopupWindow中的位置
                   // pop.showAtLocation(rb, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, rb.getMeasuredHeight());
                    pop.showAtLocation(rb, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtil.getNavigationBarHeight(this) + rb.getMeasuredHeight());
                }
            }else{
                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setToobar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewTab() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        //初始化TabHost容器
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BLUE").setContent(R.id.tv_blue_tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("NFC").setContent(R.id.tv_blue_tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("MAP").setContent(R.id.tv_blue_tab3));


        // 初始化 tabHost 颜色
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            if (i == 0) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#41C7DB"));
                TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                //非选择的背景
                tv.setTextColor(Color.parseColor("#000000"));
            } else {
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#039A9A"));
                TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                ;//非选择的背景
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        }


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

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.cyan_039A9A)); //unselected
                    // 设置字体颜色
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#41C7DB")); // selected
                TextView tv = tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
                ;//非选择的背景
                tv.setTextColor(Color.parseColor("#000000"));

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


        // 设置 getViewPager 添加适配器
        MainTabAdapter adapter = new MainTabAdapter(this.getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
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


    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }



}