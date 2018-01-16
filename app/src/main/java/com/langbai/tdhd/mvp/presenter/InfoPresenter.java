package com.langbai.tdhd.mvp.presenter;

import android.content.Context;

import com.langbai.tdhd.bean.InfoRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.InfoModel;
import com.langbai.tdhd.mvp.view.InfoView;

/**
 * Created by Moushao on 2017/9/26.
 */

public class InfoPresenter extends BasePresenter<InfoView> {
    InfoModel mModle = new InfoModel();

    public void changeInfo(Context context, InfoRequestBean bean) {
        mModle.changeInfo(context,bean, new MVPCallBack<LoginResponseBean>() {
            @Override
            public void succeed(LoginResponseBean mData) {
            }

            @Override
            public void failed(String message) {
                if (mView != null) {
                    mView.onFailed(message);
                }
            }
        });
    }
}
