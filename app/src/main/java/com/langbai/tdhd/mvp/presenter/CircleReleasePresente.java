package com.langbai.tdhd.mvp.presenter;

import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.CircleReleaseModel;
import com.langbai.tdhd.mvp.view.ReleaseView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Moushao on 2017/10/26.
 */

public class CircleReleasePresente extends BasePresenter<ReleaseView> {
    CircleReleaseModel mModel = new CircleReleaseModel();

    public void releaseText(String content) {
        mView.showLoadProgressDialog("发布中...");
        mModel.releaseText(content, null, new MVPCallBack<Long>() {
            @Override
            public void succeed(Long mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.releaseSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void releasePicture(String content, Map<String, RequestBody> params) {
        mView.showLoadProgressDialog("发布中...");
        mModel.releasePicture(content, params, new MVPCallBack<Long>() {
            @Override
            public void succeed(Long mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.releaseSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void releaseVideo(String content, Map<String, RequestBody> params) {
        mView.showLoadProgressDialog("发布中...");
        mModel.releaseVideo(content, params, new MVPCallBack<Long>() {
            @Override
            public void succeed(Long mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.releaseSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }
}
