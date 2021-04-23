package com.example.ld_street_lights_maintenance.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.DeviceTiming;
import com.example.ld_street_lights_maintenance.fragment.mainfragment.BuleFragment;
import com.example.ld_street_lights_maintenance.util.BlePusher;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;


public class OrderPhotoPopupUtils extends PopupWindow implements
        View.OnClickListener {
    private Button btnTakePhoto, btnSelect, btnCancel, btnDel, btnLook;
    private View mPopView;
    private OnItemClickListener mListener;
    // 点击的是已添加的图片或者是新增加“+”图片的标识来控制是否显示删除和查看按钮
    private int code;
    // 传入的图片地址
    private String path;
    private Context mContext;
    private CheckBox cd_main_dimming, cd_auxiliary_dimming;

    private ProgressDialog mProgress;
    private TextView txt_data;

    // 读写状态
    private enum RWStart {
        READ,
        WRITE
    }

    public OrderPhotoPopupUtils(Context context) {
        super(context);

        this.mContext = context;

        init(context);
        setPopupWindow();
        initBroadCast();

        //      ((DemoActivity)getActivity()).getmTitle();
        //   btnTakePhoto.setOnClickListener(this);

    }

    /**
     * 动态注册广播
     */
    private void initBroadCast() {

        // 动态注册通知 - 监听蓝牙是否关闭
        IntentFilter filter = new IntentFilter(BuleFragment.DATA_REFRESH_FILTER);
        mContext.registerReceiver(singleLightSettingActReceiver, filter);

    }

    private BroadcastReceiver singleLightSettingActReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 根据action过滤广播内容
            if (BuleFragment.DATA_REFRESH_FILTER.equals(intent.getAction())) {
                stopProgress();
            }
        }
    };


    private void showProgress(String meg) {
        mProgress = ProgressDialog.show(mContext, "", meg);
    }

    private void stopProgress() {
        if (mProgress != null) {
            mProgress.cancel();
        }
    }


    /**
     * 初始化
     *
     * @param context context
     */
    @SuppressLint("InflateParams")
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 绑定布局
        mPopView = inflater.inflate(R.layout.oder_popup, null);

        // 设置下拉 "控制指令" 布局
        ExpandView ev_oder_debug = mPopView.findViewById(R.id.ev_oder_debug);
        View debugCommandView = inflater.inflate(R.layout.debug_command_item, null);
        ev_oder_debug.setExpandView(debugCommandView);


        // 设置下拉 "设置指令" 布局
        ExpandView ev_oder_setting = mPopView.findViewById(R.id.ev_setting);
        View oder_serting_View = inflater.inflate(R.layout.order_setting_item, null);
        ev_oder_setting.setExpandView(oder_serting_View);

        // 恢复原厂设置
        Button bt_factory_reset = mPopView.findViewById(R.id.bt_factory_reset);
        bt_factory_reset.setOnClickListener(settingOnclick);
        // 恢复重启
        Button bt_reboot = mPopView.findViewById(R.id.bt_reboot);
        bt_reboot.setOnClickListener(settingOnclick);
        // 校时
        Button bt_timing = mPopView.findViewById(R.id.bt_timing);
        bt_timing.setOnClickListener(settingOnclick);
        // 曲线定时
        Button bt_curve_timing = mPopView.findViewById(R.id.bt_curve_timing);
        bt_curve_timing.setOnClickListener(settingOnclick);
        Button bt_setting_dufup = mPopView.findViewById(R.id.bt_setting_dufup);
        bt_setting_dufup.setOnClickListener(settingOnclick);
        Button bt_setting_illuon = mPopView.findViewById(R.id.bt_setting_illuon);
        bt_setting_illuon.setOnClickListener(settingOnclick);
        Button bt_setting_illuoff = mPopView.findViewById(R.id.bt_setting_illuoff);
        bt_setting_illuoff.setOnClickListener(settingOnclick);
        Button bt_setting_locationon = mPopView.findViewById(R.id.bt_setting_locationon);
        bt_setting_locationon.setOnClickListener(settingOnclick);
        Button bt_setting_locationoff = mPopView.findViewById(R.id.bt_setting_locationoff);
        bt_setting_locationoff.setOnClickListener(settingOnclick);
        Button bt_setting_angle_adjust = mPopView.findViewById(R.id.bt_setting_angle_adjust);
        bt_setting_angle_adjust.setOnClickListener(settingOnclick);
        Button bt_setting_collapse_alarmon = mPopView.findViewById(R.id.bt_setting_collapse_alarmon);
        bt_setting_collapse_alarmon.setOnClickListener(settingOnclick);
        Button bt_setting_collapse_alarmoff = mPopView.findViewById(R.id.bt_setting_collapse_alarmoff);
        bt_setting_collapse_alarmoff.setOnClickListener(settingOnclick);
        Button bt_setting_boot_configon = mPopView.findViewById(R.id.bt_setting_boot_configon);
        bt_setting_boot_configon.setOnClickListener(settingOnclick);
        Button bt_setting_boot_configoff = mPopView.findViewById(R.id.bt_setting_boot_configoff);
        bt_setting_boot_configoff.setOnClickListener(settingOnclick);


        // 设置下拉 "读写指令" 布局
        txt_data = mPopView.findViewById(R.id.txt_data);
        txt_data.setOnTouchListener(touchListener); // 设置避免滑动冲突
        txt_data.setMovementMethod(ScrollingMovementMethod.getInstance());  // 内容设置滑动效果
        Button bt_rw_write = mPopView.findViewById(R.id.bt_rw_write);
        bt_rw_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText(txt_data,"当前内容为：skdlfjlsdjlfjldskj46464646464646546546484787488979879879879879797984784654");
            }
        });
        Button bt_read_alarm_threshold = mPopView.findViewById(R.id.bt_read_alarm_threshold);
        bt_read_alarm_threshold.setOnClickListener(readOnclick);
        Button bt_rw_read_time = mPopView.findViewById(R.id.bt_rw_read_time);
        bt_rw_read_time.setOnClickListener(readOnclick);
        Button bt_rw_read_ep = mPopView.findViewById(R.id.bt_rw_read_ep);
        bt_rw_read_ep.setOnClickListener(readOnclick);
        Button bt_rw_read_devid = mPopView.findViewById(R.id.bt_rw_read_devid);
        bt_rw_read_devid.setOnClickListener(readOnclick);
        Button bt_rw_read_vernum = mPopView.findViewById(R.id.bt_rw_read_vernum);
        bt_rw_read_vernum.setOnClickListener(readOnclick);
        Button bt_rw_read_signal_strength = mPopView.findViewById(R.id.bt_rw_read_signal_strength);
        bt_rw_read_signal_strength.setOnClickListener(readOnclick);
        Button bt_rw_read_config = mPopView.findViewById(R.id.bt_rw_read_config);
        bt_rw_read_config.setOnClickListener(readOnclick);
        Button bt_rw_read_state = mPopView.findViewById(R.id.bt_rw_read_state);
        bt_rw_read_state.setOnClickListener(readOnclick);
        Button bt_rw_clear = mPopView.findViewById(R.id.bt_rw_clear);
        bt_rw_clear.setOnClickListener(readOnclick);


        cd_main_dimming = mPopView.findViewById(R.id.cd_main_dimming);
        cd_auxiliary_dimming = mPopView.findViewById(R.id.cd_auxiliary_dimming);

        // 清除报警
        Button bt_alarm_clear = mPopView.findViewById(R.id.bt_alarm_clear);
        bt_alarm_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress("正在写入...");
                byte[] funCode = new byte[]{0, 27};
                byte[] data = new byte[]{85, -86};
                sendOrder(funCode, data, RWStart.WRITE);

            }
        });

        // 亮度调节
        SeekBar seekbar = mPopView.findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("TAG", "onProgressChanged=" + progress + "  fromUser = " + fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("TAG", "onStartTrackingTouch=");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                showProgress("正在写入...");

                byte[] funCode = new byte[]{0, 05};
                byte[] data = new byte[]{(byte) seekBar.getProgress(), 3};

                sendOrder(funCode, data, RWStart.WRITE);
                Log.i("TAG", "onStopTrackingTouch=" + seekBar.getProgress());


            }
        });

        // 主辅灯开关灯
        Button bt_light = mPopView.findViewById(R.id.bt_light);
        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress("正在写入...");

                byte[] funCode = new byte[]{0, 05};
                byte[] data = new byte[2];

                boolean md = cd_main_dimming.isChecked();
                boolean ad = cd_auxiliary_dimming.isChecked();
                if (md && ad) {
                    data = new byte[]{100, 3};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 主灯辅灯全开");
                } else if (md) {
                    data = new byte[]{100, 1};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 主灯开");
                } else if (ad) {
                    data = new byte[]{100, 2};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 辅灯开");
                } else if (!md && !ad) {
                    data = new byte[]{0, 3};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 主辅灯关");
                }

                sendOrder(funCode, data, RWStart.WRITE);

               /* byte[] funCode = new byte[]{0, 05};
                byte[] data = new byte[]{(byte) seekBar.getProgress()};;
                sendOrder(funCode, data);
                Log.i("TAG","onStopTrackingTouch=" +seekBar.getProgress());*/
            }
        });


    }

    private void addText(TextView textView, String content) {

        textView.append(content);
        textView.append("\n");

        int offset = textView.getLineCount() * textView.getLineHeight();//判断textview文本的高度
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());//如果文本的高度大于ScrollView,就自动滑动
        }
    }


    /**
     * 发送蓝牙通讯指令
     *
     * @param funCode 功能码
     * @param data    指令
     * @param rwStart 读写标识
     */
    private void sendOrder(byte[] funCode, byte[] data, final RWStart rwStart) {

        try {
            BlePusher.writeSpliceOrder(funCode, data, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                    BlePusher.readSpliceOrder(new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] data) {
                            // 解析数据
                            parseDatas(data);
                            stopProgress();
                            if (rwStart == RWStart.WRITE) {
                                showToast("写入成功~");
                                Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 写入 当前读取返回数据成功 " + Arrays.toString(data));
                            } else {
                                showToast("读取成功~");
                                Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 读取 当前读取返回数据成功 " + Arrays.toString(data));
                            }
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
                    if (rwStart == RWStart.WRITE) {
                        showToast("写入失败" + exception.toString());
                    } else {
                        showToast("读取失败" + exception.toString());
                    }
                    stopProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage().toString());
            stopProgress();
        }
    }


    private void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 解决滑动冲突
     * 设置触摸事件，由于EditView与TextView都处于ScollView中，
     * 所以需要在OnTouch事件中通知父控件不拦截子控件事件
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE){
                //按下或滑动时请求父节点不拦截子节点
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                //抬起时请求父节点拦截子节点
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        }
    };


    /**
     * 设置窗口的相关属性
     */
    @SuppressLint({"InlinedApi", "WrongConstant"})
    private void setPopupWindow() {


        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setClippingEnabled(false);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        this.setContentView(mPopView);// 设置View
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
//        this.setHeight(DensityUtil.getScreenHeight(mContext));// 设置弹出窗口的高
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        // 重置PopupWindow高度
        int screenHeigh = mContext.getResources().getDisplayMetrics().heightPixels;
        this.setHeight(Math.round(screenHeigh * 0.6f));// 设置弹出窗口的高
        this.setFocusable(false);// 设置弹出窗口可
        this.setOutsideTouchable(false);
        //   this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // id是你需要点击的控件id之上的地方，来实现点击外围扩散的效果
                int height = mPopView.findViewById(com.example.ld_street_lights_maintenance.R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v, int code, String path);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.setOnItemClick(v, code, path);
        }
    }


    /**
     * 设置点击事件
     */
    private View.OnClickListener settingOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            byte[] funCode;
            byte[] data;
            switch (v.getId()) {
                case R.id.bt_setting_dufup: // 设备固件升级
                    break;
                case R.id.bt_setting_boot_configon: // 角度倾倒报警-开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 92};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_boot_configoff: // 角度倾倒报警-关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 92};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_collapse_alarmon: // 角度倾倒报警-开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 90};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_collapse_alarmoff: // 角度倾倒报警-关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 90};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_angle_adjust: // 角度校准
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 88};
                    sendOrder(funCode, null, RWStart.WRITE);
                    break;
                case R.id.bt_setting_locationon: // 经纬度开灯设置开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 86};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_locationoff: // 经纬度开灯设置关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 86};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_illuon: // 照度开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 82};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_setting_illuoff: // 照度关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 82};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_curve_timing: // 曲线定时
                    Intent intent = new Intent(mContext, DeviceTiming.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.bt_factory_reset:  //  恢复原厂设置
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 01};
                    data = new byte[]{-95, -86};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_reboot:
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 01};
                    data = new byte[]{-86, -95};
                    sendOrder(funCode, data, RWStart.WRITE);
                    break;
                case R.id.bt_timing:
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 02};
                    data = new byte[7];

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

                    String year = String.valueOf(cal.get(Calendar.YEAR));
                    String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                    String day = String.valueOf(cal.get(Calendar.DATE));
                    String hour;
                    if (cal.get(Calendar.AM_PM) == 0)
                        hour = String.valueOf(cal.get(Calendar.HOUR));
                    else
                        hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
                    String minute = String.valueOf(cal.get(Calendar.MINUTE));
                    String second = String.valueOf(cal.get(Calendar.SECOND));
                    //获取今天是这周的第几天,周日为1,周一为2,周六为7
                    int week = cal.get(Calendar.DAY_OF_WEEK);

                    data[0] = Byte.parseByte(year.substring(2, year.length()));
                    data[1] = Byte.parseByte(month);
                    data[2] = Byte.parseByte(day);
                    data[3] = Byte.parseByte(hour);
                    data[4] = Byte.parseByte(minute);
                    data[5] = Byte.parseByte(second);
                    data[6] = (byte) week;

                    sendOrder(funCode, data, RWStart.WRITE);
                    break;

            }
        }
    };

    /**
     * 读写点击事件
     */
    private View.OnClickListener readOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            byte[] funCode;
            byte[] data;
            switch (v.getId()) {
                case R.id.bt_rw_clear:  // 清空返回信息
                    txt_data.setText("");
                    txt_data.scrollTo(0, 0);
                    break;
                case R.id.bt_rw_read_state:  // 一键读取所有状态信息
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 49};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_config:  // 一键读取所有配置信息
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 47};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_signal_strength:  // 读取信号强度
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 45};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_read_alarm_threshold:  // 读取报警电压电流阈值
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 11};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_time: // 读取时间
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 19};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_ep: // 读取电参
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 25};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_devid: // 读取设备id
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 31};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
                case R.id.bt_rw_read_vernum: // 读取设备当前版本号
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 33};
                    sendOrder(funCode, null, RWStart.READ);
                    break;
            }

        }
    };

    /**
     * 解析数据
     *
     * @param data
     */
    private void parseDatas(byte[] data) {

        // 根据状态码解析对应的数据
        if (data[2] == 12) { // 返回警报电压电流阈值
            Log.e("xx", "返回警报电压电流阈值");
        } else if (data[2] == 20) {
            Log.e("xx", "返回时间");
        } else if (data[2] == 26) {
            Log.e("xx", "返回电参");
        } else if (data[2] == 32) {
            Log.e("xx", "返回设备ID号");
        } else if (data[2] == 34) {
            Log.e("xx", "返回设备版本号");
        } else if (data[2] == 46) {
            Log.e("xx", "读取信号强度确定");
        } else if (data[2] == 48) {
            Log.e("xx", "一键读取所有配置信息返回");
        } else if (data[2] == 50) {
            Log.e("xx", "一键读取所有状态信息返回");
        } else if (data[2] == 83) {
            Log.e("xx", "照度开关返回");
        } else if (data[2] == 87) {
            Log.e("xx", "经纬度开灯设置返回");
        } else if (data[2] == 89) {
            Log.e("xx", "角度校准返回");
        } else if (data[2] == 91) {
            Log.e("xx", "角度倾倒报警返回");
        } else if (data[2] == 93) {
            Log.e("xx", "开机状态配置返回");
        }

    }

}

