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
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.widget.smoothcheck.SmoothCheckBox;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/12.
 */

public class GroupUserHolder extends VBaseHolder<FriendsResponseBean> {
    public HashMap<Integer, Boolean> fatherTag = new HashMap<>();
    @BindView(R.id.back) TextView mBack;
    @BindView(R.id.group_user_list_head) ImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.check) SmoothCheckBox mCheck;

    RequestOptions op = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.hyphenate.easeui.R.drawable.ease_default_avatar)
            .transform(new GlideRoundTransform(mContext, 15))
            .error(com.hyphenate.easeui.R.drawable.ease_default_avatar)
            .priority(Priority.HIGH);

    public GroupUserHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final int position, final FriendsResponseBean mData) {
        super.setData(position, mData);
        Glide.with(mContext).load(mData.getFriendIcon()).apply(op).into(mHead);
        mName.setText(mData.getFriendName());


        mCheck.setOnCheckedChangeListener(null);//先设置一次CheckBox的选中监听器，传入参数null 
        mCheck.setChecked(false);
        if (fatherTag.containsKey(position) && fatherTag.get(position)) {
            mCheck.setChecked(true);
        }

        mCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                mView.setTag(mData.getFriendID());
                mListener.onItemClick(mView, position, isChecked);
            }
        });
    }

    @Override
    public void init() {
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        mCheck.setChecked(!mCheck.isChecked());
        fatherTag.put(position, mCheck.isChecked());
    }
}
