package com.langbai.tdhd.square.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.VBaseHolder;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.widget.CircleImageView;

import butterknife.BindView;

/**
 * Created by Moushao on 2017/11/6.
 */

public class SquareLikeHolder extends VBaseHolder<SquareLikeBean> {
    @BindView(R.id.head_like) CircleImageView mLikeHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.time) TextView mTime;
    private RequestOptions optionsHead = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.login_head)
            .error(R.drawable.login_head)
            .priority(Priority.HIGH);
    
    public SquareLikeHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(int position, SquareLikeBean mData) {
        super.setData(position, mData);
        Glide.with(mContext).load(mData.getLikeAvatar()).apply(optionsHead).into(mLikeHead);
        mName.setText(mData.getLikeName());
    }
}
