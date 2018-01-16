package com.langbai.tdhd.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.langbai.tdhd.R;
import com.langbai.tdhd.event.PickerListener;
import com.langbai.tdhd.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by Mou on 2017/6/20.
 */

public class ButtomDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private RecyclerView mRecycler;
    private PickerListener mPicker;
    /**
     * 宽度比
     */
    private float widthRatio = 0.80f;

    public ButtomDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ButtomDialog builder() {
        // 加载布局  dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_buttom, null);
        // 寻找控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        mRecycler = (RecyclerView) view.findViewById(R.id.name_list);

        // 设置为dialog
        dialog = new Dialog(context, R.style.BottomDialogStyle);
        dialog.setContentView(view);

        // 设置大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(ScreenUtil.getScreenWidth(context), (int) (ScreenUtil
                .getScreenHeight(context) / 2.8)));
                
        return this;
    }

    public ButtomDialog setDialogShowBottom() {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        dialogWindow.setAttributes(lp);
        return this;

    }

    public ButtomDialog setPickerListener(PickerListener pickerListener) {
        this.mPicker = pickerListener;
        return this;
    }

    public void show() {
        dialog.show();
    }
}
