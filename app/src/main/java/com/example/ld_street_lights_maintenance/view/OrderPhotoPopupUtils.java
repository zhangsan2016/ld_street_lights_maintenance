package com.example.ld_street_lights_maintenance.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.exception.TimeoutException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.DeviceTiming;
import com.example.ld_street_lights_maintenance.act.FirmwareUpdateAct;
import com.example.ld_street_lights_maintenance.act.MainActivity;
import com.example.ld_street_lights_maintenance.crc.CopyOfcheckCRC;
import com.example.ld_street_lights_maintenance.fragment.mainfragment.BuleFragment;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.BytesUtil;
import com.example.ld_street_lights_maintenance.util.DensityUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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
    public enum RWStart {
        READ,
        WRITE
    }

    public OrderPhotoPopupUtils(Context context) {
        super(context);

        this.mContext = context;

        init(mContext);
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

        // 监听蓝牙通知
        IntentFilter bleNotifyFilter = new IntentFilter(BlePusher.DATA_NOTIFY_FILTER);
        mContext.registerReceiver(bleReceiver, bleNotifyFilter);

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

    private BroadcastReceiver bleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 通知接收数据 ");
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
        mPopView = inflater.inflate(R.layout.order_popup, null);
        int screenHeigh = mContext.getResources().getDisplayMetrics().heightPixels;
        // 设置弹出窗口的高
        ((RelativeLayout) mPopView.findViewById(R.id.rl_oder_popup)).getLayoutParams().height = Math.round(screenHeigh * 0.6f);

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
        Button bt_setting_alarming_protector = mPopView.findViewById(R.id.bt_setting_alarming_protector);
        bt_setting_alarming_protector.setOnClickListener(settingOnclick);
        Button bt_setting_illu_vpt = mPopView.findViewById(R.id.bt_setting_illu_vpt);
        bt_setting_illu_vpt.setOnClickListener(settingOnclick);
        Button bt_setting_electricity_vpt = mPopView.findViewById(R.id.bt_setting_electricity_vpt);
        bt_setting_electricity_vpt.setOnClickListener(settingOnclick);
        Button alarm_lamp_control = mPopView.findViewById(R.id.alarm_lamp_control);
        alarm_lamp_control.setOnClickListener(settingOnclick);


        // 设置下拉 "读写指令" 布局
        txt_data = mPopView.findViewById(R.id.txt_data);
        txt_data.setOnTouchListener(touchListener); // 设置避免滑动冲突
        txt_data.setMovementMethod(ScrollingMovementMethod.getInstance());  // 内容设置滑动效果
        txt_data.setOnLongClickListener(new View.OnLongClickListener() {  // 添加内容拷贝
            @Override
            public boolean onLongClick(View v) {

                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ldgd", txt_data.getText().toString());
                cm.setPrimaryClip(clipData);
                showToast("内容复制成");
                return false;
            }
        });


        Button bt_rw_write = mPopView.findViewById(R.id.bt_rw_write);
        bt_rw_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        Button bt_read_alarm_threshold = mPopView.findViewById(R.id.bt_read_alarm_threshold);
        bt_read_alarm_threshold.setOnClickListener(readOnclick);
        Button bt_rw_read_time = mPopView.findViewById(R.id.bt_rw_read_time);
        bt_rw_read_time.setOnClickListener(readOnclick);
        Button bt_rw_read_alarm_state = mPopView.findViewById(R.id.bt_rw_read_alarm_state);
        bt_rw_read_alarm_state.setOnClickListener(readOnclick);
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
                sendOrder(funCode, data, RWStart.WRITE, true);

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
                byte[] data = {};

                boolean md = cd_main_dimming.isChecked();
                boolean ad = cd_auxiliary_dimming.isChecked();
                if (md && ad) {
                    data = new byte[]{(byte) seekBar.getProgress(), 3};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 主灯辅灯全开");
                } else if (md) {
                    data = new byte[]{(byte) seekBar.getProgress(), 1};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 主灯开");
                } else if (ad) {
                    data = new byte[]{(byte) seekBar.getProgress(), 2};
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 辅灯开");
                } else if (!md && !ad) {
                    showToast("请选择主灯或辅灯~");
                    stopProgress();
                    return;
                }

                sendOrder(funCode, data, RWStart.WRITE, true);
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

                sendOrder(funCode, data, RWStart.WRITE, false);

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
     * @param funCode        功能码
     * @param data           指令
     * @param rwStart        读写标识rwStart
     * @param isListenInform 是否监听蓝牙通知服务
     */
    private void sendOrder(byte[] funCode, byte[] data, final RWStart rwStart, boolean isListenInform) {

        try {
            BlePusher.writeSpliceOrder(funCode, data, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] data) {

                    // 解析数据
                    parseDatas(data);
                    if (rwStart == RWStart.WRITE) {
                        showToast("写入成功~");
                        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 写入 当前读取返回数据成功 " + Arrays.toString(data));
                    } else {
                        showToast("读取成功~");
                        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 读取 当前读取返回数据成功 " + Arrays.toString(data));
                    }
                    stopProgress();
                   /* BlePusher.readSpliceOrder(new BleReadCallback() {
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
                    });*/
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    if (rwStart == RWStart.WRITE) {
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
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE) {
                //按下或滑动时请求父节点不拦截子节点
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
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
      /*  // 重置PopupWindow高度
       int screenHeigh = mContext.getResources().getDisplayMetrics().heightPixels;
        this.setHeight(Math.round(screenHeigh * 0.6f));  // 设置弹出窗口的高*/
        this.setHeight(DensityUtil.getScreenHeight(mContext));// 设置弹出窗口的高
        this.setFocusable(false);// 设置弹出窗口可
        this.setOutsideTouchable(false);
        //   this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // id是你需要点击的控件id之上的地方，来实现点击外围扩散的效果
                int top = mPopView.findViewById(com.example.ld_street_lights_maintenance.R.id.rl_oder_popup).getTop();
                int height = mPopView.findViewById(com.example.ld_street_lights_maintenance.R.id.rl_oder_popup).getHeight();
                int y = (int) event.getY() - top;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top) {
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

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alarm_lamp_popup_item, null, false);
        Button bt_alarm_lamp_off = (Button) view.findViewById(R.id.bt_alarm_lamp_off);
        Button bt_alarm_lamp_on = (Button) view.findViewById(R.id.bt_alarm_lamp_on);
        Button bt_alarm_lamp_flicker = (Button) view.findViewById(R.id.bt_alarm_lamp_flicker);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        // 获取popupWindow布局测量后的宽高
        int[] size = DensityUtil.unDisplayViewSize(view);
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, (v.getWidth() - size[0]) / 2, 0);
        LogUtil.e("xx v.getWidth() = " + v.getWidth() + "  v.getMeasuredWidth() = " + v.getMeasuredWidth() + " view.getWidth() =" + size[0] + "  " + view.getMeasuredWidth());

        //设置popupWindow里的按钮的事件
        bt_alarm_lamp_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                byte[] funCode = new byte[]{0, 43};
                byte[] data = new byte[]{1};
                sendOrder(funCode, data, RWStart.WRITE, true);
            }
        });
        bt_alarm_lamp_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                byte[] funCode = new byte[]{0, 43};
                byte[] data = new byte[]{2};
                sendOrder(funCode, data, RWStart.WRITE, true);
            }
        });
        bt_alarm_lamp_flicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                byte[] funCode = new byte[]{0, 43};
                byte[] data = new byte[]{3};
                sendOrder(funCode, data, RWStart.WRITE, true);
            }
        });
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
                    Intent intentUpdate = new Intent(mContext, FirmwareUpdateAct.class);
                    mContext.startActivity(intentUpdate);
                    break;
                case R.id.bt_setting_illu_vpt: // 照度阈值设置

                    final View illu_vpt_dialog = LayoutInflater.from(mContext).inflate(R.layout.order_seting_illu_vpt_dialog, null);
                    AlertDialog illuVptAlerdialog = new AlertDialog.Builder(mContext)
                            .setTitle("照度阈值设置")
                            .setView(illu_vpt_dialog)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditText ed_lux_limit = illu_vpt_dialog.findViewById(R.id.ed_order_seting_lux_limit);
                                    EditText ed_lux_lowerlimit = illu_vpt_dialog.findViewById(R.id.ed_order_seting_lux_lowerlimit);

                                    if (ed_lux_limit.getText().toString().equals("") || ed_lux_lowerlimit.getText().toString().equals("")) {
                                        showToast("照度阈值设置失败，阈值不能为空！");
                                        return;
                                    }

                                    byte[] lux_limit = BytesUtil.intBytesHL(Integer.parseInt(ed_lux_limit.getText().toString()), 2);
                                    byte[] lux_lowerlimit = BytesUtil.intBytesHL(Integer.parseInt(ed_lux_lowerlimit.getText().toString()), 2);
                                    byte[] data = BytesUtil.byteMergerAll(lux_limit, lux_lowerlimit);

                                    showProgress("正在写入...");
                                    byte[] funCode = new byte[]{0, 84};
                                    sendOrder(funCode, data, RWStart.WRITE, true);

                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case R.id.bt_setting_electricity_vpt: // 电参阈值设置

                    final View electricity_vpt_dialog = LayoutInflater.from(mContext).inflate(R.layout.order_seting_electricity_vpt_dialog, null);
                    AlertDialog electricityVptAlerdialog = new AlertDialog.Builder(mContext)
                            .setTitle("电参阈值设置")
                            .setView(electricity_vpt_dialog)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {


                                    EditText ed_order_seting_voltage_vpt_h = electricity_vpt_dialog.findViewById(R.id.ed_order_seting_voltage_vpt_h);  // 电压上限
                                    EditText ed_order_seting_voltage_vpt_d = electricity_vpt_dialog.findViewById(R.id.ed_order_seting_voltage_vpt_d);  // 电压下限
                                    EditText ed_order_seting_electricity_vpt_h = electricity_vpt_dialog.findViewById(R.id.ed_order_seting_electricity_vpt_h);  // 电流上限
                                    EditText ed_order_seting_electricity_vpt_d = electricity_vpt_dialog.findViewById(R.id.ed_order_seting_electricity_vpt_d);  // 电流下限
                                    EditText ed_order_seting_dcl_vpt = electricity_vpt_dialog.findViewById(R.id.ed_order_seting_dcl_vpt);  // 漏电流阈值

                                    if (ed_order_seting_voltage_vpt_h.getText().toString().equals("") || ed_order_seting_voltage_vpt_d.getText().toString().equals("")
                                            || ed_order_seting_electricity_vpt_h.getText().toString().equals("")
                                            || ed_order_seting_electricity_vpt_d.getText().toString().equals("")
                                            || ed_order_seting_dcl_vpt.getText().toString().equals("")) {
                                        showToast("设置失败，内容不能为空！");

                                        // 条件不成立不能关闭 AlertDialog 窗口
                                        try {
                                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                            field.setAccessible(true);
                                            field.set(dialog, false); // false - 使之不能关闭(此为机关所在，其它语句相同)
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }

                                    showProgress("正在写入...");

                                    byte[] voltage_h = BytesUtil.intBytesHL(Integer.parseInt(ed_order_seting_voltage_vpt_h.getText().toString()), 2);
                                    byte[] voltage_d = BytesUtil.intBytesHL(Integer.parseInt(ed_order_seting_voltage_vpt_d.getText().toString()), 2);
                                    byte[] electricity_h = BytesUtil.intBytesHL(Integer.parseInt(ed_order_seting_electricity_vpt_h.getText().toString()), 2);
                                    byte[] electricity_d = BytesUtil.intBytesHL(Integer.parseInt(ed_order_seting_electricity_vpt_d.getText().toString()), 2);
                                    byte[] dcl_vpt = BytesUtil.intBytesHL(Integer.parseInt(ed_order_seting_dcl_vpt.getText().toString()), 2);
                                    byte[] data = BytesUtil.byteMergerAll(voltage_h, voltage_d, electricity_h, electricity_d, dcl_vpt);

                                    byte[] funCode = new byte[]{0, 9};
                                    sendOrder(funCode, data, RWStart.WRITE, true);

                                    try {
                                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog, true); // false - 使之不能关闭(此为机关所在，其它语句相同)
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog, true); // false - 使之不能关闭(此为机关所在，其它语句相同)
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setCancelable(true)
                            .show();
                    break;
                case R.id.bt_setting_alarming_protector: // 使能电参异常报警

                    final View view = LayoutInflater.from(mContext).inflate(R.layout.order_seting_alarm_enable_dialog, null);
                    AlertDialog myAlerdialog = new AlertDialog.Builder(mContext)
                            .setTitle("报警保护使能")
                            .setView(view)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ToggleButton overvoltage = view.findViewById(R.id.tb_order_setting_alarm_overvoltage);
                                    ToggleButton undervoltage = view.findViewById(R.id.tb_order_setting_alarm_undervoltage);
                                    ToggleButton overcurrent = view.findViewById(R.id.tb_order_setting_alarm_overcurrent);
                                    ToggleButton undercurrent = view.findViewById(R.id.tb_order_setting_alarm_undercurrent);
                                    ToggleButton leakage = view.findViewById(R.id.tb_order_setting_alarm_leakage);
                                    String alarm_enable = (overvoltage.isChecked() ? 1 : 0) + "" + (undervoltage.isChecked() ? 1 : 0) + "" + (overcurrent.isChecked() ? 1 : 0) + "" + (undercurrent.isChecked() ? 1 : 0) + "" + (leakage.isChecked() ? 1 : 0);
                                    alarm_enable = "000" + alarm_enable;
                                    byte alarmByte = BytesUtil.BitToByte(alarm_enable);
                                    Log.e("xx", ">>>>>>>>>>>>>>>>>>>> alarm_enable = " + alarm_enable + " " + alarmByte);

                                    showProgress("正在写入...");
                                    byte[] funCode = new byte[]{0, 80};
                                    byte[] data = new byte[]{alarmByte};
                                    sendOrder(funCode, data, RWStart.WRITE, true);

                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();


                    break;
                case R.id.bt_setting_boot_configon: // 角度倾倒报警-开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 92};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_boot_configoff: // 角度倾倒报警-关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 92};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_collapse_alarmon: // 角度倾倒报警-开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 90};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_collapse_alarmoff: // 角度倾倒报警-关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 90};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_angle_adjust: // 角度校准

                    final View angle_adjust_dialog = LayoutInflater.from(mContext).inflate(R.layout.order_seting_angle_adjust_dialog, null);
                    AlertDialog angleAdjustAlerdialog = new AlertDialog.Builder(mContext)
                            .setTitle("角度校准设置")
                            .setView(angle_adjust_dialog)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    EditText ed_angle_adjust = angle_adjust_dialog.findViewById(R.id.ed_order_seting_angle_adjust);
                                    byte[] data =  new byte[]{10};
                                    if (!ed_angle_adjust.getText().toString().equals("")) {
                                        data = new byte[]{Byte.parseByte(ed_angle_adjust.getText().toString())};
                                    }

                                    showProgress("正在写入...");
                                    byte[] funCode = new byte[]{0, 88};
                                    sendOrder(funCode, data, RWStart.WRITE, true);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();

                    break;
                case R.id.bt_setting_locationon: // 经纬度开灯设置开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 86};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_locationoff: // 经纬度开灯设置关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 86};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_illuon: // 照度开
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 82};
                    data = new byte[]{1};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_setting_illuoff: // 照度关
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 82};
                    data = new byte[]{0};
                    sendOrder(funCode, data, RWStart.WRITE, true);
                    break;
                case R.id.bt_curve_timing: // 曲线定时
                    Intent intent = new Intent(mContext, DeviceTiming.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.bt_factory_reset:  //  恢复原厂设置
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 01};
                    data = new byte[]{-95, -86};
                    sendOrder(funCode, data, RWStart.WRITE, false);
                    break;
                case R.id.bt_reboot:  // 重启
                    showProgress("正在写入...");
                    funCode = new byte[]{0, 01};
                    data = new byte[]{-86, -95};
                    sendOrder(funCode, data, RWStart.WRITE, false);
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

                    sendOrder(funCode, data, RWStart.WRITE, false);
                    break;
                case R.id.alarm_lamp_control:  // 雾灯报警灯开关
                    initPopWindow(v);
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
                case R.id.bt_rw_read_alarm_state:  // 读取报警状态
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 17};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_state:  // 一键读取所有状态信息
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 49};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_config:  // 一键读取所有配置信息
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 47};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_signal_strength:  // 读取信号强度
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 45};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_read_alarm_threshold:  // 读取报警电压电流阈值
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 11};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_time: // 读取时间
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 19};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_ep: // 读取电参
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 25};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_devid: // 读取设备id
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 31};
                    sendOrder(funCode, null, RWStart.READ, true);
                    break;
                case R.id.bt_rw_read_vernum: // 读取设备当前版本号
                    showProgress("正在读取...");
                    funCode = new byte[]{0, 33};
                    sendOrder(funCode, null, RWStart.READ, true);
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

        // addText(txt_data, Arrays.toString(data) + "\n");
        //使用 crc 校验数据
        if (!checkDataCrc(data)) {
            Log.e("xx", "CRC 校验失败~");
            return;
        } else {
            Log.e("xx", "CRC 校验成功~");
        }

        // 根据状态码解析对应的数据
        if (data[2] == 12) { // 返回警报电压电流阈值

            Log.e("xx", "返回警报电压电流阈值");
            // -18, 0, 12, 0, 10, 97, -88, 46, -32, 1, -12, 0, 0, 0, 20, 10, 1, -17, 0, 0
            String txt = "读取报警电压电流阈值：" + "电压高:" + BytesUtil.bytesIntHL(new byte[]{data[5], data[6]}) + " 电压低:" + BytesUtil.bytesIntHL(new byte[]{data[7], data[8]}) + " 电流高:" + BytesUtil.bytesIntHL(new byte[]{data[9], data[10]})
                    + " 电流低:" + BytesUtil.bytesIntHL(new byte[]{data[11], data[12]});
            addText(txt_data, txt);

        } else if (data[2] == 18) {

            Log.e("xx", "返回读警报状态确认");
            // -18, 0, 18, 0, 4, 0, 17, 0, 0, -112, 28, -17, 0, 0, 0, 0, 56, 51, 49, 48
            String txt = "读取警报状态：" + "报警状态:" + data[7] + " 电参报警状态:" + data[8];
            addText(txt_data, txt);

        } else if (data[2] == 20) {

            Log.e("xx", "返回时间");
            String txt = "读取的时间为：" + "20" + data[5] + "年" + data[6] + "月" + data[7] + "日" + " " + data[8] + ":" + data[9] + ":" + data[10] + "\n";
            addText(txt_data, txt);

        } else if (data[2] == 26) {
            Log.e("xx", "返回电参");
            String txt = "读取电参："
                    + " 电压:" + (BytesUtil.bytesIntHL(new byte[]{data[5], data[6]}) / 100
                    + " 电流:" + (BytesUtil.bytesIntHL(new byte[]{data[7], data[8]})) / 100
                    + " 功率:" + (BytesUtil.bytesIntHL(new byte[]{data[9], data[10]})) / 10
                    + " 电能:" + BytesUtil.bytesIntHL(new byte[]{data[11], data[12], data[13], data[14]})
                    + " 功率因数:" + (BytesUtil.bytesIntHL(new byte[]{data[15], data[16]})) / 1000)
                    + "\n";
            addText(txt_data, txt);
        } else if (data[2] == 32) {
            Log.e("xx", "返回设备ID号" + "\n");
            //  [-18, 0, 32, 0, 23, 52, 48, 48, 55, 48, 48, 48, 48, 56, 54, 52, 56, 51, 49, 48, 53, 53, 48, 52, 50, 51, 54, 51, 20, 35, -17, 0, 0, 0, 0, 0, 0, 20, -27, 4]
            //  String txt = "设备ID为："  + data[5] + data[6] + data[7] + data[8] + data[9] + data[10] + data[11] + data[12] + data[13]  + data[14]  + data[15] + data[16] + "\n";
            String txt = null;
            try {
                txt = new String(new byte[]{data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15], data[16]}, "ascii") + "\n";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            addText(txt_data, txt);

        } else if (data[2] == 34) {
            Log.e("xx", "返回设备版本号");
            // -18, 0, 34, 0, 3, 0, 4, 9, -49, 11, -17, 0, 0, 0, 0, 52, 56, 51, 49, 48
            String txt = "当前设备版本为：" + data[5] + "-" + data[6] + "-" + data[7] + "\n";
            addText(txt_data, txt);

        } else if (data[2] == 46) {
            Log.e("xx", "读取信号强度确定");
            // -18, 0, 46, 0, 3, 0, 45, 18, -104, 84, -17, 0, 0, 0, 0, 0, 56, 51, 49, 48
            String txt = "当前设备信号强度为：" + data[5] + "\n";
            addText(txt_data, txt);
        } else if (data[2] == 48) {

            Log.e("xx", "一键读取所有配置信息返回");
            // -18, 0, 48, 0, 56, 17, 0, 100, 20, 0, 80, 23, 0, 30, 1, 0, 40, 4, 0, 80, 9, 0, 0, 18, 0, 0, 21, 23, 0, 1, 0, 0, 3, 0, 0, 5, 0, 0, 8, 0, 0, 97, -88, 46, -32, 1, -12, 0, 0, 0, 20, -1, 1, 0, 30, 0, 5, 1, -60, 0, 1, -65, -56, -17, 0, 0, 0, 0, 77, 81
            StringBuilder txt = new StringBuilder();
            txt.append("一键读取所有配置信息: ");
            txt.append("\n主灯定时: \n");
            for (int i = 0; i < 6; i++) {
                txt.append(+data[(i * 3) + 5] + ":" + data[(i * 3) + 5 + 1] + "   " + data[(i * 3) + 5 + 2] + "%" + "\n");
            }
            txt.append("副灯定时: \n");
            for (int i = 0; i < 6; i++) {
                txt.append(+data[(i * 3) + 23] + ":" + data[(i * 3) + 23 + 1] + "   " + data[(i * 3) + 23 + 2] + "%" + "\n");
            }
            txt.append("电压上限:" + BytesUtil.bytesIntHL(new byte[]{data[41], data[42]}) + "\n");
            txt.append("电压下限:" + BytesUtil.bytesIntHL(new byte[]{data[43], data[44]}) + "\n");
            txt.append("电流上限:" + BytesUtil.bytesIntHL(new byte[]{data[45], data[46]}) + "\n");
            txt.append("电流下限:" + BytesUtil.bytesIntHL(new byte[]{data[47], data[48]}) + "\n");
            txt.append("漏电流上限:" + BytesUtil.bytesIntHL(new byte[]{data[49], data[50]}) + "\n");

            txt.append("电参报警使能:" + BytesUtil.byteToBit(data[51]) + "\n");
            if (data[52] == 1) {
                txt.append("照度开关: 开" + "\n");
            } else {
                txt.append("照度开关: 关" + "\n");
            }
            txt.append("照度上限:" + BytesUtil.bytesIntHL(new byte[]{data[53], data[54]}) + "\n");
            txt.append("照度下限:" + BytesUtil.bytesIntHL(new byte[]{data[55], data[56]}) + "\n");
            if (data[57] == 1) {
                txt.append("经纬度开关: 开" + "\n");
            } else {
                txt.append("经纬度开关: 关" + "\n");
            }
            txt.append("角度精度:" + data[58] + "\n");
            if (data[59] == 1) {
                txt.append("倾斜报警开关: 开" + "\n");
            } else {
                txt.append("倾斜报警开关: 关" + "\n");
            }
            if (data[60] == 1) {
                txt.append("测试模式开关: 开" + "\n");
            } else {
                txt.append("测试模式开关: 关" + "\n");
            }


            addText(txt_data, txt.toString());

        } else if (data[2] == 50) {

            Log.e("xx", "一键读取所有状态信息");
            // -18, 0, 50, 0, 46, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 95, -27, 5, 27, 10, 8, 58, 0, 0, 0, 0, 20, 0, 9, 104, -5, -17, 0, 0, 0, 0, 0, 0
            StringBuilder txt = new StringBuilder();
            txt.append("一键读取所有状态信息: \n");
            txt.append("报警灯状态: " + BytesUtil.byteToBit(data[5]) + "\n");
            txt.append("主灯亮度: " + data[6] + "\n");
            txt.append("副灯亮度: " + data[7] + "\n");
            txt.append("电压: " + (BytesUtil.bytesIntHL(new byte[]{data[8], data[9]})) / 100 + "\n");
            txt.append("电流: " + (BytesUtil.bytesIntHL(new byte[]{data[10], data[11]})) / 100 + "\n");
            txt.append("功率: " + (BytesUtil.bytesIntHL(new byte[]{data[12], data[13]})) / 10 + "\n");
            txt.append("电能: " + BytesUtil.bytesIntHL(new byte[]{data[14], data[15]}) + "\n");
            txt.append("功率因数: " + (BytesUtil.bytesIntHL(new byte[]{data[16], data[17]})) / 1000 + "\n");
            txt.append("漏电流: " + BytesUtil.bytesIntHL(new byte[]{data[18], data[19]}) + "\n");
            txt.append("报警状态: " + BytesUtil.byteToBit(data[20]) + "\n");
            txt.append("版本号: " + data[21] + "_" + data[22] + "_" + data[23] + "\n");
            txt.append("子设备版本号: " + data[24] + data[25] + data[26] + "\n");
            txt.append("经度: " + BytesUtil.bytesIntHL(new byte[]{data[28], data[29]}) + "\n");
            txt.append("纬度: " + BytesUtil.bytesIntHL(new byte[]{data[31], data[32]}) + "\n");
            txt.append("信号值: " + data[33] + "\n");
            txt.append("重启次数: " + BytesUtil.bytesIntHL(new byte[]{data[34], data[35], data[36], data[37]}) + "\n");
            txt.append("当前时间: " + data[38] + "年_" + data[39] + "月_" + data[40] + "日 " + data[41] + ":" + data[42] + ":" + data[43] + "\n");
            txt.append("GPS关灯时间: " + data[44] + ":" + data[45] + "\n");
            txt.append("GPS开灯时间: " + data[46] + ":" + data[47] + "\n");
            txt.append("温度: " + data[48] + "\n");
            txt.append("光照度: " + BytesUtil.bytesIntHL(new byte[]{data[49], data[50]}) + "\n");

            addText(txt_data, txt.toString());

        } else if (data[2] == 81) {
            Log.e("xx", "使能电参异常报警返回");
            String txt = "使能电参异常报警状态:" + data[7];
            addText(txt_data, txt);
        } else if (data[2] == 83) {
            Log.e("xx", "照度开关返回");
        } else if (data[2] == 85) {
            Log.e("xx", "照度阈值设置返回");
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

}

