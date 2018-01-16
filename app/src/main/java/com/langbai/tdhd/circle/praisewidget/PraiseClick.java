package com.langbai.tdhd.circle.praisewidget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.View;

import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.ClickableSpanEx;
import com.langbai.tdhd.utils.DimenUtil;


/**
 * Created by 大灯泡 on 2016/2/21.
 * 点击事件
 */
public class PraiseClick extends ClickableSpanEx {
    private static final int DEFAULT_COLOR = 0xff517fae;
    private int color;
    private Context mContext;
    private int textSize;
    private ThumbsUp mPraiseInfo;

    private PraiseClick() {
    }

    private PraiseClick(Builder builder) {
        super(builder.color, builder.clickBgColor);
        mContext = builder.mContext;
        mPraiseInfo = builder.mPraiseInfo;
        this.textSize = builder.textSize;
    }

    @Override
    public void onClick(View widget) {
        //        if (mPraiseInfo != null)
        //            UIHelper.ToastMessage("当前用户名是： " + mPraiseInfo.getNick() + "   它的ID是： " + mPraiseInfo.getUserid
        // ());
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
        private ThumbsUp mPraiseInfo;
        private int clickBgColor;

        public Builder(Context context, @NonNull ThumbsUp info) {
            mContext = context;
            mPraiseInfo = info;
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
            this.clickBgColor = color;
            return this;
        }

        public PraiseClick build() {
            return new PraiseClick(this);
        }
    }
}
