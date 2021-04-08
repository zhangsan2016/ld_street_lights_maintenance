package com.example.ld_street_lights_maintenance.act;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.amap.api.maps.MapView;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseFragment;

public class MapFragment extends BaseFragment {
    MapView mMapView = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_map,
                container, false);

        //获取地图控件引用
        mMapView = (MapView)rootView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        return rootView;
    }
}