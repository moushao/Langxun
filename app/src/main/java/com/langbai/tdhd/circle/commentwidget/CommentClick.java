package com.langbai.tdhd.circle.commentwidget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;

import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.circle.ClickableSpanEx;
import com.langbai.tdhd.utils.DimenUtil;


/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论点击事件
 */
public class CommentClick extends ClickableSpanEx {
    private Context mContext;
    private int textSize;
    private CommentBean mUserInfo;
    private SquareCommentBean mSquareCommentBean;

    private CommentClick() {
    }

    private CommentClick(Builder builder) {
        super(builder.color, builder.clickEventColor);
        mContext = builder.mContext;
        mUserInfo = builder.mUserInfo;
        mSquareCommentBean = builder.mSquareCommentBean;
        this.textSize = builder.textSize;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(textSize);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public static class Builder {
        private int color;
        private Context mContext;
        private int textSize = 16;
        private CommentBean mUserInfo;
        private SquareCommentBean mSquareCommentBean;
        private int clickEventColor;

        public Builder(Context context, @NonNull CommentBean info) {
            mContext = context;
            mUserInfo = info;
        }

        public Builder(Context context, @NonNull SquareCommentBean info) {
            mContext = context;
            mSquareCommentBean = info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = (int) DimenUtil.sp2px(textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setClickEventColor(int color) {
            this.clickEventColor = color;
            return this;
        }

        public CommentClick build() {
            return new CommentClick(this);
        }
    }
}
