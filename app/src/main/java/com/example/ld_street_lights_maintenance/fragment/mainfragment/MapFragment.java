package com.example.ld_street_lights_maintenance.fragment.mainfragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.LoginAct;
import com.example.ld_street_lights_maintenance.base.BaseFragment;
import com.example.ld_street_lights_maintenance.cluster.Cluster;
import com.example.ld_street_lights_maintenance.cluster.ClusterClickListener;
import com.example.ld_street_lights_maintenance.cluster.ClusterItem;
import com.example.ld_street_lights_maintenance.cluster.ClusterRender;
import com.example.ld_street_lights_maintenance.cluster.RegionItem;
import com.example.ld_street_lights_maintenance.common.MyApplication;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.entity.ProjectInfo;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.util.SpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapFragment extends BaseFragment implements ClusterRender, AMap.OnMapLoadedListener, ClusterClickListener {
    MapView mMapView = null;
    private AMap aMap;
    private Context mContext;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_map,
                container, false);
        mContext = this.getActivity();

        // 初始化地图
        initAMap(savedInstanceState,rootView);
        // 获取项目
        getProject();



        return rootView;
    }

    private void getProject() {

        String param = "{\"size\":1000}";
        String url =  HttpConfiguration.PROJECT_LIST_URL;

        RequestBody requestBody = FormBody.create(param, MediaType.parse("application/json"));
        HttpUtil.sendHttpRequest(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showToast("连接服务器异常！");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {

                    ProjectInfo project =   MyApplication.gson.fromJson( response.body().string(), ProjectInfo.class);

                    List<ProjectInfo.DataBeanX.DataBean> projectList =  project.getData().getData();

                    if(projectList == null || projectList.size() == 0){
                        return;
                    }


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (ProjectInfo.DataBeanX.DataBean projectInfo : projectList) {

                        try {
                            // 用于同步线程
                            final CountDownLatch latch = new CountDownLatch(1);

                            // 用于计算当前显示范围
                            LatLng ll = new LatLng(Double.parseDouble(projectInfo.getLat()), Double.parseDouble(projectInfo.getLng()));
                            Cluster cluster = new Cluster(ll, projectInfo.getTitle());
                            builder.include(ll);

                            // 获取当前项目下的所有路灯
                            getDeviceLampList(projectInfo.getTitle(), getToken(), cluster, latch);

                            //阻塞当前线程直到latch中数值为零才执行
                            latch.await();

                          //  mClusterOverlay.addClusterGroup(cluster);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, getToken(), requestBody);
    }


    /**
     * 获取设备下管理的所有路灯
     */
    public void getDeviceLampList(final String title, final String token, final Cluster cluster, final CountDownLatch latch) {

        new Thread(new Runnable() {
            @Override
            public void run() {



                String url = HttpConfiguration.DEVICE_LIST_URL;

                // 创建请求的参数body
                //   String postBody = "{\"where\":{\"PROJECT\":" + title + "},\"size\":5000}";
               // String postBody = "{\"where\":{\"PROJECT\":\"" + title + "\"},\"size\":5000}";
                String postBody = "{\"size\":1000}";
                RequestBody body = FormBody.create(MediaType.parse("application/json"), postBody);

                HttpUtil.sendHttpRequest(url, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.e("xxx" + "失败" + e.toString());
                        showToast("连接服务器异常！");
                        stopProgress();

                        //让latch中的数值减一
                        latch.countDown();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String json = response.body().string();

                        System.out.println(">>>>>>>>>> json = " + json);

                        // 解析返回过来的json
                        Gson gson = new Gson();
                        DeviceLampJson deviceLampJson = gson.fromJson(json, DeviceLampJson.class);
                        List<DeviceLampJson.DataBean> projectList = deviceLampJson.getData();

                        for (DeviceLampJson.DataBean deviceLamp : projectList) {

                            if (deviceLamp.getLAT().equals("") || deviceLamp.getLNG().equals("")) {
                                break;
                            }
                            if (deviceLamp.getNAME().contains("米泉路")) {
                                LogUtil.e("xxx 米泉路" + deviceLamp.getLAT() + "  " + deviceLamp.getLNG());
                                break;
                            }

                            LatLng ll = new LatLng(Double.parseDouble(deviceLamp.getLAT()), Double.parseDouble(deviceLamp.getLNG()), false);
                            RegionItem regionItem = new RegionItem(ll, deviceLamp);
                            cluster.addClusterItem(regionItem);
                        }


                        latch.countDown();

                    }
                }, token, body);
            }
        }).start();

    }



    private String getToken(){

        Gson gson = new Gson();
        LoginInfo loginInfo =  gson.fromJson((String) SpUtils.getValue(SpUtils.LOGIN_INFO, ""), LoginInfo.class);
        return loginInfo.getData().getToken().getToken();
    }

    private void initAMap(Bundle savedInstanceState, View rootView) {

        //获取地图控件引用
        mMapView = (MapView) rootView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setLogoBottomMargin(-150);//隐藏logo
            // 设置地图缩放比例
            aMap.moveCamera(CameraUpdateFactory.zoomTo(5f));
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


    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {

    }

    @Override
    public Drawable getDrawAble(int clusterNum) {
        return null;
    }
}