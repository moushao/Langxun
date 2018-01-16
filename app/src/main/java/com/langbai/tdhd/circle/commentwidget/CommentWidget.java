package com.langbai.tdhd.circle.commentwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.circle.ClickableSpanEx;
import com.langbai.tdhd.circle.SpannableStringBuilderCompat;


/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论控件
 */
public class CommentWidget extends TextView {
    private static final String TAG = "CommentWidget";
    //用户名颜色
    private int textColor = 0xff517fae;
    private static final int textSize = 14;
    SpannableStringBuilderCompat mSpannableStringBuilderCompat;
    private int commentPositon;

    public CommentWidget(Context context, int commentPositon) {
        this(context, commentPositon, null);
    }

    public CommentWidget(Context context, int commentPositon, AttributeSet attrs) {
        this(context, commentPositon, attrs, 0);
    }

    public CommentWidget(Context context, int commentPositon, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.commentPositon = commentPositon;

        setMovementMethod(LinkMovementMethod.getInstance());
        setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    public void setCommentText(CommentBean info) {
        if (info == null)
            return;
        try {
            setTag(info);
            createCommentStringBuilder(info);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "虽然在下觉得不可能会有这个情况，但还是捕捉下吧，万一被打脸呢。。。");
        }
    }

    private void createCommentStringBuilder(@NonNull CommentBean info) {
        if (mSpannableStringBuilderCompat == null) {
            mSpannableStringBuilderCompat = new SpannableStringBuilderCompat();
        } else {
            mSpannableStringBuilderCompat.clear();
            mSpannableStringBuilderCompat.clearSpans();
        }
        String content = ": " + info.getContent() + "\0";
        boolean isApply = (info.getCommentID() == 0);
        // 用户B为空，证明是一条原创评论
        if (isApply) {
            CommentClick userA = new CommentClick.Builder(getContext(), info).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(info.getFormName(), userA, 0);
            mSpannableStringBuilderCompat.append(content);
        } else if (!isApply) {
            //用户A，B不空，证明是回复评论
            CommentClick userA = new CommentClick.Builder(getContext(), info).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(info.getFormName(), userA, 0);
            mSpannableStringBuilderCompat.append(" 回复 ");
            CommentClick userB = new CommentClick.Builder(getContext(), info).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .build();
            mSpannableStringBuilderCompat.append(info.getToName(), userB, 0);
            mSpannableStringBuilderCompat.append(content);
        }
        setText(mSpannableStringBuilderCompat);
    }

    public CommentBean getData() throws ClassCastException {
        return (CommentBean) getTag();
    }

    public int getCommentPositon() {
        return commentPositon;
    }
}