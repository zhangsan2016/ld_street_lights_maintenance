package com.example.ld_street_lights_maintenance.act;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.OrderUtil;

import org.w3c.dom.Text;

import java.util.Arrays;

public class OperationAct extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgress;
    private  EditText et_writedata;
    private Button btn_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        initView();

    }

    private void initView() {
         et_writedata = this.findViewById(R.id.et_writedata);
         btn_write =  this.findViewById(R.id.btn_write);
        btn_write.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_write:
                String data = et_writedata.getText().toString();
                sendOrder(data);
                break;
        }

    }


    /**
     * 发送蓝牙通讯指令
     *
     * @param funCode 功能码
     * @param data    指令
     */
    private void sendOrder(byte[] funCode, byte[] data) {

        try {
            BlePusher.writeSpliceOrder(funCode, data, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                    BlePusher.readSpliceOrder(new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] data) {
                            showToast("写入成功~");
                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 当前读取返回数据成功 " + Arrays.toString(data));
                            // 解析数据
                            parseDatas(data);
                            stopProgress();
                        }

                        @Override
                        public void onReadFailure(BleException exception) {
                            showToast("数据读取失败，请靠近蓝牙设备，或重新连接蓝牙~");
                            stopProgress();
                        }
                    });
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    showToast("写入失败" + exception.toString());
                    stopProgress();
                }
            },true);
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage().toString());
            stopProgress();
        }
    }

    /**
     * 发送十六进制
     *
     */
    private void sendOrder(String hexData) {

        try {
            BlePusher.writeOrder(hexData, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                    BlePusher.readSpliceOrder(new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] data) {
                            showToast("写入成功~");
                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 当前读取返回数据成功 " + Arrays.toString(data));
                            // 解析数据
                            parseDatas(data);
                            stopProgress();
                        }

                        @Override
                        public void onReadFailure(BleException exception) {
                            showToast("数据读取失败，请靠近蓝牙设备，或重新连接蓝牙~");
                            stopProgress();
                        }
                    });
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    showToast("写入失败" + exception.toString());
                    stopProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage().toString());
            stopProgress();
        }
    }




    private void parseDatas(byte[] data) {
    }

    private void showProgress(String meg) {
        mProgress = ProgressDialog.show(this, "", meg);
    }

    private void stopProgress() {
        if (mProgress != null) {
            mProgress.cancel();
        }
    }
    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}