package com.example.ld_street_lights_maintenance.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.exception.TimeoutException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.crc.CopyOfcheckCRC;
import com.example.ld_street_lights_maintenance.entity.FirmwareJson;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.BytesUtil;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.view.OrderPhotoPopupUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.ld_street_lights_maintenance.util.BlePusher.spliceOder;

public class FirmwareUpdateAct extends BaseActivity {
    private Spinner sp_firmware;
    private TextView tv_endpoint;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();
    // 固件包地址
    private File firmwarePackageFile = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_firmware_update);


        initView();


    }

    private void initView() {
        ImageView iv_break = this.findViewById(R.id.iv_firmware_update_break);
        iv_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


    /**
     * 服务器获取所有固件包
     *
     * @param view
     */
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

        if (sp_firmware.getSelectedItem() == null) {
            showToast("请先获取获取升级文件~");
            return;
        }

        showProgress();
        String url = "http://asset.sz-luoding.com/" + sp_firmware.getSelectedItem().toString();
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

                // 升级
                upgrade(response.body());

                stopProgress();
            }
        });

    }


    private void upgrade(ResponseBody body) {

        // 1.下载文件
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

            if (sum == totalSize) {
                showToast(downloadFile + "固件下载完成");
                // 保存固件包地址
                firmwarePackageFile = file;
                // 2.发送蓝牙协议请求升级
                sendOrderRequest(downloadFile, totalSize);

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


    /**
     * 发送命令，请求更新
     *
     * @param downloadFile 文件名
     * @param totalSize    固件包大小
     */
    private void sendOrderRequest(String downloadFile, long totalSize) {

        //  1_1_0_4_7.bin
        String[] infos = downloadFile.split("_");
        if (infos.length != 5) {
            showToast("固件包有误~");
            return;
        }

        // 功能码
        byte[] funCode = new byte[]{0, 35};
        // 设备类型
        byte deviceType = Byte.parseByte(infos[0]);
        byte[] data = new byte[]{deviceType};
        // 固件id
        byte[] firmwareId = BytesUtil.intBytesHL(Integer.parseInt(infos[1]), 2);
        data = BytesUtil.byteMergerAll(data, firmwareId);
        // 版本号 高、中、 低
        data = BytesUtil.byteMergerAll(data, new byte[]{Byte.parseByte(infos[2]), Byte.parseByte(infos[3]), Byte.parseByte(infos[4].substring(0, infos[4].indexOf(".")))});
        // 固件包大小
        byte[] totalSizeBt = BytesUtil.intBytesHL((int) totalSize, 4);
        data = BytesUtil.byteMergerAll(data, totalSizeBt);
        sendOrder(funCode, data, OrderPhotoPopupUtils.RWStart.WRITE, true);


    }

    /**
     * 发送蓝牙通讯指令
     *
     * @param funCode        功能码
     * @param data           指令
     * @param rwStart        读写标识rwStart
     * @param isListenInform 是否监听蓝牙通知服务
     */
    private void sendOrder(byte[] funCode, byte[] data, final OrderPhotoPopupUtils.RWStart rwStart, boolean isListenInform) {

        try {
            BlePusher.writeSpliceOrder(funCode, data, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] data) {

                    // 解析数据
                    parseDatas(data);
                    if (rwStart == OrderPhotoPopupUtils.RWStart.WRITE) {
                        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 写入 当前读取返回数据成功 " + Arrays.toString(data));
                    } else {
                        showToast("读取成功~");
                        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 读取 当前读取返回数据成功 " + Arrays.toString(data));
                    }
                    stopProgress();
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    if (rwStart == OrderPhotoPopupUtils.RWStart.WRITE) {
                        if (exception instanceof TimeoutException) {
                            showToast("写入失败: 当前蓝牙信号较弱，请尝试靠近~");
                        } else {
                            showToast("写入失败:" + exception.toString());
                        }
                    } else {
                        if (exception instanceof TimeoutException) {
                            showToast("读取失败: 当前蓝牙信号较弱，请尝试靠近~");
                        } else {
                            showToast("读取失败" + exception.toString());
                        }

                    }
                    stopProgress();
                }
            }, isListenInform);

        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage().toString());
            stopProgress();
        }
    }


    /**
     * 解析数据
     *
     * @param data
     */
    private void parseDatas(byte[] data) {

        //使用 crc 校验数据
        if (!checkDataCrc(data)) {
            Log.e("xx", "CRC 校验失败~");
            return;
        } else {
            Log.e("xx", "CRC 校验成功~");
        }

        // 根据状态码解析对应的数据
        if (data[2] == 36) { // 返回设备固件升级确认
            //  [-18, 0, 36, 0, 3, 0, 32, 1, 111, 17, -17, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            if (data[7] == 1) {
                // 确认升级
                Log.e("xx", "确认升级~");
                // 获取更新包每次发送承接的长度
                int sendLeng = BytesUtil.bytesIntHL(new byte[]{data[5], data[6]});
                // 发送固件
                sendFirmware(sendLeng);

            } else {
                // 固件包有误
                showToast("升级失败固件包参数有误~");
            }

        } else if (data[2] == 38) { // 返回设备固件数据包确认
            // -18, 0, 38, 0, 2, 0, -128, -87, -68, -17
            int index = BytesUtil.bytesIntHL(new byte[]{data[5], data[6]});
            Log.e("xx", "固件升级返回 " + "(" + index + ")" + Arrays.toString(data));

         /*   final byte[] funCode = new byte[]{0, 37};
            byte[] packageNumber = BytesUtil.intBytesHL(index + 1, 2); // 包序号
            final byte[] dataUp = BytesUtil.byteMergerAll(packageNumber, (byte[]) datas[index]);
            sendOrder(funCode, dataUp, OrderPhotoPopupUtils.RWStart.WRITE, true);*/

        }

    }


    /**
     * 发送固件包到下位机升级固件
     *
     * @param sendLeng 发送包的长度
     */
    Object[] datas = new Object[]{};

    private void sendFirmware(final int sendLeng) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (firmwarePackageFile != null) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(firmwarePackageFile);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        fis.close();

                        Log.e("xx", "buffer = " + Arrays.toString(buffer));

                        /*
                        final Object[] objdata = BytesUtil.splitAry(buffer, sendLeng - 10);
                        datas = objdata; // 保存拆分数据
                        //   包序号_高8位 包序号_低8位 数据包（根据数据包大小决定）
                        final byte[] funCode = new byte[]{0, 37};
                        byte[] packageNumber = new byte[]{0, 0}; // 包序号
                        final byte[] data = BytesUtil.byteMergerAll(packageNumber, (byte[]) objdata[0]);
                        sendOrder(funCode, data, OrderPhotoPopupUtils.RWStart.WRITE, true);
                        */

                        try {
                            BlePusher.writeUpdate(buffer, sendLeng, new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] data) {

                                    // 解析数据
                                   // parseDatas(data);
                                    showToast("固件升级成功~");
                                    stopProgress();
                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    if (exception instanceof TimeoutException) {
                                        showToast("固件升级失败: 当前蓝牙信号较弱，请尝试靠近~");
                                    } else {
                                        showToast("固件升级失败:" + exception.toString());
                                    }
                                    stopProgress();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * 使用 crc 校验数据
     *
     * @param data
     */
    private boolean checkDataCrc(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == -17) {
                byte[] checkData = new byte[i - 2];
                System.arraycopy(data, 0, checkData, 0, i - 2);
                return CopyOfcheckCRC.checkTheCrc(checkData, new byte[]{data[i - 2], data[i - 1]});
            }
        }
        return false;
    }

    public void check(View view) {
        BlePusher.check();
    }
}
