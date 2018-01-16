package com.langbai.tdhd.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.utils.GlideRoundTransform;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.widget.smoothcheck.SmoothCheckBox;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/12.
 */

public class GroupHolder extends VBaseHolder<GroupResponseBean> {
    public HashMap<Integer, Boolean> fatherTag = new HashMap<>();
    @BindView(R.id.back) TextView mBack;
    @BindView(R.id.group_user_list_head) ImageView mGroupHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.check) SmoothCheckBox mCheck;

    RequestOptions op = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.hyphenate.easeui.R.drawable.ease_group_icon)
            .transform(new GlideRoundTransform(mContext, 15))
            .error(com.hyphenate.easeui.R.drawable.ease_group_icon)
            .priority(Priority.HIGH);

    public GroupHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final int position, GroupResponseBean mData) {
        super.setData(position, mData);
        Glide.with(mContext).load(mData.getGroupicon()).apply(op).into(mGroupHead);
        mName.setText(mData.getName());
        mCheck.setVisibility(View.GONE);
    }

    @Override
    public void init() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        mListener.onItemClick(null, position, mData);
    }
}
