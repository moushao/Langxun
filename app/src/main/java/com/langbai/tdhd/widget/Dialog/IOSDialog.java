package com.langbai.tdhd.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.utils.ScreenUtil;


/**
 * Created by Mou on 2017/6/20.
 */

public class IOSDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private TextView mTitle;
    private TextView mCareme;
    private TextView mAlbum;
    private TextView mCancle;
    private TextView mWords;
    private View mOther;
    private View view1;
    private View view2;
    private View view3;
    private boolean isOutSide = true;

    public IOSDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IOSDialog builder() {
        // 加载布局  dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ios, null);
        // 寻找控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        mTitle = (TextView) view.findViewById(R.id.titel);
        mCareme = (TextView) view.findViewById(R.id.camera);
        mAlbum = (TextView) view.findViewById(R.id.album);
        mCancle = (TextView) view.findViewById(R.id.cancel);
        mWords = (TextView) view.findViewById(R.id.characters);
        mOther = view.findViewById(R.id.other_view);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOutSide) {
                    dialog.dismiss();
                }
            }
        });
        // 设置为dialog
        dialog = new Dialog(context, R.style.MyDialogStyle_top);
        dialog.setContentView(view);

        // 设置大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(ScreenUtil.getScreenWidth(context), LinearLayout
                .LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 是否可以取消
     */
    public IOSDialog setCancelable(boolean cancel) {
        this.isOutSide = cancel;
        return this;
    }

    /**
     * 初始化相机
     */
    public IOSDialog setCareme(String text, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mCareme.setText("取消");
        } else {
            mCareme.setText(text);
        }
        mCareme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;

    }

    /**
     * 初始化相机
     */
    public IOSDialog setTitle(String title) {
        mTitle.setText(title);
        return this;

    }

    /**
     * 初始化相册
     */
    public IOSDialog setAlbum(String content, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(content)) {
            mAlbum.setText("取消");
        } else {
            mAlbum.setText(content);
        }
        mAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;

    }

    /**
     * 初始化相册
     */
    public IOSDialog setWord(String content, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(content)) {
            mWords.setText("取消");
        } else {
            mWords.setText(content);
        }
        mWords.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        mWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;

    }

    /**
     * 初始化取消按钮
     */
    public IOSDialog setCancle(String content, final View.OnClickListener listener) {
        if (TextUtils.isEmpty(content)) {
            mCancle.setText("取消");
        } else {
            mCancle.setText(content);
        }
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;

    }

    public IOSDialog setDialogShowBottom() {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        dialogWindow.setAttributes(lp);
        return this;
    }


    public void show() {
        dialog.show();
    }

    public IOSDialog setOtherViewVisiable() {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        mTitle.setVisibility(View.GONE);
        mCareme.setVisibility(View.GONE);
        return this;
    }
}
