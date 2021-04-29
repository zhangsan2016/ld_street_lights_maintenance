package com.example.ld_street_lights_maintenance.fragment.mainfragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseFragment;

public class NfcFragment extends BaseFragment {
    public static final int REQUEST_CODE_QR = 10;
    // 请求权限的code
    public static final int REQUEST_CODE_CAMERA = 21;

    // xml缓存的name
    private static final String NFC_DATA_CACHE = "NfcDataCache.xml";
    // xml编辑缓存的name
    private static final String NFC_EIDT_DATA_CACHE = "NfcEidtDataCache.xml";
    // handle
    private static final int HANDLE_UP_WRITE = 21;
    private static final int HANDLE_UP_READ = 22;
    private static final int START_WRITE_NFC = 23;
    private static final int STOP_WRITE_NFC = 24;


    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private LinearLayout ll;
    private EditText ed_search;
    private EditText et_text_editor;
    private TextView bt_save_config;
    private TextView tv_deploy;
    private TextView tv_write;
    private TextView tv_edit_switch;
    private ToggleButton tb_nfc_switch;
    private boolean temp = false;
    private Button bt_clear;
    private ProgressBar progressbar;
    private AlertDialog writeAlertDialog;
    private Button bt_uploading;
    private  TextView   tv_location;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前高德地图定位
    private AMapLocation cAMapLocation;
    // 登录获取的 Token
    private String token = null;
    private Context mContext;

