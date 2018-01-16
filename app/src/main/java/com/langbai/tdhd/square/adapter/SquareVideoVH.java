package com.langbai.tdhd.square.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquareVideoVH extends SquareBaseViewHolder {
    private JZVideoPlayerStandard mPlayer;

    public SquareVideoVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId, 2);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        mPlayer = rootView.findViewById(R.id.player);
    }

    @Override
    public void onBindDataToView(@NonNull final SquareBaseBean data, int position, int viewType) {
        mPlayer.setUp(data.getVideo(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        ImageLoadMnanger.INSTANCE.loadImage(mPlayer.thumbImageView, data.getVideoPicture());
    }
}
