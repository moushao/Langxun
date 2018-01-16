package com.langbai.tdhd.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.utils.ScreenUtil;


/**
 * Created by Mou on 2017/6/20.
 */

public class CommentDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private TextView mTotal;
    private TextView mPrice;
    private TextView mVolume;
    private TextView mConfirm;
    private EditText mPass;
    private ImageView mClose;
    private View mOther;
    private LinearLayout code_layout;
    private EditText mCode;
    private TextView mGetCode;
    private boolean isOutSide = false;
    private CountDownTimer timer;

    public CommentDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public CommentDialog builder() {
        // 加载布局  dialog
        View view = LayoutInflater.from(context).inflate(R.layout.comment_dialog, null);
        // 寻找控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);

        // 设置为dialog
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
//        // 设置大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(ScreenUtil.getScreenWidth(context), 50));
        return this;
    }


    public void show() {
        dialog.show();
    }


}
