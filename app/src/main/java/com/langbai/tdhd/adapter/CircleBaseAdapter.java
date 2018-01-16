package com.langbai.tdhd.adapter;

import android.content.Context;

import com.alibaba.android.vlayout.LayoutHelper;
import com.langbai.tdhd.event.ItemListener;

import java.util.List;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CircleBaseAdapter extends VBaseAdapter {
    public CircleBaseAdapter(Context context) {
        super(context);
    }

    public CircleBaseAdapter(Context context, List mDatas, int mResLayout, Class mClazz, LayoutHelper layoutHelper, 
                             ItemListener listener) {
        super(context, mDatas, mResLayout, mClazz, layoutHelper, listener);
    }
}
