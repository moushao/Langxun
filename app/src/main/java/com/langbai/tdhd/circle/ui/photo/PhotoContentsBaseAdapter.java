package com.langbai.tdhd.circle.ui.photo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.langbai.tdhd.circle.ui.photo.observer.PhotoAdapterObservable;
import com.langbai.tdhd.circle.ui.photo.observer.PhotoBaseDataObserver;


/**
 * Created by 大灯泡 on 2016/11/9.
 */

public abstract class PhotoContentsBaseAdapter {


    private PhotoAdapterObservable mObservable = new PhotoAdapterObservable();


    public void registerDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.registerObserver(observer);

    }

    public void unregisterDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    public void notifyDataChanged() {
        mObservable.notifyChanged();
        mObservable.notifyInvalidated();
    }


    public abstract ImageView onCreateView(ImageView convertView, ViewGroup parent, int position);

    public abstract void onBindData(int position, @NonNull ImageView convertView);

    public abstract int getCount();
}
