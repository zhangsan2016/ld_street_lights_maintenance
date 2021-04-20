package com.example.ld_street_lights_maintenance.act;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;

import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseActivity;
import com.example.ld_street_lights_maintenance.common.MyApplication;
import com.example.ld_street_lights_maintenance.entity.DeviceLampJson;
import com.example.ld_street_lights_maintenance.fragment.mainfragment.BuleFragment;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class DeviceTiming extends BaseActivity {
    public static String receiver = "DEVICE_TIMING_RECELVER";
    // 定时开始结束时间
    TextView txtStartTime, txtEndTime;
    // 开始时间小时分钟
    private int startHour, startMinute, endHour, endMinute;
    // 开始时间结束时间字符串
    private String strStartTime = "";
    private String strEndTime = "";
    // 定时开始结束时间设置按钮
    private LinearLayout llStarttime, llEndTime;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private TextView tv_spacing_start_time1, tv_spacing_start_time2,
            tv_spacing_start_time3, tv_spacing_start_time4,
            tv_spacing_start_time5, tv_spacing_start_time6;
    // 确认按钮
    private Button okDevice;
    // 进度百分比
    private TextView tv_progress1, tv_progress2, tv_progress3, tv_progress4,
            tv_progress5, tv_progress6;
    private LinearLayout ll_spacing_start_time1, ll_spacing_start_time2,
            ll_spacing_start_time3, ll_spacing_start_time4,
            ll_spacing_start_time5, ll_spacing_start_time6;
    // 进度条
    private SeekBar sb_brightness1, sb_brightness2, sb_brightness3,
            sb_brightness4, sb_brightness5, sb_brightness6;

    private ProgressDialog mProgress;
    // 定时数据返回标识
    private boolean timingToken;

    // 当前uuid
    private byte[] uuid;
    // 开关灯的小时分钟
    private String startTimeH, startTimeM, endTimeH, endTimeM;
    // 其他四段段调光
    private String timeTwoH, timeTwoM, timeThirH, timeThirM, timeFourH, timeFourM, timeFifH, timeFifM, timeSixH, timeSixM;
    // 亮度
    private int brightness1, brightness2, brightness3, brightness4, brightness5;
    // 主辅灯checkBox
   private CheckBox cb_main,cb_assist;

    //  Progress加载框延迟关闭时间，毫秒
    private long stopProgressTime = 3000;


    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 判断定时是否成功
                    if (!timingToken) {
                        Toast.makeText(DeviceTiming.this, "定时失败!", Toast.LENGTH_SHORT).show();
                        stopProgress();
                    }
                    break;
                case 2:
                    finish();
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.device_timing);
        // 动态注册通知
        IntentFilter filter = new IntentFilter(DeviceTiming.receiver);
        registerReceiver(MyReceiver, filter);

        initView();
        initListeners();
        initVariable();
        // 添加监听
        initBroadCast();

    }


    private void initVariable() {

        // 获取传递的参数
        // 获取传递过来的参数
        Bundle bundle = getIntent().getExtras();

        // 初始化开关灯时间
        initTime();

        // 初始化时间参数
        setTime();

        // 初始化开始时间和结束时间弹窗
        startTimeOrendTimeDialog();


    }

    private void initTime() {

        // 开关灯时间

        // 时间为空时设置一个默认的时间
        startTimeH = "18";
        startTimeM = "00";
        endTimeH = "08";
        endTimeM = "00";
        timeTwoH = "21";
        timeTwoM = "23";
        timeThirH = "01";
        timeThirM = "00";
        timeFourH = "03";
        timeFourM = "00";
        timeFifH = "05";
        timeFifM = "00";
        timeSixH = endTimeH;
        timeSixM = endTimeM;

        return;

    }

    private void startTimeOrendTimeDialog() {

        startTimePickerDialog = new TimePickerDialog(DeviceTiming.this,
                new OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String strStartHour = "" + hourOfDay;
                        String strStartMinute = "" + minute;

                        if (hourOfDay / 10 <= 0) {
                            strStartHour = "0" + strStartHour;
                        }
                        if (minute / 10 <= 0) {
                            strStartMinute = "0" + strStartMinute;
                        }
                        startHour = hourOfDay;
                        startMinute = minute;
                        strStartTime = strStartHour + " : " + strStartMinute;
                        txtStartTime.setText(strStartTime);
                        tv_spacing_start_time1.setText(strStartTime);
                    }
                }, Integer.parseInt(startTimeH), Integer.parseInt(startTimeM), true);

        endTimePickerDialog = new TimePickerDialog(DeviceTiming.this,
                new OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String strEndHour = "" + hourOfDay;
                        String strEndMinute = "" + minute;

                        if (hourOfDay / 10 <= 0) {
                            strEndHour = "0" + strEndHour;
                        }
                        if (minute / 10 <= 0) {
                            strEndMinute = "0" + strEndMinute;
                        }
                        endHour = hourOfDay;
                        endMinute = minute;
                        strEndTime = strEndHour + " : " + strEndMinute;
                        txtEndTime.setText(strEndTime);
                        tv_spacing_start_time6.setText(strEndTime);

                    }
                }, Integer.parseInt(endTimeH), Integer.parseInt(endTimeM), true);
    }

    private void setTime() {

        // 开关灯时间
        txtStartTime.setText(startTimeH + ":" + startTimeM);
        txtEndTime.setText(endTimeH + ":" + endTimeM);

        // 设置六段亮度
        sb_brightness1.setProgress(brightness1);
        sb_brightness2.setProgress(brightness2);
        sb_brightness3.setProgress(brightness3);
        sb_brightness4.setProgress(brightness4);
        sb_brightness5.setProgress(brightness5);

        // 设置六段定时时间
        tv_spacing_start_time1.setText(startTimeH + ":" + startTimeM);
        tv_spacing_start_time2.setText(timeTwoH + ":" + timeTwoM);
        tv_spacing_start_time3.setText(timeThirH + ":" + timeThirM);
        tv_spacing_start_time4.setText(timeFourH + ":" + timeFourM);
        tv_spacing_start_time5.setText(timeFifH + ":" + timeFifM);
        tv_spacing_start_time6.setText(timeSixH + ":" + timeSixM);

        // 六段不可点击
        sb_brightness6.setEnabled(false);

	/*	tv_spacing_start_time1.setText(time[0] + ":" + time[1]);
        sb_brightness1.setProgress(time[2]);
		sb_brightness2.setProgress(time[5]);
		sb_brightness3.setProgress(time[8]);
		sb_brightness4.setProgress(time[11]);
		sb_brightness5.setProgress(time[14]);

		tv_spacing_start_time2.setText(time[3] + ":" + isLessTen(time[4]));
		tv_spacing_start_time3.setText(time[6] + ":" + isLessTen(time[7]) );
		tv_spacing_start_time4.setText(time[9] + ":" + isLessTen(time[10]));
		tv_spacing_start_time5.setText(time[12] + ":" + isLessTen(time[13]));
		tv_spacing_start_time6.setText(time[15] + ":" + isLessTen(time[16]));
		// 六段不可点击
		sb_brightness6.setEnabled(false);*/
    }

    /**
     * 小于10的数字后面加0
     *
     * @return
     */
    private String isLessTen(byte time) {
        String timeStr = Integer.toString(time);
        if (time < 10) {
            timeStr = "0" + time;
        }
        return timeStr;
    }

    private void initListeners() {
        // 进度更新
        sb_brightness1
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        sb_brightness2
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        sb_brightness3
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        sb_brightness4
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        sb_brightness5
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        sb_brightness6
                .setOnSeekBarChangeListener(new MySeekBarChangeListener());
        // 六段时间设置
        ll_spacing_start_time1
                .setOnClickListener(new MyOnclickCancelListener());
        ll_spacing_start_time2
                .setOnClickListener(new MyOnclickCancelListener());
        ll_spacing_start_time3
                .setOnClickListener(new MyOnclickCancelListener());
        ll_spacing_start_time4
                .setOnClickListener(new MyOnclickCancelListener());
        ll_spacing_start_time5
                .setOnClickListener(new MyOnclickCancelListener());
        ll_spacing_start_time6
                .setOnClickListener(new MyOnclickCancelListener());
        // 开始结束时间设置
        llStarttime.setOnClickListener(new MyOnclickCancelListener());
        llEndTime.setOnClickListener(new MyOnclickCancelListener());
        // 设置
        okDevice.setOnClickListener(new MyOnclickCancelListener());

    }


    private void initView() {
       ImageView iv_break =  this.findViewById(R.id.iv_break);
        iv_break.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            // 六个阶段的时间LinearLayout
        ll_spacing_start_time1 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time1);
        ll_spacing_start_time2 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time2);
        ll_spacing_start_time3 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time3);
        ll_spacing_start_time4 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time4);
        ll_spacing_start_time5 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time5);
        ll_spacing_start_time6 = (LinearLayout) this
                .findViewById(R.id.ll_spacing_start_time6);

        sb_brightness1 = (SeekBar) this.findViewById(R.id.sb_brightness1);
        sb_brightness2 = (SeekBar) this.findViewById(R.id.sb_brightness2);
        sb_brightness3 = (SeekBar) this.findViewById(R.id.sb_brightness3);
        sb_brightness4 = (SeekBar) this.findViewById(R.id.sb_brightness4);
        sb_brightness5 = (SeekBar) this.findViewById(R.id.sb_brightness5);
        sb_brightness6 = (SeekBar) this.findViewById(R.id.sb_brightness6);

        // 进度条界面参数初始化
        tv_progress1 = (TextView) this.findViewById(R.id.tv_progress1);
        tv_progress2 = (TextView) this.findViewById(R.id.tv_progress2);
        tv_progress3 = (TextView) this.findViewById(R.id.tv_progress3);
        tv_progress4 = (TextView) this.findViewById(R.id.tv_progress4);
        tv_progress5 = (TextView) this.findViewById(R.id.tv_progress5);
        tv_progress6 = (TextView) this.findViewById(R.id.tv_progress6);

        // 六个阶段的textView
        tv_spacing_start_time1 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time1);
        tv_spacing_start_time2 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time2);
        tv_spacing_start_time3 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time3);
        tv_spacing_start_time4 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time4);
        tv_spacing_start_time5 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time5);
        tv_spacing_start_time6 = (TextView) this
                .findViewById(R.id.tv_spacing_start_time6);

        // 主灯辅灯选项
         cb_main  = this.findViewById(R.id.cb_main);
        cb_assist  = this.findViewById(R.id.cb_assist);

        txtStartTime = (TextView) findViewById(R.id.txt_device_main_start_time);
        txtEndTime = (TextView) findViewById(R.id.txt_device_main_end_time);

        okDevice = (Button) this.findViewById(R.id.btn_ok_device_main);

        // 开始结束时间设置
        llStarttime = (LinearLayout) this
                .findViewById(R.id.ll_device_main_start_time);
        llEndTime = (LinearLayout) this
                .findViewById(R.id.ll_device_main_end_time);

    }

    private class MySeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.sb_brightness1:
                    tv_progress1.setText(progress + "%");
                    break;
                case R.id.sb_brightness2:
                    tv_progress2.setText(progress + "%");
                    break;
                case R.id.sb_brightness3:
                    tv_progress3.setText(progress + "%");
                    break;
                case R.id.sb_brightness4:
                    tv_progress4.setText(progress + "%");
                    break;
                case R.id.sb_brightness5:
                    tv_progress5.setText(progress + "%");
                    break;
                case R.id.sb_brightness6:
                    System.out.println("sb_brightness6");
                    tv_progress6.setText(progress + "%");
                    break;

            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    private class MyOnclickCancelListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent;
            switch (v.getId()) {
                case R.id.btn_ok_device_main:

                    new Thread() {
                        public void run() {
                            getTimingParameter();
                        }

                    }.start();

                    break;
                case R.id.ll_spacing_start_time1:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time1.setText(strEndTime);
                                    txtStartTime.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time1.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time1.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_spacing_start_time2:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time2.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time2.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time2.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_spacing_start_time3:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time3.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time3.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time3.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_spacing_start_time4:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time4.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time4.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time4.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_spacing_start_time5:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time5.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time5.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time5.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_spacing_start_time6:
                    new TimePickerDialog(DeviceTiming.this,
                            new OnTimeSetListener() {
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    String strEndHour = "" + hourOfDay;
                                    String strEndMinute = "" + minute;

                                    if (hourOfDay / 10 <= 0) {
                                        strEndHour = "0" + strEndHour;
                                    }
                                    if (minute / 10 <= 0) {
                                        strEndMinute = "0" + strEndMinute;
                                    }
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    strEndTime = strEndHour + " : " + strEndMinute;
                                    tv_spacing_start_time6.setText(strEndTime);
                                    txtEndTime.setText(strEndTime);
                                }
                            }, Integer.parseInt(tv_spacing_start_time6.getText()
                            .toString().trim().split(":")[0].trim()),
                            Integer.parseInt(tv_spacing_start_time6.getText()
                                    .toString().trim().split(":")[1].trim()), true)
                            .show();
                    break;
                case R.id.ll_device_main_start_time:
                    startTimePickerDialog.show();
                    break;
                case R.id.ll_device_main_end_time:
                    endTimePickerDialog.show();
                    break;
            }
        }

    }

    private byte[] timeData;

    private void getTimingParameter() {

        // 显示加载框
        showProgress();


        // 六段调光时间
        String timingTime1 = tv_spacing_start_time1.getText().toString().replaceAll(" ", "");
        String timingTime2 = tv_spacing_start_time2.getText().toString().replaceAll(" ", "");
        String timingTime3 = tv_spacing_start_time3.getText().toString().replaceAll(" ", "");
        String timingTime4 = tv_spacing_start_time4.getText().toString().replaceAll(" ", "");
        String timingTime5 = tv_spacing_start_time5.getText().toString().replaceAll(" ", "");
        String timingTime6 = tv_spacing_start_time6.getText().toString().replaceAll(" ", "");

        // 六段调光亮度
        int progress1 = sb_brightness1.getProgress();
        int progress2 = sb_brightness2.getProgress();
        int progress3 = sb_brightness3.getProgress();
        int progress4 = sb_brightness4.getProgress();
        int progress5 = sb_brightness5.getProgress();
        int progress6 = sb_brightness6.getProgress();

        String aa = timingTime1.split(":")[0];
        String a2a = timingTime1.split(":")[1];


        byte[] funCode = new byte[]{0, 19};
        // 时0 分0 亮度0 时1 分1 亮度1 时2 分2 亮度2 时3 分3 亮度3 时4 分4 亮4 时5 分5 亮度5 灯具位
        byte[] data = new byte[19];

        data[0] = Byte.parseByte(timingTime1.split(":")[0]);
        data[1] = Byte.parseByte(timingTime1.split(":")[1]);
        data[2] = (byte) progress1;

        data[3] = Byte.parseByte(timingTime2.split(":")[0]);
        data[4] = Byte.parseByte(timingTime2.split(":")[1]);
        data[5] = (byte) progress2;

        data[6] = Byte.parseByte(timingTime3.split(":")[0]);
        data[7] = Byte.parseByte(timingTime3.split(":")[1]);
        data[8] = (byte) progress3;

        data[9] = Byte.parseByte(timingTime4.split(":")[0]);
        data[10] = Byte.parseByte(timingTime4.split(":")[1]);
        data[11] = (byte) progress4;

        data[12] = Byte.parseByte(timingTime5.split(":")[0]);
        data[13] = Byte.parseByte(timingTime5.split(":")[1]);
        data[14] = (byte) progress5;

        data[15] = Byte.parseByte(timingTime6.split(":")[0]);
        data[16] = Byte.parseByte(timingTime6.split(":")[1]);
        data[17] = (byte) progress6;

        if(cb_main.isChecked() && cb_assist.isChecked()){
            data[18] = 3;
        }else if(cb_main.isChecked()){
            data[18] = 1;
        }else if(cb_assist.isChecked()){
            data[18] = 2;
        }else{
            showToast("请选择主灯或辅灯定时~");
            return;
        }

        sendOrder(funCode, data);


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
                            //  parseDatas(data);
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

    /**
     * 广播接收
     */
    private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            byte[] data = intent.getByteArrayExtra("data");
            if (data[13] == -62) {
                if (data[15] == 0) {
                    String msg = "";
                    if (data[16] == 1) {
                        msg = "主灯定时设置成功！";
                    } else {
                        msg = "辅灯定时设置成功！";
                    }
                    Toast.makeText(DeviceTiming.this, msg, Toast.LENGTH_SHORT).show();
                    timingToken = true;
                    stopProgress();


                    myHandler.sendEmptyMessage(2);
                }
            }


        }
    };

/*    private void showProgress() {
        mProgress = ProgressDialog.show(this, "", "Loading...", true, false);
    }

    private void stopProgress() {
        if (mProgress != null) {
            mProgress.dismiss();
        }
    }*/


    /**
     * 动态注册广播
     */
    private void initBroadCast() {

        // 动态注册通知 - 监听蓝牙是否关闭
        IntentFilter filter = new IntentFilter(BuleFragment.DATA_REFRESH_FILTER);
        this.registerReceiver(singleLightSettingActReceiver, filter);

    }

    private BroadcastReceiver singleLightSettingActReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 根据action过滤广播内容
            if (BuleFragment.DATA_REFRESH_FILTER.equals(intent.getAction())) {
                stopProgress();
                showToast("蓝牙断开，请重新连接再试");
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 关闭接收者
        unregisterReceiver(MyReceiver);

    }

}
