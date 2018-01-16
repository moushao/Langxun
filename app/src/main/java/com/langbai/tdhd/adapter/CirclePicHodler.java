package com.langbai.tdhd.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/23.
 */

class CirclePicHodler extends VBaseHolder<String> {
    @BindView(R.id.item_pic) ImageView mItemPic;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);

    public CirclePicHodler(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(int position, String mData) {
        super.setData(position, mData);
        Glide.with(mContext).load(mData).apply(options).into(mItemPic);

    }

    @OnClick(R.id.item_pic)
    public void onViewClicked() {

    }
}
