package com.example.ld_street_lights_maintenance.cluster;

import com.amap.api.maps.model.LatLng;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;


/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private DeviceLampJson.DataBean deviceLamp;

    public RegionItem(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public RegionItem(LatLng latLng, DeviceLampJson.DataBean deviceLamp) {
        mLatLng = latLng;
        this.deviceLamp = deviceLamp;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

    public DeviceLampJson.DataBean getDeviceLamp() {
        return deviceLamp;
    }
}
