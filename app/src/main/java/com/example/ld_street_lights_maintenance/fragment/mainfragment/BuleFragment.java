package com.example.ld_street_lights_maintenance.fragment.mainfragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.MainActivity;
import com.example.ld_street_lights_maintenance.act.OperationAct;
import com.example.ld_street_lights_maintenance.adapter.DeviceAdapter;
import com.example.ld_street_lights_maintenance.base.BaseFragment;
import com.example.ld_street_lights_maintenance.comm.ObserverManager;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.view.MyRadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BuleFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    public static final String DATA_REFRESH_FILTER = "street_lights_maintenance_blestart";

    private Button btn_scan;
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_blue,
                container, false);

        mContext =  getActivity();

        initView(rootView);


        return rootView;
    }


    public void showProgress(String meg) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置进度条样式环形精度条
        }
        progressDialog.setMessage(meg);
        progressDialog.show();

    }

    public void stopProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


    private void initView(View rootView) {


        btn_scan = (Button) rootView.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

        //  progressDialog.setCancelable(false);

        // 初始化蓝牙工具类
        BleManager.getInstance().init(getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);


        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {

            // 连接当前蓝牙设备
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan(); // 关闭蓝牙扫描
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                   /* Intent intent = new Intent(getActivity(), OperationActivity.class);
                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                    startActivity(intent);*/
                    Intent intent = new Intent(mContext, OperationAct.class);
                    mContext.startActivity(intent);
                }
            }
        });

        ListView listView_device = (ListView) rootView.findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);

    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

                BleManager.getInstance().disconnectAllDevice();  // 清空所有已连接列表
                showProgress("正在连接中...");

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                btn_scan.setText(getString(R.string.start_scan));
                stopProgress();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            // 连接成功
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                stopProgress();
                //  mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.addDeviceTop(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                // 将连接成功的蓝牙设备更新到 MainActivity 中
                ((MainActivity) getActivity()).setBleDevice(bleDevice);

                StartNotify(bleDevice);

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    showToast(getString(R.string.active_disconnected));

                } else {
                    showToast(getString(R.string.disconnected));
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

                // 发送蓝牙状态广播
                Intent intent = new Intent();
                intent.setAction(DATA_REFRESH_FILTER);
                BuleFragment.this.getActivity().sendBroadcast(intent);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void StartNotify(BleDevice bleDevice) {
        BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        // 获取服务
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(BlePusher.serviceUuid));
        final BluetoothGattCharacteristic notify = service.getCharacteristic(UUID.fromString(BlePusher.notifyUuid));

        BleManager.getInstance().notify(
                bleDevice,
                notify.getService().getUuid().toString(),
                notify.getUuid().toString(),
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {

                        Log.e("xxx", " notify success " );
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {

                        Log.e("xxx", " notify onNotifyFailure " + exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        Log.e("xxx", " notify onCharacteristicChanged " + Arrays.toString(data));
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (btn_scan.getText().equals(getString(R.string.start_scan))) {
                    checkPermissions();
                } else if (btn_scan.getText().equals(getString(R.string.stop_scan))) {
                    BleManager.getInstance().cancelScan();
                }
                break;


        }
    }


    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            showToast(getString(R.string.please_open_blue));
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }


    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    startScan();
                }
                break;
        }
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {

            // 开始寻找蓝牙
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                btn_scan.setText(getString(R.string.stop_scan));
                showProgress("正在扫描...");
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

                if (bleDevice.getDevice().toString().contains("84:C2:E4")) {
                    mDeviceAdapter.addDevice(bleDevice);
                    mDeviceAdapter.notifyDataSetChanged();
                }
            }

            // 扫描结束
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                btn_scan.setText(getString(R.string.start_scan));
                stopProgress();
            }
        });
    }


    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁时清空所有蓝牙连接
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // showConnectedDevice();
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }
}