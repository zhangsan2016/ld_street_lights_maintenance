package com.example.ld_street_lights_maintenance.act;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.entity.FirmwareJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FirmwareUpdateAct extends BaseActivity {
    private Spinner sp_firmware;
    private  TextView tv_endpoint;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_firmware_update);


        initView();


    }

    private void initView() {

        sp_firmware = this.findViewById(R.id.sp_firmware);
        tv_endpoint = this.findViewById(R.id.tv_endpoint);
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
                        final FirmwareJson firmwareJson = gson.fromJson(json, FirmwareJson.class);
                        Log.e("xxx", " json = " + firmwareJson.getData().getFiles().size());
                        // 清空list
                        list.clear();
                        for (int i = 0; i < firmwareJson.getData().getFiles().size(); i++) {
                            Log.e("xxx", "  firmware = " + firmwareJson.getData().getFiles().get(i));
                            if (firmwareJson.getData().getFiles().get(i).contains(".bin")) {
                                list.add(firmwareJson.getData().getFiles().get(i));
                            }
                        }
                        FirmwareUpdateAct.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_endpoint.setText(firmwareJson.getData().getEndpoint());
                                adapter.notifyDataSetChanged();
                            }
                        });

                        stopProgress();
                    }
                }
        );

    }

    public void firmwareUp(View view) {

        showProgress();
        String url = "http://asset.sz-luoding.com/"+ sp_firmware.getSelectedItem().toString();
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

                long totalSize = response.body().contentLength();  //文件总大小

                writeFile(response.body());

                stopProgress();
            }
        });

    }


    private void writeFile(ResponseBody body) {
        InputStream is = null;  //网络输入流
        FileOutputStream fos = null;  //文件输出流

        is = body.byteStream();

        String downloadFile = sp_firmware.getSelectedItem().toString();
        // /data/data/com.ldgd.ldstreetlightmanagement/cache/com.android.opengl.shaders_cache
        String filePath = FirmwareUpdateAct.this.getCacheDir() + File.separator + downloadFile;
        File file = new File(filePath);
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            long totalSize = body.contentLength();  //文件总大小
            long sum = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                sum += len;
            }

            if(sum == totalSize){
                showToast(downloadFile + "下载完成");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }










}
