package com.langbai.tdhd.square.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.activity.CircleActivity;
import com.langbai.tdhd.activity.SquareActivity;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.utils.ScreenUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquarePictureVH extends SquareBaseViewHolder {
    private Banner mBanner;
    private RelativeLayout mContent;
    private List<LocalMedia> medias = new ArrayList<>();

    public SquarePictureVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId, 1);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        mBanner = rootView.findViewById(R.id.banner);
        mContent = rootView.findViewById(R.id.content);
    }

    @Override
    public void onBindDataToView(@NonNull SquareBaseBean data, int position, int viewType) {
        if (mBanner == null || data == null || mContent == null)
            return;
        //        ViewGroup.LayoutParams params = mContent.getLayoutParams();
        //        params.height = params.width * 9 / 16;// 控件的宽强制设成30   
        //        mContent.setLayoutParams(params);
        initBanner(data);
    }

    /**
     * 初始化banner
     */
    private void initBanner(SquareBaseBean data) {
        for (String url : data.getThumbnail()) {
            medias.add(new LocalMedia(url, 0, 0, ""));
        }
        //mBanner.isAutoPlay(false);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        mBanner.setImages(data.getThumbnail());
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageLoadMnanger.INSTANCE.loadImage(imageView, (String) path);
            }

        });

        //设置点击事件监听
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                PictureSelector.create((Activity) mContext).externalPicturePreview(position, medias);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }
}
