package com.example.ld_street_lights_maintenance.fragment.mainfragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.exception.TimeoutException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.MainActivity;
import com.example.ld_street_lights_maintenance.act.NfcNdefActivity;
import com.example.ld_street_lights_maintenance.base.BaseBleFragment;
import com.example.ld_street_lights_maintenance.comm.ObserverManager;
import com.example.ld_street_lights_maintenance.entity.LampData;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.BytesUtil;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.google.gson.Gson;
import com.ldgd.ld_nfc_ndef_module.entity.DataDictionaries;
import com.ldgd.ld_nfc_ndef_module.json.LoginJson;
import com.ldgd.ld_nfc_ndef_module.util.AutoFitKeyBoardUtil;
import com.ldgd.ld_nfc_ndef_module.util.DrawableUtil;
import com.ldgd.ld_nfc_ndef_module.util.MapHttpConfiguration;
import com.ldgd.ld_nfc_ndef_module.util.NfcDataUtil;
import com.ldgd.ld_nfc_ndef_module.util.NfcUtils;
import com.ldgd.ld_nfc_ndef_module.zbar.CaptureActivity;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.ldgd.ld_nfc_ndef_module.util.NfcDataUtil.replaceBlank;

public class NfcFragment extends BaseBleFragment {
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
    private static final int UP_BULE_STATE = 25;
    private static final int UP_LAMP_DATA = 26;
    private static final int UP_FIRDIMMING = 27;
    private static final String TAG = "NfcFragment";


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
    private AlertDialog writeAlertDialog, checkAlertDialog;
    private Button bt_uploading;
    private TextView tv_location;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前高德地图定位
    private AMapLocation cAMapLocation;
    // 登录获取的 Token
    private String token = null;
    private Context mContext;
    /// static private NFCTag mTag;
    private TextView blueState, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17,tv18;

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
                case UP_BULE_STATE:
                    //  ((TextView)checkAlertDialog.getListView().findViewById(R.id.tv_blue_start)).setText("连接蓝牙设备：正常");
                    if (checkAlertDialog.isShowing()) {
                        blueState.setText("蓝牙连接：正常");
                    }
                    break;
                case UP_LAMP_DATA:

                    if (checkAlertDialog.isShowing()) {
                        LampData lampData = (LampData) msg.obj;
                        tv1.setText("" +lampData.getData().getFirDimming());
                        tv2.setText("" +lampData.getData().getEnergy());
                        tv3.setText("" +lampData.getData().getGprs_csq());
                        tv4.setText("" +lampData.getData().getIllu());
                        tv5.setText("" +lampData.getData().getLeak_curt());
                        tv6.setText("" +lampData.getData().getPower());
                        tv7.setText("" +lampData.getData().getPower_Factor());
                        tv8.setText("" +lampData.getData().getTemp());
                        tv9.setText("" +lampData.getData().getVoltage());
                        tv10.setText("" +lampData.getData().getELE_Warning_type());
                        tv11.setText("" +lampData.getData().getWarning_state());
                        tv12.setText("" +lampData.getData().getRESET_COUNT());
                        tv13.setText("" +lampData.getData().getVersion());
                        tv14.setText("" +lampData.getData().getGPS_CLOSETIME());
                        tv15.setText("" +lampData.getData().getGPS_OPENTIME());
                        tv16.setText("" +lampData.getData().getSIM_CCID());
                        tv17.setText("" +lampData.getData().getTime());
                    }

                    break;
                case UP_FIRDIMMING:

