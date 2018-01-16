package com.langbai.tdhd.circle.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.langbai.tdhd.bean.CircleBaseBean;


/**
 * Created by 大灯泡 on 2016/11/3.
 * <p>
 * 空内容的vh
 */

public class EmptyMomentsVH extends CircleBaseViewHolder {

    public EmptyMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        
    }

    @Override
    public void onBindDataToView(@NonNull CircleBaseBean data, int position, int viewType) {

    }
}
