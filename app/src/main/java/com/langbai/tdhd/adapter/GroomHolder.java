package com.langbai.tdhd.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/9.
 */

public class GroomHolder extends VBaseHolder<LoginResponseBean> {
    @BindView(R.id.head) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.item_layout) LinearLayout mItemLayout;

    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);

    public GroomHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(int position, LoginResponseBean mData) {
        super.setData(position, mData);
        ViewGroup.LayoutParams lp = mItemLayout.getLayoutParams();
        lp.width = ScreenUtil.getScreenWidth(mContext) / 4;
        mItemLayout.setLayoutParams(lp);

        mName.setText(mData.getRealName());
        Glide.with(mContext).load(mData.getUserIcon()).apply(options).into(mHead);
    }

    @OnClick(R.id.item_layout)
    public void onViewClicked() {
        if (mListener != null)
            mListener.onItemClick(mView, position, mData);
    }

    @Override
    public void init() {
    }
}
