package com.example.ld_street_lights_maintenance.fragment.devices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.NfcNdefActivity;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.util.SpUtils;
import com.example.ld_street_lights_maintenance.view.FlowRadioGroup;
import com.google.gson.Gson;
import com.ldgd.ld_nfc_ndef_module.util.NfcUtils;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DeviceControlFragment1 extends Fragment {
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


    public static DeviceControlFragment1 getInstance(DeviceLampJson.DataBean device) {
        DeviceControlFragment1 sf = new DeviceControlFragment1();
        sf.device = device;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_device_control1, null);

        initView(v);

        return v;
    }

    private void initView(View v) {

        if (device != null) {

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

            final ToggleButton tb_nfc_switch = v.findViewById(R.id.tb_relay_switch);
            tb_nfc_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tb_nfc_switch.isChecked()){
                        String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 512,\"options\": {\"Rel_State\":" + 0 + "}}";
                        sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);
                    }else{
                        String param = "{\"UUID\": \"" + device.getUUID() + "\",\"Confirm\": 512,\"options\": {\"Rel_State\":" + 255 + "}}";
                        sendOrder(param, HttpConfiguration.DEVICE_CONTROL_URL);
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