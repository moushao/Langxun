package com.langbai.tdhd.adapter;

import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/11.
 */

public class ApplyHolder extends VBaseHolder<ApplyResponseBean> {


    @BindView(R.id.view) View mView;
    @BindView(R.id.sex_age) TextView mSexAge;
    @BindView(R.id.head) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.agree) TextView mAgree;
    @BindView(R.id.layout) ConstraintLayout mLayout;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);

    public ApplyHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void setData(int position, ApplyResponseBean mData) {
        super.setData(position, mData);
        ViewGroup.LayoutParams lp = mLayout.getLayoutParams();
        lp.width = ScreenUtil.getScreenWidth(mContext);
        mLayout.setLayoutParams(lp);
//        mView.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        mName.setText(TextUtils.isEmpty(mData.applyText) ? "请求添加好友" : mData.applyText);
        mSexAge.setText(""+mData.age);
        Glide.with(mContext).load(mData.userIcon).apply(options).into(mHead);
    }

    @Override
    public void init() {
    }

    @OnClick(R.id.agree)
    public void onViewClicked() {
        mListener.onItemClick(mView,position,mData);
    }
}
