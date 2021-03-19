package com.example.ld_street_lights_maintenance.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.adapter.MainTabAdapter;
import com.example.ld_street_lights_maintenance.view.ChoosPhotoPopupUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mTabRadioGroup;
    private ViewPager viewPager;
    private TabHost tabHost;

    private RadioButton rb_order_tab;

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

        rb_order_tab = findViewById(R.id.order_tab);
        rb_order_tab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("x", "isChecked = " + rb_order_tab.isChecked());
                Log.e("x", "isSelected = " + rb_order_tab.isSelected());


                ChoosPhotoPopupUtils mPop = new ChoosPhotoPopupUtils(MainActivity.this, 350, "");
                mPop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                mPop.setClippingEnabled(false);
                mPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                mPop.setOnItemClickListener(new ChoosPhotoPopupUtils.OnItemClickListener() {
                    @Override
                    public void setOnItemClick(View v, int code, String path) {

                    }
                });
                    // 设置PopupWindow中的位置
                mPop.showAtLocation(rb_order_tab, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 350);//此处是具体展示的位置和界面的设置，在此偷个懒，具体自行百度咯。

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_toolbar_menu,menu);
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
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            if (i == 0)
            {
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#039A9A"));
                TextView tv =  tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);;//非选择的背景
                tv.setTextColor(Color.parseColor("#000000"));
            } else {
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#41C7DB"));
                TextView tv =  tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);;//非选择的背景
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

                for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
                {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.cyan_039A9A)); //unselected
                    // 设置字体颜色
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#41C7DB")); // selected
                TextView tv =  tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);;//非选择的背景
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


}