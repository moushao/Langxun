package com.langbai.tdhd.square.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.VBaseHolder;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.circle.SpannableStringBuilderCompat;
import com.langbai.tdhd.circle.commentwidget.CommentClick;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.widget.CircleImageView;

import butterknife.BindView;

/**
 * Created by Moushao on 2017/11/6.
 */

public class CommentHolder extends VBaseHolder<SquareCommentBean> {
    @BindView(R.id.head_comment) CircleImageView mHeadComment;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.content) TextView mContent;
    private RequestOptions optionsHead = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.login_head)
            .error(R.drawable.login_head)
            .priority(Priority.HIGH);
    
    public CommentHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setData(int position, SquareCommentBean mData) {
        super.setData(position, mData);
        ImageLoadMnanger.INSTANCE.loadImage(mHeadComment, mData.getFormAvatar());
        Glide.with(mContext).load(mData.getFormAvatar()).apply(optionsHead).into(mHeadComment);

        mName.setText(getUserName(mData));
        //mContent.setText(mData.getContent());
    }

    private SpannableStringBuilderCompat getUserName(SquareCommentBean mData) {
        SpannableStringBuilderCompat mSpannableStringBuilderCompat = new SpannableStringBuilderCompat();
        String content = ": " + mData.getContent() + "\0";
        boolean isEmpty = (TextUtils.isEmpty(mData.getToName()));
        // 用户B为空，证明是一条原创评论
        if (isEmpty) {
            CommentClick userA = new CommentClick.Builder(mContext, mData).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(14)
                    .build();
            mSpannableStringBuilderCompat.append(mData.getFormName(), userA, 0);
            mSpannableStringBuilderCompat.append(content);
        } else if (!isEmpty) {
            //用户A，B不空，证明是回复评论
            CommentClick userA = new CommentClick.Builder(mContext, mData).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(14)
                    .build();
            mSpannableStringBuilderCompat.append(mData.getFormName(), userA, 0);
            mSpannableStringBuilderCompat.append(" 回复 ");
            CommentClick userB = new CommentClick.Builder(mContext, mData).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(14)
                    .build();
            mSpannableStringBuilderCompat.append(mData.getToName(), userB, 0);
            mSpannableStringBuilderCompat.append(content);
        }
        return mSpannableStringBuilderCompat;
    }
}
