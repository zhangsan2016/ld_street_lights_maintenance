package com.example.ld_street_lights_maintenance.fragment.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.entity.DeviceHistoryJson;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.util.SpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceHistoryFragment2 extends Fragment {

    private DeviceLampJson.DataBean device = null;
    private ListView lv_history;
    private List<String> list = new ArrayList<>();
    private   ArrayAdapter<String> adapter;

    public static DeviceHistoryFragment2 getInstance(DeviceLampJson.DataBean device) {
        DeviceHistoryFragment2 dhf = new DeviceHistoryFragment2();
        dhf.device = device;
        return dhf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_history2, null);
        initView(v);

        lv_history = v.findViewById(R.id.lv_history);
       adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, list);
        lv_history.setAdapter(adapter);

        // 获取历史记录
        getHistory();

        return v;
    }


    private void getHistory() {

        if (device == null){
            return;
        }

       String param =  "{\"UUID\": \"" + device.getUUID() + "\"}";
        String url = HttpConfiguration.HISTORY_METRICS_URL;

        RequestBody requestBody = FormBody.create(param, MediaType.parse("application/json"));
        HttpUtil.sendHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String json = response.body().string();
                LogUtil.e("xx DeviceHistoryFragment data = " + json);
                // 解析返回过来的json
                Gson gson = new Gson();
                DeviceHistoryJson deviceHistoryJson = gson.fromJson(json, DeviceHistoryJson.class);
                LogUtil.e("xx DeviceHistoryFragment data size = "+ "  " + deviceHistoryJson.getData().size());
                list.clear();
                if(deviceHistoryJson.getData().size() > 10){
                    for (int i = 0; i < 10; i++) {
                        list.add(deviceHistoryJson.getData().get(i).toString());
                    }

                }else{
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < deviceHistoryJson.getData().size(); i++) {
                        list.add(deviceHistoryJson.getData().get(i).toString());
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });



            }
        }, getToken(), requestBody);
    }

    private String getToken() {
        Gson gson = new Gson();
        LoginInfo loginInfo = gson.fromJson((String) SpUtils.getValue(SpUtils.LOGIN_INFO, ""), LoginInfo.class);
        return loginInfo.getData().getToken().getToken();
    }

    private void initView(View v) {
        if (device != null) {



        }
    }
}
