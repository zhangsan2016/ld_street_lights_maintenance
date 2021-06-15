package com.example.ld_street_lights_maintenance.fragment.devices;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.cluster.Cluster;
import com.example.ld_street_lights_maintenance.common.MyApplication;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.entity.ProjectInfo;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.util.SpUtils;
import com.example.ld_street_lights_maintenance.view.FlowRadioGroup;
import com.google.gson.Gson;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DeviceControlFragment extends Fragment {
    private DeviceLampJson.DataBean device = null;
    // 显示Toast
    private static final int SHOW_TOAST = 20;

    private Handler baseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    String text = (String) msg.obj;
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    public static DeviceControlFragment getInstance(DeviceLampJson.DataBean device) {
        DeviceControlFragment sf = new DeviceControlFragment();
        sf.device = device;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_control, null);

        initView(v);

        return v;
    }

    private void initView(View v) {

        if (device != null) {
            IndicatorSeekBar sb_quickly_adjustable_lamp = v.findViewById(R.id.sb_quickly_adjustable_lamp);
            sb_quickly_adjustable_lamp.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                    //  String param = "{\"UUID\": \"" + device.getUUID() +"\",\"Confirm\": 297,\"options\": {\"accuracy\": " + seekBar.getProgress() +"}}";
                    String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + seekBar.getProgress() + "}}";
                    sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);

                }
            });
            IndicatorSeekBar sb_main_adjustable_lamp = v.findViewById(R.id.sb_main_adjustable_lamp);
            sb_main_adjustable_lamp.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                    LogUtil.e("xx seekBar = " + seekBar.getProgress());
                    String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 260,\"options\": {\"FirDimming\":" + seekBar.getProgress() + "}}";
                    sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);

                }
            });
            IndicatorSeekBar sb_subsidiary_adjustable_lamp = v.findViewById(R.id.sb_subsidiary_adjustable_lamp);
            sb_subsidiary_adjustable_lamp.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                    LogUtil.e("xx seekBar = " + seekBar.getProgress());
                    String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 260,\"options\": {\"SecDimming\":" + seekBar.getProgress() + "}}";
                    sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);

                }
            });

            Button bt_overall_off = v.findViewById(R.id.bt_overall_off);
            bt_overall_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + 0 + "}}";
                    sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_overall_on = v.findViewById(R.id.bt_overall_on);
            bt_overall_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + 100 + "}}";
                    sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            FlowRadioGroup frg_area_type = v.findViewById(R.id.frg_area_type);
            frg_area_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_area_type_1:
                            String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 289,\"options\": {\"Group0_ID\":1,\"Group1_ID\":0,\"Group2_ID\":0,\"Group3_ID\":0}}";
                            sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);
                            break;
                        case R.id.rb_area_type_2:
                            String param2 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 289,\"options\": {\"Group0_ID\":0,\"Group1_ID\":1,\"Group2_ID\":0,\"Group3_ID\":0}}";
                            sendOrder(param2, HttpConfiguration.DEVICE_CONTROL_URL);
                            break;
                        case R.id.rb_area_type_3:
                            String param3 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 289,\"options\": {\"Group0_ID\":0,\"Group1_ID\":0,\"Group2_ID\":1,\"Group3_ID\":0}}";
                            sendOrder(param3, HttpConfiguration.DEVICE_CONTROL_URL);
                            break;
                        case R.id.rb_area_type_4:
                            String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 289,\"options\": {\"Group0_ID\":0,\"Group1_ID\":0,\"Group2_ID\":0,\"Group3_ID\":1}}";
                            sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                            break;

                    }
                }
            });

            Button bt_alarm_clear = v.findViewById(R.id.bt_alarm_clear);
            bt_alarm_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  String param4 = "{\"UUID\": \""+ device.getUUID() +"\"}";
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\"}";
                    sendOrder(param4, HttpConfiguration.CLEAN_ALARM_URL);
                    //  sendOrder(param4, " https://iot.sz-luoding.com:888/api/device/clearAlarm");
                }
            });

            Button bt_alarm_light_glint = v.findViewById(R.id.bt_alarm_light_glint);
            bt_alarm_light_glint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 270,\"options\": {\"Alarm_Light_Mode\":\"OFF\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_alarm_light_clear = v.findViewById(R.id.bt_alarm_light_clear);
            bt_alarm_light_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 270,\"options\": {\"Alarm_Light_Mode\":\"FLASH\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });


            Button bt_infrared_off = v.findViewById(R.id.bt_infrared_off);
            bt_infrared_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 234,\"options\": {\"IR_Dimming_en\":\"0\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_infrared_on = v.findViewById(R.id.bt_infrared_on);
            bt_infrared_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 234,\"options\": {\"IR_Dimming_en\":\"1\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_Sun_Light_off = v.findViewById(R.id.bt_Sun_Light_off);
            bt_Sun_Light_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 280,\"options\": {\"Sun_Light_Enable\":\"0\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_Sun_Light_on = v.findViewById(R.id.bt_Sun_Light_on);
            bt_Sun_Light_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 280,\"options\": {\"Sun_Light_Enable\":\"0\"}}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_get_status = v.findViewById(R.id.bt_get_status);
            bt_get_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": \"232\"}";
                    sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                }
            });

            Button bt_device_accuracy = v.findViewById(R.id.bt_device_accuracy);
            final EditText et_device_accuracy = v.findViewById(R.id.et_device_accuracy);
            bt_device_accuracy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String accuracy =  et_device_accuracy.getText().toString();
                    if (accuracy != null && !accuracy.equals("")){
                        String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 297,\"options\": {\"accuracy\":"+ accuracy  +"}}";
                        sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                    }else{
                        showToast("请先输入角度校准值~");
                    }

                }
            });

            Button bt_device_send_order = v.findViewById(R.id.bt_device_send_order);
            final EditText et_order_code = v.findViewById(R.id.et_order_code);
            final EditText et_order = v.findViewById(R.id.et_order);
            /*String checktext =  "{\"Alarm_Light_Mode\":\"OFF\"}";
            et_order.setText(checktext);*/
            bt_device_send_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!et_order.getText().toString().equals("") && !et_order_code.getText().toString().equals("")){
                        String param4 = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\":" + et_order_code.getText().toString() + ",\"options\":"+ et_order.getText().toString()  +"}";
                        sendOrder(param4, HttpConfiguration.DEVICE_CONTROL_URL);
                    }else{
                        showToast("请先输入指令码和指令~");

                    }
                }
            });

        }
    }


    private void sendOrder(String param, String url) {

        // String url =  HttpConfiguration.DEVICE_CONTROL_URL;
        LogUtil.e("xx param = " + param);
        LogUtil.e("xx url = " + url);
        LogUtil.e("xx getToken = " + getToken());

        RequestBody requestBody = FormBody.create(param, MediaType.parse("application/json"));
        HttpUtil.sendHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("连接服务器异常！");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String data = response.body().string();
                LogUtil.e("xx data = " + data);
                if (data.equals("OK")) {
                    showToast("指令发送成功~");
                } else {
                    showToast("指令发送失败，请检查当前网络~");
                }

            }
        }, getToken(), requestBody);
    }

    private String getToken() {
        Gson gson = new Gson();
        LoginInfo loginInfo = gson.fromJson((String) SpUtils.getValue(SpUtils.LOGIN_INFO, ""), LoginInfo.class);
        return loginInfo.getData().getToken().getToken();
    }

    protected void showToast(String msg) {
        Message message = baseHandler.obtainMessage();
        message.what = SHOW_TOAST;
        message.obj = msg;
        baseHandler.sendMessage(message);
    }

}