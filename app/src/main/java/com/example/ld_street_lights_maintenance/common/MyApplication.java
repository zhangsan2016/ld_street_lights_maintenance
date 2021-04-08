package com.example.ld_street_lights_maintenance.common;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.example.ld_street_lights_maintenance.util.SpUtils;

public class MyApplication  extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 在设置文件名参数时，不要带 “.xml” 后缀，android会自动添加
        SpUtils.getInstance(this,"myapp");
        // 初始化MultiDex,解决 methods: 68388 > 65536
        MultiDex.install(this);
    }

    public static MyApplication getInstance(){
        return instance;
    }

}

