package com.langbai.tdhd.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.langbai.tdhd.R;


public class ApkUpDialog {
    private Context context;
    private Dialog dialog;
    private Button upBtn;
    private ImageView closeImg;
    private ImageView imageBack;
    private TextView title;
    private TextView messageOne;
    private TextView messageTwo;


    private String command;

    public ApkUpDialog(Context context, String command) {
        this.context = context;
        this.command = command;
    }

    public ApkUpDialog builder() {
        // 加载布局  dialog
        View view = LayoutInflater.from(context).inflate(R.layout.view_up_apk_dialog, null);

        // 寻找控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        upBtn = (Button) view.findViewById(R.id.update);
        closeImg = (ImageView) view.findViewById(R.id.close_up);
        imageBack = (ImageView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        messageOne = (TextView) view.findViewById(R.id.message_one);
        messageTwo = (TextView) view.findViewById(R.id.message_two);
        if ("1".equals(command))
            closeImg.setVisibility(View.VISIBLE);
        // 设置为dialog
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        // 设置大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return this;
    }


    public ApkUpDialog setPositiveButton(String tag, final OnClickListener listener) {
        upBtn.setText(tag);
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public ApkUpDialog setContent(int imageBackRes, String title, String messageOne, String messageTwo) {
        imageBack.setBackgroundResource(imageBackRes);
        this.title.setText(title);
        this.messageOne.setText(messageOne);
        this.messageTwo.setText(messageTwo);
        return this;
    }

    public ApkUpDialog setNegativeButton(final OnClickListener listener) {
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }


    public void show() {
        dialog.show();
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
