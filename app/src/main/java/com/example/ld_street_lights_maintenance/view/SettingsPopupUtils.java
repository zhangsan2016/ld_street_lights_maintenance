package com.example.ld_street_lights_maintenance.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.util.DensityUtil;


public class SettingsPopupUtils extends PopupWindow implements
        View.OnClickListener {
    private Button btnTakePhoto, btnSelect, btnCancel, btnDel, btnLook;
    private View mPopView;
    private OnItemClickListener mListener;
    // 点击的是已添加的图片或者是新增加“+”图片的标识来控制是否显示删除和查看按钮
    private int code;
    // 传入的图片地址
    private String path;
    private Context mContext;

    public SettingsPopupUtils(Context context) {
        super(context);

        this.mContext = context;
        init(context);
        setPopupWindow();


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
        mPopView = inflater.inflate(R.layout.settings_popup, null);


      /*  btnTakePhoto = (Button) mPopView
                .findViewById(R.id.choos_photo_popup_pz);
        btnSelect = (Button) mPopView.findViewById(R.id.choos_photo_popup_xc);
        btnCancel = (Button) mPopView.findViewById(R.id.choos_photo_popup_qx);
        btnDel = (Button) mPopView.findViewById(R.id.choos_photo_popup_sc);
        btnLook = (Button) mPopView.findViewById(R.id.choos_photo_popup_ck);
        if (code == 0) {
            btnDel.setVisibility(View.GONE);
            btnLook.setVisibility(View.GONE);
        } else {
            btnDel.setVisibility(View.VISIBLE);
            btnLook.setVisibility(View.VISIBLE);
        }*/
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
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
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
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop() ;
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

