package com.example.ld_street_lights_maintenance.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.entity.FirmwareJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirmwareUpdateAct extends BaseActivity {
    private Spinner sp_firmware;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_firmware_update);


        initView();


    }

    private void initView() {

        sp_firmware= this.findViewById(R.id.sp_firmware);
        adapter = new ArrayAdapter<String>(FirmwareUpdateAct.this, android.R.layout.simple_spinner_item, list);
        sp_firmware.setAdapter(adapter);

        sp_firmware.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // showToast("当前选项是 ：" + adapter.getItem(position));
                /* 将 spinnertext 显示^*/
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });



    }


    public void getFirmware(View view) {

        showProgress();
        String token = getToken();
        String url = "https://iot2.sz-luoding.com:2888/api/util_asset/filelist";
        HttpUtil.sendHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        FirmwareUpdateAct.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("连接服务器异常！");
                            }
                        });
                        stopProgress();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String json = response.body().string();
                        Log.e("xxx", "成功 json = " + json);
                        Gson gson = new Gson();
                        FirmwareJson firmwareJson = gson.fromJson(json, FirmwareJson.class);
                        Log.e("xxx", " json = " + firmwareJson.getData().getFiles().size() );
                        // 清空list
                        list.clear();
                        for (int i = 0; i < firmwareJson.getData().getFiles().size(); i++) {
                            Log.e("xxx", "  firmware = " + firmwareJson.getData().getFiles().get(i));
                            if(firmwareJson.getData().getFiles().get(i).contains(".bin")){
                                list.add(firmwareJson.getData().getFiles().get(i));
                            }
                        }
                        FirmwareUpdateAct.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        stopProgress();
                    }
                }
        );

    }
}
