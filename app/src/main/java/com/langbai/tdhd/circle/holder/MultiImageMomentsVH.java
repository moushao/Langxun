package com.langbai.tdhd.circle.holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.activity.CircleActivity;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.circle.imageview.ForceClickImageView;
import com.langbai.tdhd.circle.ui.photo.PhotoContents;
import com.langbai.tdhd.circle.ui.photo.PhotoContentsBaseAdapter;
import com.langbai.tdhd.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 九宮格圖片的vh
 */

public class MultiImageMomentsVH extends CircleBaseViewHolder implements PhotoContents.OnItemClickListener {

    private PhotoContents imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        imageContainer = (PhotoContents) findView(imageContainer, R.id.circle_image_container);
        if (imageContainer.getmOnItemClickListener() == null) {
            imageContainer.setmOnItemClickListener(this);
        }
    }

    @Override
    public void onBindDataToView(@NonNull CircleBaseBean data, int position, int viewType) {
        if (adapter == null) {
            adapter = new InnerContainerAdapter(getContext(), data.getThumbnail());
            imageContainer.setAdapter(adapter);
        } else {
            adapter.updateData(data.getThumbnail());
        }
    }

    @Override
    public void onItemClick(ImageView imageView, int position) {
//        ToastUtils.showToast(getContext(), i + "");
        // 预览图片 可自定长按保存路径
//        PictureSelector.create((CircleActivity)mContext).externalPicturePreview(position, "/custom_file", selectList);
        //PhotoBrowseInfo info = PhotoBrowseInfo.create(adapter.datas, imageContainer.getContentViewsDrawableRects(),i);
        // ActivityLauncher.startToPhotoBrosweActivity((Activity) getContext(), info);
        List<LocalMedia> medias = new ArrayList<>();
        for (String url: adapter.datas) {
            medias.add(new LocalMedia(url, 0, 0, ""));
        }
        PictureSelector.create((Activity)mContext).externalPicturePreview(position, medias);
    }


    private static class InnerContainerAdapter extends PhotoContentsBaseAdapter {

        private RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
        private Context context;
        private List<String> datas;

        InnerContainerAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = new ArrayList<>();
            this.datas.addAll(datas);
        }

        @Override
        public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
            if (convertView == null) {
                convertView = new ForceClickImageView(context);
                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                convertView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            return convertView;
        }

        @Override
        public void onBindData(int position, @NonNull ImageView convertView) {
            //ImageLoadMnanger.INSTANCE.loadImage(convertView, datas.get(position));
            Glide.with(context).load(datas.get(position)).apply(options).into(convertView);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public void updateData(List<String> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataChanged();
        }

    }
}
