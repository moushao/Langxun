package com.langbai.tdhd.circle.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.utils.ToastUtils;
import com.langbai.tdhd.utils.VideoPlayer;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Moushao on 2017/10/25.
 */

public class CirccleVideoMomentsVH extends CircleBaseViewHolder {
    private ImageView videoPic;

    public CirccleVideoMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        videoPic = (ImageView) findView(videoPic, R.id.pic_vedio);
    }

    @Override
    public void onBindDataToView(@NonNull final CircleBaseBean data, int position, int viewType) {
        ImageLoadMnanger.INSTANCE.loadImage(videoPic, data.getVideoPicture());
        videoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JZVideoPlayerStandard.startFullscreen(videoPic.getContext(), JZVideoPlayerStandard.class, data
                        .getVideo(), "");
            }
        });
    }
}
