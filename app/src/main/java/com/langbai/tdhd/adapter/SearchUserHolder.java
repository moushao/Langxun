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
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.widget.smoothcheck.SmoothCheckBox;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/10/12.
 */

public class SearchUserHolder extends VBaseHolder<LoginResponseBean> {
    @BindView(R.id.name) TextView mName;


    public SearchUserHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final int position, LoginResponseBean mData) {
        super.setData(position, mData);
        switch (mData.getType()) {
            case 1:
                mName.setText(mData.getRealName() + "(KF)");
                break;
            case 2:
                mName.setText(mData.getRealName() + "(TD)");
                break;
            case 3:
                mName.setText(mData.getRealName() + "(XN)");

                break;
        }
    }

    @Override
    public void init() {
        super.init();
    }
}
