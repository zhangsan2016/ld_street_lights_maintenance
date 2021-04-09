package com.example.ld_street_lights_maintenance.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.act.MainActivity;
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

    public OrderPhotoPopupUtils(Context context) {
        super(context);

        this.mContext = context;
        init(context);
        setPopupWindow();

        //      ((DemoActivity)getActivity()).getmTitle();
        //   btnTakePhoto.setOnClickListener(this);

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


        Button bt_alarm_clear = mPopView.findViewById(R.id.bt_alarm_clear);

        bt_alarm_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                // disconnectAllDevice 关闭所有链接设备
                List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
                StringBuffer sb = new StringBuffer();
                sb.append("isBlueEnable =" + bleDevices);
                Log.e("xxx", "  >>>>>>>>>>>>>> " + sb.toString() + v.getId());*/

                Log.e("xxx", ">>>>>>>>>>>>>>>>>>> bt_alarm_clear");

                byte[] funCode = new byte[]{0,05};
                byte[] data = new byte[]{100};
                try {
                    BlePusher.writeSpliceOrder(funCode, data, new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 写入成功 " );

                            BlePusher.readSpliceOrder(new BleReadCallback() {
                                @Override
                                public void onReadSuccess(byte[] data) {
                                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 当前返回数据成功 " + Arrays.toString(data));
                                    if(data[2] == 6 ){
                                        showToast("写入成功~");
                                    }
                                }

                                @Override
                                public void onReadFailure(BleException exception) {
                                     showToast("数据读取失败，请靠近蓝牙设备，或重新连接蓝牙~");
                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            showToast("写入失败" +exception.toString());
                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 写入失败 " +exception.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage().toString());
                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> Exception " +e.getMessage().toString());
                }

                /*try {
                    BlePusher.writeOrder("ee00050001503516ef", new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            BlePusher.readSpliceOrder(new BleReadCallback() {
                                @Override
                                public void onReadSuccess(byte[] data) {

                                    BlePusher.readSpliceOrder(new BleReadCallback() {
                                        @Override
                                        public void onReadSuccess(byte[] data) {

                                            Log.e("xxx", "读取返回 " + Arrays.toString(data));

                                        }

                                        @Override
                                        public void onReadFailure(BleException exception) {

                                        }
                                    });
                                }

                                @Override
                                public void onReadFailure(BleException exception) {
                                    Log.e("xxx", ">>>>>>>>>>>>>>>>>>> onReadFailure2");
                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> onWriteFailure1" +exception.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.toString());
                }*/



            }
        });
    }

    private void showToast(String str){
        Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
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
        if(Build.VERSION.SDK_INT >= 24){
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

