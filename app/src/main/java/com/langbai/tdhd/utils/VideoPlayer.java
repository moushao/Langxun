package com.langbai.tdhd.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.langbai.tdhd.widget.Dialog.ProgressDialog;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Moushao on 2017/12/11.
 */

public class VideoPlayer extends JZVideoPlayerStandard {
    private static VideoPlayer Instance;
    private Context mContext;

    public VideoPlayer(Context context) {
        super(context);
        this.mContext = context;
    }

    public static VideoPlayer getInstance(Context context) {
        if (Instance == null) {
            Instance = new VideoPlayer(context);

        }
        return Instance;
    }

    public void starPlay(String videoUrl) {
        JZVideoPlayerStandard.startFullscreen(mContext, JZVideoPlayerStandard.class, videoUrl, "");
    }

    public void releaseVideoPlayer() {
        JZVideoPlayer.releaseAllVideos();
        
        Instance = null;
        mContext = null;
    }

}