    private String cxml = "\n" +
            "<当前读取信息>\n" +
            "  <设备类型>0001</设备类型>\n" +
            "  <更新标志>58</更新标志>\n" +
            "  <CRC>0000</CRC>\n" +
            "  <安装测试模式>1</安装测试模式>\n" +
            "  <主灯1段调光时>18</主灯1段调光时>\n" +
            "  <主灯1段调光分>30</主灯1段调光分>\n" +
            "  <主灯1段调光亮度>100</主灯1段调光亮度>\n" +
            "  <主灯2段调光时>21</主灯2段调光时>\n" +
            "  <主灯2段调光分>0</主灯2段调光分>\n" +
            "  <主灯2段调光亮度>100</主灯2段调光亮度>\n" +
            "  <主灯3段调光时>23</主灯3段调光时>\n" +
            "  <主灯3段调光分>0</主灯3段调光分>\n" +
            "  <主灯3段调光亮度>100</主灯3段调光亮度>\n" +
            "  <主灯4段调光时>1</主灯4段调光时>\n" +
            "  <主灯4段调光分>0</主灯4段调光分>\n" +
            "  <主灯4段调光亮度>100</主灯4段调光亮度>\n" +
            "  <主灯5段调光时>4</主灯5段调光时>\n" +
            "  <主灯5段调光分>0</主灯5段调光分>\n" +
            "  <主灯5段调光亮度>100</主灯5段调光亮度>\n" +
            "  <主灯6段调光时>7</主灯6段调光时>\n" +
            "  <主灯6段调光分>30</主灯6段调光分>\n" +
            "  <主灯6段调光亮度>100</主灯6段调光亮度>\n" +
            "  <副灯1段调光时>18</副灯1段调光时>\n" +
            "  <副灯1段调光分>30</副灯1段调光分>\n" +
            "  <副灯1段调光亮度>100</副灯1段调光亮度>\n" +
            "  <副灯2段调光时>21</副灯2段调光时>\n" +
            "  <副灯2段调光分>0</副灯2段调光分>\n" +
            "  <副灯2段调光亮度>100</副灯2段调光亮度>\n" +
            "  <副灯3段调光时>23</副灯3段调光时>\n" +
            "  <副灯3段调光分>0</副灯3段调光分>\n" +
            "  <副灯3段调光亮度>100</副灯3段调光亮度>\n" +
            "  <副灯4段调光时>1</副灯4段调光时>\n" +
            "  <副灯4段调光分>0</副灯4段调光分>\n" +
            "  <副灯4段调光亮度>100</副灯4段调光亮度>\n" +
            "  <副灯5段调光时>4</副灯5段调光时>\n" +
            "  <副灯5段调光分>0</副灯5段调光分>\n" +
            "  <副灯5段调光亮度>100</副灯5段调光亮度>\n" +
            "  <副灯6段调光时>7</副灯6段调光时>\n" +
            "  <副灯6段调光分>30</副灯6段调光分>\n" +
            "  <副灯6段调光亮度>100</副灯6段调光亮度>\n" +
            "  <过流保护开关>1</过流保护开关>\n" +
            "  <漏电保护开关>0</漏电保护开关>\n" +
            "  <照度开灯开关>0</照度开灯开关>\n" +
            "  <过压保护阈值>250(V)</过压保护阈值>\n" +
            "  <欠压保护阈值>120(V)</欠压保护阈值>\n" +
            "  <过流保护阈值>5(A)</过流保护阈值>\n" +
            "  <欠流保护阈值>0(A)</欠流保护阈值>\n" +
            "  <报警开关>-1</报警开关>\n" +
            "  <经纬度辅助开灯开关>0</经纬度辅助开灯开关>\n" +
            "  <漏电保护阈值>40(mA)</漏电保护阈值>\n" +
            "  <照度开灯阈值>30(Lux)</照度开灯阈值>\n" +
            "  <照度关灯阈值>5(Lux)</照度关灯阈值>\n" +
            "  <灯杆倒塌报警开关>0</灯杆倒塌报警开关>\n" +
            "  <项目地区>400700</项目地区>\n" +
            "  <项目编号>00</项目编号>\n" +
            "  <IMEI>865976051596240</IMEI>\n" +
            "  <维修IMEI>0</维修IMEI>\n" +
            "  <执行底板ID>0</执行底板ID>\n" +
            "  <NC>0</NC>\n" +
            "  <角度校准标志>0</角度校准标志>\n" +
            "  <校准角度>0(度)</校准角度>\n" +
            "  <角度报警阈值误差>0</角度报警阈值误差>\n" +
            "  <过压报警标志>1</过压报警标志>\n" +
            "  <欠压报警标志>0</欠压报警标志>\n" +
            "  <过流报警标志>0</过流报警标志>\n" +
            "  <欠流报警标志>0</欠流报警标志>\n" +
            "  <漏电报警标志>0</漏电报警标志>\n" +
            "  <灯杆倒塌报警标志>0</灯杆倒塌报警标志>\n" +
            "  <灯杆碰撞报警标志>1</灯杆碰撞报警标志>\n" +
            "  <温度异常报警标志>0</温度异常报警标志>\n" +
            "  <重启计数>8(次)</重启计数>\n" +
            "  <X方向加速度初始值>0</X方向加速度初始值>\n" +
            "  <X方向加速度初始值小数>2048</X方向加速度初始值小数>\n" +
            "  <Y方向加速度初始值>0</Y方向加速度初始值>\n" +
            "  <Y方向加速度初始值小数>1536</Y方向加速度初始值小数>\n" +
            "  <Z方向加速度初始值>0</Z方向加速度初始值>\n" +
            "  <Z方向加速度初始值小数>9472</Z方向加速度初始值小数>\n" +
            "  <经度整数>0</经度整数>\n" +
            "  <经度小数>0</经度小数>\n" +
            "  <纬度整数>0</纬度整数>\n" +
            "  <纬度小数>0</纬度小数>\n" +
            "  <远程服务器域名或IP>ludeng.stgxy.com</远程服务器域名或IP>\n" +
            "  <远程端口>50001</远程端口>\n" +
            "  <用户名>device</用户名>\n" +
            "  <密码>!@Ddfox136030</密码>\n" +
            "</当前读取信息>\n";

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDLE_UP_WRITE:

                    String uuid = (String) msg.obj;
                    if (writeAlertDialog.isShowing()) {
                        TextView tv_cache_nfcuid = writeAlertDialog.findViewById(R.id.tv_write_nfcuid);
                        tv_cache_nfcuid.setText(uuid);
                    }
                    break;

                case HANDLE_UP_READ:

                    String uuidCache = (String) msg.obj;
                    if (writeAlertDialog.isShowing()) {
                        TextView tv_cache_nfcuid = writeAlertDialog.findViewById(R.id.tv_cache_nfcuid);
                        tv_cache_nfcuid.setText(uuidCache);
                    }

                    break;
                case START_WRITE_NFC:
                    // 初始化进度条
                    initProgressBar();
                    showProgress();

                    break;
                case STOP_WRITE_NFC:
                    // 关闭加载框
                    stopProgress();
                    break;
            }


        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_nfc,
                container, false);

        mContext =  getActivity();

      //  setContentView(R.layout.activity_nfc);

        return rootView;
    }

    /**
     * 初始化进度条
     */
    private void initProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressbar != null) {
                    progressbar.setProgress(0);
                    progressbar.setSecondaryProgress(0);
                }
            }
        });

    }

}