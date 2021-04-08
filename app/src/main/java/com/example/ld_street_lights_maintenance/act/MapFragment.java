package com.example.ld_street_lights_maintenance.act;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseFragment;

import java.io.IOException;
import java.io.InputStream;

public class MapFragment extends BaseFragment {
    MapView mMapView = null;
    private AMap aMap;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_map,
                container, false);
        mContext = this.getActivity();

        initAMap(savedInstanceState,rootView);

        return rootView;
    }

    private void initAMap(Bundle savedInstanceState, View rootView) {

        //获取地图控件引用
        mMapView = (MapView) rootView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        // 设置地图样式
        aMap.setCustomMapStyle(
                new com.amap.api.maps.model.CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleData(getAssetsStyle(mContext,"style.data"))
                        .setStyleExtraData(getAssetsStyle(mContext,"style_extra.data"))
        );

    }



    private static byte[] getAssetsStyle(Context context,String pathName){
        byte[]  buffer1 = null;
        InputStream is1 = null;
        try {
            is1 = context.getResources().getAssets().open(pathName);
            int lenght1 = is1.available();
            buffer1 = new byte[lenght1];
            is1.read(buffer1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is1!=null) {
                    is1.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer1;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


}