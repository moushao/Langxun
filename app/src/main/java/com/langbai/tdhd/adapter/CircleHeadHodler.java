package com.langbai.tdhd.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.langbai.tdhd.R;
import com.langbai.tdhd.utils.MarginUtils;
import com.langbai.tdhd.utils.ScreenUtil;

import butterknife.BindView;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CircleHeadHodler extends VBaseHolder {

    @BindView(R.id.head_background) ImageView mBigHead;
    @BindView(R.id.samll_head) ImageView mSamllHead;
    @BindView(R.id.last_message_head) ImageView mLastMessageHead;
    @BindView(R.id.new_message) TextView mNewMessage;
    @BindView(R.id.new_message_layout) LinearLayout mNewMessageLayout;

    public CircleHeadHodler(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(int position, Object mData) {
        super.setData(position, mData);
        ViewGroup.LayoutParams lp = mBigHead.getLayoutParams();
        lp.width = ScreenUtil.getScreenWidth(mContext) ;
        mBigHead.setLayoutParams(lp);
//        ViewGroup.MarginLayoutParams hd =(ViewGroup.MarginLayoutParams) mSamllHead.getLayoutParams();
//        hd.setMargins(0,(int)(ScreenUtil.getScreenWidth(mContext)*0.8)-30,30,0);
//        mSamllHead.setLayoutParams(hd);
//        mSamllHead.setPadding(0,(int)(ScreenUtil.getScreenWidth(mContext)*0.8)-30,30,0);
//        Toast.makeText(mContext, "mBigHead.getHeight()-30:" + (ScreenUtil.getScreenWidth(mContext)*0.8 - 30), Toast
//                .LENGTH_SHORT).show();
    }
}
