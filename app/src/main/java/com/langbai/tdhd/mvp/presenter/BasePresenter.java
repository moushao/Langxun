package com.langbai.tdhd.mvp.presenter;


import com.langbai.tdhd.bean.LogBaseRequestBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.mvp.view.BaseView;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public abstract class BasePresenter<T extends BaseView> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }

    public void requestFailed(String message) {
        if (mView != null) {
            mView.disDialog();
            mView.onFailed(message);
        }
    }

}