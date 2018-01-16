package com.langbai.tdhd.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.widget.CircleImageView;

import butterknife.BindView;

/**
 * Created by Moushao on 2017/10/13.
 */

public class GroupDetailsHolder extends VBaseHolder<GroupMember> {

    @BindView(R.id.head) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.item_layout) LinearLayout mItemLayout;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);

    public GroupDetailsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final int position, final GroupMember mData) {
        super.setData(position, mData);
        mName.setText(mData.getPersonRealName());
        Glide.with(mContext).load(mData.getPersonIcon()).apply(options).into(mHead);
        mItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setTag(false);
                mListener.onItemClick(view, position, mData);
                return true;
            }
        });
        mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(true);
                mListener.onItemClick(view, position, mData);
            }
        });
    }

}