                    if (checkAlertDialog.isShowing()) {
                        tv18.setText("光照度上传:正常");
                    }
                    break;
            }


        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_nfc,
                container, false);

        mContext = getActivity();

        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initNFC();

        initView(rootView);

        // 初始化监听
        initListening();

        //初始化定位
        initLocation();

        // 登录获取 token
        getToken();

        return rootView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 停止定位
        locationClient.stopLocation();
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(mContext.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (aMapLocation.getErrorCode() == 0) {
                    cAMapLocation = aMapLocation;

                    tv_location.setText(aMapLocation.getLongitude() + " / " + aMapLocation.getLatitude());
                    sb.append("经    度    : " + aMapLocation.getLongitude() + "\n");
                    sb.append("纬    度    : " + aMapLocation.getLatitude() + "\n");
                    Log.e("xx", ">>>>>>>>>>>>>>>>>>>>>>>>>  经纬度信息 = " + sb.toString());


                }
            }
        });
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    private void initNFC() {
        // 初始化NFC-onResume处理
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        mPendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(mContext, getContext().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 检测nfc权限
        NfcUtils.NfcCheck(getActivity());

    }

    private void initListening() {

        DrawableUtil drawableUtil = new DrawableUtil(ed_search, new DrawableUtil.OnDrawableListener() {

            @Override
            public void onLeft(View v, Drawable left) {
                Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRight(View v, Drawable right) {

                //动态权限申请
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                } else {
                    //扫码
                    goScan();
                }

            }
        });

        tv_deploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtil.e(" xxx tv_deploy = " + et_text_editor.getText().toString().trim());
            }
        });

        // 写入
        tv_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(mContext.getCacheDir(), NFC_EIDT_DATA_CACHE);
                if (file.exists()) {

                    // 显示Dialog
                    writeAlertDialog.show();

                    // 清理dialog的text文本
                    TextView tv_write_nfcuid = writeAlertDialog.findViewById(R.id.tv_write_nfcuid);
                    TextView tv_read_nfcuid = writeAlertDialog.findViewById(R.id.tv_cache_nfcuid);
                    tv_write_nfcuid.setText("");
                    tv_read_nfcuid.setText("");

                    try {
                        // 解析xml文件，得到所有参数
                        FileInputStream inputStream = new FileInputStream(new File(mContext.getCacheDir(), NFC_EIDT_DATA_CACHE));
                        List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);
                        String baseplateId = null;
                        for (int i = 0; i < dataDictionaries.size(); i++) {
                            if (dataDictionaries.get(i).getName().equals("执行底板ID")) {
                                baseplateId = dataDictionaries.get(i).getXmValue();
                            }
                        }


                        // 通过 Handle 更新 AlertDialog
                        Message tempMsg = myHandler.obtainMessage();
                        tempMsg.what = HANDLE_UP_READ;
                        tempMsg.obj = baseplateId.replaceAll(" +", "");
                        myHandler.sendMessage(tempMsg);

                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("读取xml文件失败！");

                    }
                } else {
                    showToast("xml文件不存在，请先保存文件");
                }

            }
        }); // 编辑开关切换
        tv_edit_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp == true) {
                    temp = false;
                    v.setBackgroundResource(R.drawable.ico_nfc_off);
                    //    et_text_editor.setEnabled(true);
                    et_text_editor.setFocusable(false);
                    et_text_editor.setCursorVisible(false);
                    et_text_editor.setFocusableInTouchMode(false);
                } else {
                    temp = true;
                    v.setBackgroundResource(R.drawable.ico_nfc_on);
                    //   et_text_editor.setEnabled(false);
                    et_text_editor.setFocusable(true);
                    et_text_editor.setCursorVisible(true);
                    et_text_editor.setFocusableInTouchMode(true);
                }
            }
        });


        // nfc切换开关
        tb_nfc_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NfcUtils.IsToSet(getActivity(), tb_nfc_switch);
            }
        });

        // 清除
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInterface();
            }
        });

        // 缓存当前数据get
        bt_save_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmlStr = et_text_editor.getText().toString();
                if (!xmlStr.equals("")) {
                    File file = new File(getActivity().getCacheDir(), NFC_EIDT_DATA_CACHE);
                    saveXmlCacheDir(xmlStr, file);
                } else {
                    showToast("当前内容为空,请您触碰NFC设备，读取设备信息");
                }
            }
        });

        bt_uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoading();
            }
        });
    }

    private void saveXmlCacheDir(String xmlStr, File file) {
        try {
            NfcDataUtil.saveXml(xmlStr, file);
            showToast("保存成功");
            progressbar.setSecondaryProgress(100);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("保存出错");
            progressbar.setSecondaryProgress(0);
        }
    }

    /// static private NFCTag mTag;
    static private Tag mTag;

    public void onNewIntent() {
        LogUtil.e("XXXXXXXXXXXXXXXXXXXXXXX onNewIntent");
        //获取Tag对象
        Intent intent = getActivity().getIntent();
        mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        readNfcTag(intent);
    }

    /**
     * 上传设备信息
     */
    private void upLoading() {

        final String uuid = ed_search.getText().toString().trim();
        final String xmlConfig = et_text_editor.getText().toString().trim();


        if (uuid.equals("")) {
            showToast("当前uuid不能为空");
            return;
        }
        myHandler.sendEmptyMessage(START_WRITE_NFC);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = MapHttpConfiguration.LOGIN_URl;

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", "ld")
                        .add("password", "ld9102")
                        .build();


                HttpUtil.sendHttpRequest(url, new Callback() {


                    @Override
                    public void onFailure(Call call, IOException e) {
                        showToast("连接服务器失败！");
                        myHandler.sendEmptyMessage(STOP_WRITE_NFC);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String json = response.body().string();
                            myHandler.sendEmptyMessage(STOP_WRITE_NFC);

                            // 解析返回过来的json
                            Gson gson = new Gson();
                            LoginJson loginInfo = gson.fromJson(json, LoginJson.class);


                            if (loginInfo.getErrno() == 0) {

                                reportDevice(xmlConfig, uuid, loginInfo.getData().getToken().getToken());

                            } else {
                                showToast("连接服务器失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("获取异常错误 ：" + e.getMessage());
                        }

                    }
                }, requestBody);

            }
        }).start();
    }


    /**
     * 汇报设备信息到服务器
     *
     * @param xmlConfig xml 配置信息
     * @param uuid      上传保存的 uuid
     * @param token     登录的 token
     */
    private void reportDevice(final String xmlConfig, final String uuid, final String token) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                // 格式化xml
                String xml = null;
                Document document = null;
                try {
                    document = DocumentHelper.parseText(xmlConfig);
                    xml = document.getRootElement().asXML(); //可以去掉头部内容
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                String url = MapHttpConfiguration.REPORT_CONFIG_URL;
                //     String postBody = "{\"UUID\":\"2016C0312000001200001192\",\"config\": {\"xml_config\": \"21351515615sdf1sd61fs651d65f465sd46f54s6d54f33998\"}}";
                String postBody = " {\"UUID\": \"" + uuid + "\",\"config\": {\"xml_config\":\"" + replaceBlank(xml) + "\"}}";


                RequestBody body = FormBody.create(MediaType.parse("application/json"), postBody);


                HttpUtil.sendHttpRequest(url, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("" + "失败" + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String json = response.body().string();
                        showToast("上传完成");

                    }
                }, token, body);
            }
        }).start();

    }

    /**
     * 清理界面
     */
    private void clearInterface() {
        et_text_editor.setText("");
        // 初始化进度条
        initProgressBar();
        // 删除编辑的缓存文件
        NfcDataUtil.deleFile(new File(mContext.getCacheDir(), NFC_EIDT_DATA_CACHE));
    }

    /**
     * 跳转到二维码扫描界面
     */
    private void goScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QR);
    }

    private void initView(View rootView) {


        //将Activity传入以便获取contentView
        AutoFitKeyBoardUtil.getInstance().assistActivity(getActivity());

        ll = (LinearLayout) rootView.findViewById(R.id.ll_nfc);
        ed_search = (EditText) rootView.findViewById(R.id.ed_search);
        bt_save_config = (TextView) rootView.findViewById(R.id.bt_save_config);
        tv_deploy = (TextView) rootView.findViewById(R.id.tv_deploy);
        tv_write = (TextView) rootView.findViewById(R.id.tv_write);
        tv_edit_switch = (TextView) rootView.findViewById(R.id.tv_edit_switch);
        et_text_editor = (EditText) rootView.findViewById(R.id.et_text_editor);
        tb_nfc_switch = (ToggleButton) rootView.findViewById(R.id.tb_nfc_switch);
        bt_clear = (Button) rootView.findViewById(R.id.bt_clear);
        progressbar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        bt_uploading = (Button) rootView.findViewById(R.id.bt_uploading);
        tv_location = (TextView) rootView.findViewById(R.id.tv_location);

        // 清除当前界面信息
        clearInterface();

        // 写入提示框
        View view = View.inflate(mContext, R.layout.alert_dialog_item, null);
        writeAlertDialog = new AlertDialog.Builder(mContext).setTitle("提示")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("写入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                     /*   try {
                            Field field = dialog.getClass().getSuperclass()
                                    .getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/


                        // 写入
                        //     writeNfc();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      /*  try {
                            Field field = dialog.getClass().getSuperclass()
                                    .getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        //    showToast("已经取消");
                    }
                }).create();


        writeAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = writeAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                final ToggleButton tbLocation = writeAlertDialog.findViewById(R.id.tb_location);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 判断是否更新经纬度
                        if (tbLocation.isChecked()) {
                            // 更新经纬度信息
                            upLocation();

                        } else {
                            // 写入操作执行，先检测数据
                            checkWrite();
                        }

                    }
                });

                // 经纬度上传监听
                final EditText et_longitude = writeAlertDialog.findViewById(R.id.et_longitude);
                final EditText et_latitude = writeAlertDialog.findViewById(R.id.et_latitude);
                if (cAMapLocation != null) {
                    et_longitude.setText(cAMapLocation.getLongitude() + "");
                    et_latitude.setText(cAMapLocation.getLatitude() + "");

                }
                final LinearLayout ll_uplocation = writeAlertDialog.findViewById(R.id.ll_uplocation);
                tbLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tbLocation.isChecked() == true) {
                            if (cAMapLocation != null) {
                                et_longitude.setText(cAMapLocation.getLongitude() + "");
                                et_latitude.setText(cAMapLocation.getLatitude() + "");
                                ll_uplocation.setVisibility(View.VISIBLE);

                            } else {
                                tbLocation.setChecked(false);
                                ll_uplocation.setVisibility(View.GONE);
                                showToast("定位失败，无法获取经纬度信息");
                            }
                        } else {
                            ll_uplocation.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    /**
     * 经纬度更新
     */
    private void upLocation() {
        if (token == null) {
            getToken();
            showToast("当前 token 为空，请重新写入！");
        } else {
            try {
                httpUpLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String regionN, proN, imei, uuid;

    private void httpUpLocation() throws Exception {

        // 解析xml文件，获取url
        FileInputStream inputStream = new FileInputStream(new File(mContext.getCacheDir(), NFC_EIDT_DATA_CACHE));
        List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);

        for (DataDictionaries dataDictionarie : dataDictionaries) {
            if (dataDictionarie.getName().equals("项目地区")) {
                regionN = dataDictionarie.getXmValue();
            } else if (dataDictionarie.getName().equals("项目编号")) {
                proN = dataDictionarie.getXmValue();
            } else if (dataDictionarie.getName().equals("IMEI")) {
                imei = dataDictionarie.getXmValue();
            }
        }
        uuid = regionN + proN + imei;
        if (uuid != null) {

            // 获取 Dialog 中的经纬度
            String longitude = ((EditText) writeAlertDialog.findViewById(R.id.et_longitude)).getText().toString();
            String latitude = ((EditText) writeAlertDialog.findViewById(R.id.et_latitude)).getText().toString();


            String url = "https://ludeng.stgxc.com:9443/api/device_lamp/edit";
            // String postBody = "{\"data\":{ \"LNG\":"+"106.541652"+",\"\"LAT:" +"29.803828" +"},\"where\":{ \"UUID\":"+"000000000000000000000022" +"} }";
            JSONStringer jsonstr = new JSONStringer()
                    .object().key("data")
                    .object().key("LNG").value(longitude)
                    .key("LAT").value(latitude)
                    .endObject()
                    .key("where").object().key("UUID").value(uuid)
                    .endObject().endObject();
            //  String postBody = "{\"data\":{ \"LNG\":\"106.541654\",\"LAT\":\"29.803828\"},\"where\":{ \"UUID\":\"000000000000000000000022\"} }";
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json"), jsonstr.toString());

            HttpUtil.sendHttpRequest(url, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("连接服务器异常！");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                showToast("更新经纬度成功" + response.body().string());

                                checkWrite();

                                myHandler.sendEmptyMessage(STOP_WRITE_NFC);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, token, requestBody);

        } else {
            showToast("uuid 有误，请先读取uuid");
        }
    }


    private void getToken() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                // String url = "https://ludeng.stgxy.com:9443/api/user/login";

                String url = "https://ludeng.stgxc.com:9443/api/user/login";

              /*  RequestBody requestBody = new FormBody.Builder()
                        .add("strTemplate", "{\"ischeck\":$data.rows}")
                        .add("username", "cy")
                        .add("password", "@@ld9102")
                        .add("strVerify", "[admin]")
                        .build();*/

              /*  RequestBody requestBody = new FormBody.Builder()
                        .add("strTemplate", "{\"ischeck\":$data.rows}")
                        .add("username", "admin")
                        .add("password", "Ld@cc0unt")
                        .add("strVerify", "[admin]")
                        .build();*/

                RequestBody requestBody = new FormBody.Builder()
                        .add("strTemplate", "{\"ischeck\":$data.rows}")
                        .add("username", "cy")
                        .add("password", "@@ld9102")
                        .add("strVerify", "[admin]")
                        .build();


                HttpUtil.sendHttpRequest(url, new Callback() {


                    @Override
                    public void onFailure(Call call, IOException e) {
                        showToast("连接服务器异常！");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {


                        String json = response.body().string();
                        Gson gson = new Gson();
                        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
                        if (loginInfo.getErrno() == 0) {
                            token = loginInfo.getData().getToken().getToken();
                            LogUtil.e("xxx token = " + token);
                        }

                    }
                }, requestBody);
            }
        }).start();

    }

    private void checkWrite() {

        try {

            // 解析xml文件，得到所有参数

         /*   FileInputStream inputStream = new FileInputStream(new File(NfcNdefActivity.this.getCacheDir(), NFC_EIDT_DATA_CACHE));
            List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);*/

           /* InputStream is = NfcNdefActivity.this.getAssets().open("xml/NfcDataCache.xml");
            FileInputStream inputStream = (FileInputStream) (is);
            List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);*/


            FileInputStream inputStream;
            List<DataDictionaries> dataDictionaries;
            final ToggleButton tbDefault = writeAlertDialog.findViewById(R.id.tb_default);
            if (tbDefault.isChecked()) {
                File file = new File(mContext.getCacheDir(), "moren.xml");
                if (!file.exists()) {
                    saveXmlCacheDir(cxml, file);
                }
                inputStream = new FileInputStream(new File(mContext.getCacheDir(), "moren.xml"));
                dataDictionaries = NfcDataUtil.parseXml2(inputStream);
            } else {
                inputStream = new FileInputStream(new File(mContext.getCacheDir(), NFC_EIDT_DATA_CACHE));
                dataDictionaries = NfcDataUtil.parseXml2(inputStream);
            }

            // 如果设备类型为 1 时，需要匹配 IMEI 码
         /*   for (DataDictionaries dataDictionarie : dataDictionaries) {
                if (dataDictionarie.getName().equals("设备类型")) {
                    if (dataDictionarie.getXmValue().equals("0001")) {
                        // 获 MEI 码
                        List<String> imeis = NfcDataUtil.parseImeiExcel("IMEI/v_device_lamp.xls", NfcNdefActivity.this);

                        DataDictionaries cImei = null;
                        boolean imeiVerify = false;
                        for (DataDictionaries imei : dataDictionaries) {
                            if (imei.getName().equals("IMEI")) {
                                cImei = imei;
                                break;
                            }
                        }
                        if (cImei == null) {

                            return;
                        } else {
                            for (int i = 0; i < imeis.size(); i++) {
                                if (imeis.get(i).contains(cImei.getXmValue())) {
                                    imeiVerify = true;
                                    System.out.println(">>>>>>>>>>>>>>>>> imeiVerify 成功 ");
                                    break;

                                }
                            }
                        }
                        if (!imeiVerify) {
                            showToast("IMEI 验证失败！");
                            return;
                        }
                    }
                }
            }*/

            // 写入
            writeNfc(dataDictionaries);

        } catch (Exception e) {
            e.printStackTrace();
            showToast("校验 IMEI 异常， 写入失败");
            return;
        }
    }


    private void writeNfc(final List<DataDictionaries> dataDictionaries) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // 通知 Handle 当前正在写入nfc
                    myHandler.sendEmptyMessage(START_WRITE_NFC);

   /*                    for (int i = 0; i < dataDictionaries.size(); i++) {
                        System.out.println("xxxx = "+dataDictionaries.get(i).getStartAddress() + " - " + dataDictionaries.get(i).getEndAddress() + "  " + dataDictionaries.get(i).getName() + Arrays.toString(dataDictionaries.get(i).getValue() ) + ""  + dataDictionaries.get(i).getXmValue());
                    }*/


                    // 解析excel
                    // 校验获取的参数是否符合规定,然后写入
                    NfcDataUtil.writeNfcDeviceInfo2(dataDictionaries, new NfcDataUtil.OnNfcDataListening() {
                        @Override
                        public void succeed() {
                            showToast("写入成功");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar.setProgress(100);
                                    //  通知 Handle nfc 已关闭写入
                                    myHandler.sendEmptyMessage(STOP_WRITE_NFC);
                                }
                            });
                        }

                        @Override
                        public void failure(String error) {
                            showToast(error);
                            //  通知 Handle nfc 已关闭写入
                            progressbar.setProgress(0);
                            myHandler.sendEmptyMessage(STOP_WRITE_NFC);
                        }

                    }, mContext, mTag, payload);

                } catch (Exception e) {
                    e.printStackTrace();
                    // 初始化进度条
                    initProgressBar();
                    //  通知 Handle nfc 已关闭写入
                    myHandler.sendEmptyMessage(STOP_WRITE_NFC);

                    showToast("" + e.getMessage().toString());
                }
            }
        }).start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

       /* // 处理二维码扫描结果
        if (requestCode == REQUEST_CODE_ZXING) {
            //处理扫描结果（在界面上显示）
            if (null != intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ed_search.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    showToast("解析二维码失败");
                }
            }
        }*/


        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_QR:// 二维码
                // 扫描二维码回传
                if (resultCode == getActivity().RESULT_OK) {
                    if (intent != null) {
                        //获取扫描结果
                        Bundle bundle = intent.getExtras();
                        String result = bundle.getString(CaptureActivity.EXTRA_STRING);
                        ed_search.setText(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void onPostExecute(byte[] mBuffer) {

        System.out.println("xxxxxxxxx " + Arrays.toString(mBuffer));
        if (mBuffer != null) {

            // 判断nfc硬件类型
            byte[] typeByte = new byte[2];
            //   System.arraycopy(mBuffer, 3+ 29, typeByte, 0, 2);
            System.arraycopy(mBuffer, 29, typeByte, 0, 2);
                /*typeByte[0] = 0;
                typeByte[1] = 3;*/
            System.out.println("xxxxxxxxx " + Arrays.toString(typeByte) + "  " + BytesUtil.bytesIntHL(typeByte));
            if (BytesUtil.bytesIntHL(typeByte) == 1) {
                // 根据类型读取 nfc
                readNfcByType("0001_83140000.xls", mBuffer);
            } else if (BytesUtil.bytesIntHL(typeByte) == 2) {
                // 根据类型读取 nfc
                readNfcByType("0002_83140000.xls", mBuffer);
            } else if (BytesUtil.bytesIntHL(typeByte) == 3) {
                // 根据类型读取 nfc
                readNfcByType("0003_83140000.xls", mBuffer);
            } else {
                showToast("当前类型无法解析");
            }
        }
    }


    /**
     * 根据 nfc 的标识类型读取内容信息
     *
     * @param nfcFileName 类型对应的 nfc 文件名
     */
    private void readNfcByType(String nfcFileName, byte[] mBuffer) {


        LogUtil.e("nfcFileName = " + nfcFileName);
        // 解析成xml文件
        File cacheFile = NfcDataUtil.parseBytesToXml(mBuffer, nfcFileName, NFC_DATA_CACHE, mContext);

        if (cacheFile != null) {
            try {

                LogUtil.e("writeAlertDialog.isShowing() = " + writeAlertDialog.isShowing());

                // 更新 Dialog
                if (writeAlertDialog.isShowing()) {
                    // 解析xml文件，得到所有参数
                    FileInputStream inputStream = new FileInputStream(cacheFile);
                    List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);
                    String baseplateId = null;
                    for (int i = 0; i < dataDictionaries.size(); i++) {
                        if (dataDictionaries.get(i).getName().equals("执行底板ID")) {
                            baseplateId = dataDictionaries.get(i).getXmValue();
                        }
                    }


                    // 通过 Handle 更新 AlertDialog
                    Message tempMsg = myHandler.obtainMessage();
                    tempMsg.what = HANDLE_UP_WRITE;
                    tempMsg.obj = baseplateId.replaceAll(" +", "");
                    myHandler.sendMessage(tempMsg);
                }

                if (et_text_editor.getText().toString().equals("")) {
                    //读取文件流
                    FileInputStream fis = new FileInputStream(cacheFile);
                    int size = fis.available();
                    System.out.println("可读取的字节数 " + size);
                    byte[] buffer = new byte[size];
                    fis.read(buffer);
                    String txt = new String(buffer, 0, buffer.length);
                    //  LogUtil.e("xxx XmlUtil.formatXml(txt) =" + NfcDataUtil.formatXml(txt));
                    et_text_editor.setText(NfcDataUtil.formatXml(txt));
                    // 初始化进度条
                    initProgressBar();
                    fis.close();

                }

                // 解析xml文件，得到所有参数
                // 解析xml文件，获取url
                FileInputStream inputStream = new FileInputStream(cacheFile);
                List<DataDictionaries> dataDictionaries = NfcDataUtil.parseXml2(inputStream);
                for (DataDictionaries dataDictionarie : dataDictionaries) {
                    if (dataDictionarie.getName().equals("项目地区")) {
                        regionN = dataDictionarie.getXmValue();
                    } else if (dataDictionarie.getName().equals("项目编号")) {
                        proN = dataDictionarie.getXmValue();
                    } else if (dataDictionarie.getName().equals("IMEI")) {
                        imei = dataDictionarie.getXmValue();
                    }
                }
                uuid = regionN + proN + imei;
                LogUtil.e("xxxx = " + uuid);
                // 测试代码
                showCheckAlertDialog();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("xxxx Exception " + e.getMessage().toString());
            }
        } else {
            Toast.makeText(mContext, "读取失败！", Toast.LENGTH_SHORT).show();
        }
    }


    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private boolean isSearch = true;  // 是否继续向下检索蓝牙设备
    boolean isEnd = false;  // 光照度上传判断，是否继续获取光照度
    private void showCheckAlertDialog() {

        /*Looper.prepare();
        Looper.loop();*/

        isSearch = true;

        // 写入提示框
        View view = View.inflate(mContext, R.layout.alert_check_dialog_item, null);
        blueState = (TextView) view.findViewById(R.id.tv_blue_start);
        tv1 = (TextView) view.findViewById(R.id.tv_1);
        tv2 = (TextView) view.findViewById(R.id.tv_2);
        tv3 = (TextView) view.findViewById(R.id.tv_3);
        tv4 = (TextView) view.findViewById(R.id.tv_4);
        tv5 = (TextView) view.findViewById(R.id.tv_5);
        tv6 = (TextView) view.findViewById(R.id.tv_6);
        tv7 = (TextView) view.findViewById(R.id.tv_7);
        tv8 = (TextView) view.findViewById(R.id.tv_8);
        tv9 = (TextView) view.findViewById(R.id.tv_9);
        tv10 = (TextView) view.findViewById(R.id.tv_10);
        tv11 = (TextView) view.findViewById(R.id.tv_11);
        tv12 = (TextView) view.findViewById(R.id.tv_12);
        tv13 = (TextView) view.findViewById(R.id.tv_13);
        tv14 = (TextView) view.findViewById(R.id.tv_14);
        tv15 = (TextView) view.findViewById(R.id.tv_15);
        tv16 = (TextView) view.findViewById(R.id.tv_16);
        tv17 = (TextView) view.findViewById(R.id.tv_17);
        tv18 = (TextView) view.findViewById(R.id.tv_18);

        checkAlertDialog = new AlertDialog.Builder(mContext).setTitle("提示")
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 销毁时清空所有蓝牙连接
                        BleManager.getInstance().disconnectAllDevice();
                        // 清空nfc数据
                        clearInterface();
                    }
                }).create();

        // 显示Dialog
        checkAlertDialog.show();

        // 获取蓝牙设备
        bleDeviceList.clear();
        BleManager.getInstance().scan(new BleScanCallback() {

            // 开始寻找蓝牙
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

                if (bleDevice.getDevice().toString().contains("84:C2:E4")) {
                    LogUtil.e(" xxxxxxxxxxxx onScanning = " + Arrays.toString(bleDevice.getScanRecord()));
                    bleDeviceList.add(bleDevice);
                }
            }

            // 扫描结束
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                    BleManager.getInstance().cancelScan();
                    LogUtil.e("xxxxxxxxxxxx bleDeviceList.size() =" + bleDeviceList.size());


                    Collections.sort(bleDeviceList, new Comparator<BleDevice>() {
                        @Override
                        public int compare(BleDevice o1, BleDevice o2) {

                            if (o1.getRssi() > o2.getRssi()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });

                    for (int i = 0; i < bleDeviceList.size(); i++) {
                        LogUtil.e("xxx " + bleDeviceList.get(i).getDevice().toString());
                    }

                    for (int i = 0; i < bleDeviceList.size(); i++) {
                        final CountDownLatch latch = new CountDownLatch(1);
                        connect(bleDeviceList.get(i), latch);
                        //阻塞当前线程直到latch中数值为零才执行
                        latch.await();
                        if (!isSearch) {
                            myHandler.sendEmptyMessage(UP_BULE_STATE);
                            break;
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
                if(bleDevices.size() > 0){
                    showToast("蓝牙连接失败~");
                    return;
                }

                // 获取灯杆信息
                final String uriViewByUUID = "https://iot.sz-luoding.com:888/api/device/viewByUUID";
                String param1 = "{\"UUID\": \"" + uuid + "\"}";
                final RequestBody requestBody = FormBody.create(param1, MediaType.parse("application/json"));
                HttpUtil.sendHttpRequest(uriViewByUUID, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        showToast("连接服务器异常！");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        String json = response.body().string();
                        Gson gson = new Gson();
                        LampData lampData = gson.fromJson(json, LampData.class);
                        LogUtil.e("xxx lampData1 = " + lampData.getData());
                        // 通过 Handle 更新 AlertDialog
                        Message tempMsg = myHandler.obtainMessage();
                        tempMsg.what = UP_LAMP_DATA;
                        tempMsg.obj = lampData;
                        myHandler.sendMessage(tempMsg);


                    }
                }, token, requestBody);

                // 控制灯杆
                // String uri =  "https://iot.sz-luoding.com:2890/api/device/control";
                final String uriControl = "https://iot.sz-luoding.com:888/api/device/control";
                final String param2 = "{\"UUID\": \"" + uuid + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + 75 + "}}";
                final String param3 = "{\"UUID\": \"" + uuid + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + 0 + "}}";
                final String param4 = "{\"UUID\": \"" + uuid + "\",\"Confirm\": 260,\"options\": {\"Dimming\":" + 100 + "}}";
                sendOrder(param2, uriControl);



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CountDownLatch latch2 = new CountDownLatch(1);
                            loopIlluCheck(uriViewByUUID, requestBody, uriControl, param3, param4,75,latch2);
                            latch2.await();

                            Thread.sleep(10000);
                            // 亮灯100
                            sendOrder(param4, uriControl);
                            CountDownLatch latch3 = new CountDownLatch(1);
                            loopIlluCheck(uriViewByUUID, requestBody, uriControl, param3, param4,100,latch3);
                            latch3.await();

                            Thread.sleep(10000);
                            // 发送关灯
                            sendOrder(param3, uriControl);
                            myHandler.sendEmptyMessage(UP_FIRDIMMING);
                            showToast("设备测试正常~");

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            showToast("发生异常"+ e.getMessage().toString());
                        }
                    }
                }).start();


            }
        }).start();

    }

    private void loopIlluCheck(final String uriViewByUUID, final RequestBody requestBody, final String uriControl, final String param3, final String param4, final int illu, final CountDownLatch latch2) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                isEnd = false;
                for (int i = 0; i < 5; i++) {
                    LogUtil.e("xx  isEnd 执行");
                    final CountDownLatch latch = new CountDownLatch(1);
                    checkIllu(latch, uriViewByUUID, requestBody,illu);

                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (isEnd){
                        try {
                            //让latch中的数值减一
                            latch2.countDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    }else{
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }

                if(!isEnd){
                    showToast("光照度上传超时");
                }

            }
        }).start();

    }


    private void checkIllu(final CountDownLatch latch, String uriViewByUUID, RequestBody requestBody, final int illu) {
        HttpUtil.sendHttpRequest(uriViewByUUID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("连接服务器异常！");
                //让latch中的数值减一
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String json = response.body().string();
                Gson gson = new Gson();
                LampData lampData = gson.fromJson(json, LampData.class);
                LogUtil.e("xxx lampData2 = " + lampData.getData());
                // 通过 Handle 更新 AlertDialog
                Message tempMsg = myHandler.obtainMessage();
                tempMsg.what = UP_LAMP_DATA;
                tempMsg.obj = lampData;
                myHandler.sendMessage(tempMsg);

                lampData.getData().getFirDimming();
                if( lampData.getData().getFirDimming() == illu){
                    LogUtil.e("xx  isEnd 匹配成功 " + illu);
                    isEnd = true;
                }

                //让latch中的数值减一
                latch.countDown();

            }

        }, token, requestBody);
    }

    private void sendOrder(String param, String url) {


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
               /* if (data.equals("OK")) {
                    showToast("指令发送成功~");
                } else {
                    showToast("指令发送失败，请检查当前网络~");
                }*/

            }
        }, token, requestBody);
    }

    private void connect(final BleDevice bleDevice, final CountDownLatch latch) {

        //让latch中的数值减一
        // latch.countDown();

        // 连接蓝牙
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                //让latch中的数值减一
                latch.countDown();
            }

            // 连接成功
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                try {
                    BlePusher.writeSpliceOrder(new byte[]{0, 31}, null, new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] data) {

                            try {

                                byte[] deviceId = new byte[23];
                                System.arraycopy(data, 5, deviceId, 0, 23);
                                String txt = new String(deviceId, "ascii");
                                if (uuid.equals(txt)) {
                                    LogUtil.e("xxx 匹配成功");
                                    isSearch = false; // 停止继续搜索
                                } else {
                                    LogUtil.e("xxx 匹配失败" + uuid + " : " + txt);
                                }
                                // 销毁时清空所有蓝牙连接
                                BleManager.getInstance().disconnectAllDevice();
                                //让latch中的数值减一
                                latch.countDown();

                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.e("xxx Exception = " + e.getMessage().toString());
                            }


                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            if (exception instanceof TimeoutException) {
                                showToast("写入失败: 当前蓝牙信号较弱，请尝试靠近~");
                            } else {
                                showToast("写入失败:" + exception.toString());
                            }
                        }
                    }, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("xxx Exception = " + e.getMessage().toString());
                }

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {


            }
        });
    }


    private byte[] payload;

    private void readNfcTag(Intent intent) {

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //   Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Log.d("textRecord", "长度 = " + rawMsgs.length);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    payload = record.getPayload();

                    //   System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx " + Arrays.toString(payload2));

                    //    et_text_editor.setText(Arrays.toString(payload));

                    // 获得数据
                    byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
                    int headLong = 1 + langBytes.length;
                    byte[] data = new byte[payload.length - headLong];
                    System.arraycopy(payload, headLong, data, 0, payload.length - headLong);


                    onPostExecute(data);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            Log.v(TAG, "disableForegroundDispatch");
            mNfcAdapter.disableForegroundDispatch(getActivity());
        }
    }

    @Override
    public void onResume() {
        Intent intent = getActivity().getIntent();
        Log.d(TAG, "Resume mainActivity intent: " + intent);
        super.onResume();


        if (mNfcAdapter != null) {
            Log.v(TAG, "enableForegroundDispatch");
            mNfcAdapter.enableForegroundDispatch(getActivity(), mPendingIntent, null /*nfcFiltersArray*/, null /*nfcTechLists*/);

            if (mNfcAdapter.isEnabled()) {
                // NFC enabled
                tb_nfc_switch.setChecked(true);
            } else {
                // NFC disabled
                tb_nfc_switch.setChecked(false);
            }

        } else {
            // NFC not available on this phone!!!
            //  showToast(getString(R.string.nfc_not_available));
        }

        //    readNfc();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除布局监听
        AutoFitKeyBoardUtil.getInstance().onDestory();
        // 销毁定位
        destroyLocation();

    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
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