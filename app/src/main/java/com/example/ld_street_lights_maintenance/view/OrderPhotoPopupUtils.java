package com.example.ld_street_lights_maintenance.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Message;
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
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.MainActivity;
import com.example.ld_street_lights_maintenance.fragment.mainfragment.BuleFragment;
import com.example.ld_street_lights_maintenance.util.BlePusher;
import com.example.ld_street_lights_maintenance.util.DensityUtil;

import java.util.Arrays;
import java.util.List;


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

        cd_main_dimming = mPopView.findViewById(R.id.cd_main_dimming);
        cd_auxiliary_dimming = mPopView.findViewById(R.id.cd_auxiliary_dimming);


        Button bt_alarm_clear = mPopView.findViewById(R.id.bt_alarm_clear);
        bt_alarm_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  byte[] funCode = new byte[]{0, 21};
                byte[] data = null;;
                sendOrder(funCode, data);*/

                showProgress("正在写入...");
                byte[] funCode = new byte[]{0, 2};
                byte[] data = new byte[]{05, -86};
                ;
                sendOrder(funCode, data);

            }
        });

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
                ;
                sendOrder(funCode, data);
                Log.i("TAG", "onStopTrackingTouch=" + seekBar.getProgress());


            }
        });

        Button bt_light = mPopView.findViewById(R.id.bt_light);
        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("正在写入...");

               /* cd_main_dimming.isChecked();
                cd_auxiliary_dimming.isChecked();*/

               /* byte[] funCode = new byte[]{0, 05};
                byte[] data = new byte[]{(byte) seekBar.getProgress()};;
                sendOrder(funCode, data);
                Log.i("TAG","onStopTrackingTouch=" +seekBar.getProgress());*/
            }
        });


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
            });
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

    }

    private void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }


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
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(false);// 设置弹出窗口可
        this.setOutsideTouchable(false);
        //   this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // id是你需要点击的控件id之上的地方，来实现点击外围扩散的效果
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
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
}

