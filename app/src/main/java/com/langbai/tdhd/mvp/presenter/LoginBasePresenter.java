package com.langbai.tdhd.mvp.presenter;

import android.content.Context;

import com.langbai.tdhd.bean.LogBaseRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.LoginBaseModel;
import com.langbai.tdhd.mvp.view.AccountView;

/**
 * Created by Moushao on 2017/9/25.
 */

public class LoginBasePresenter extends BasePresenter<AccountView> {
    LoginBaseModel mModle = new LoginBaseModel();

    public void getCode(String phone) {
        mModle.getCode(phone, new MVPCallBack<String>() {

            @Override
            public void succeed(String mData) {
            }

            @Override
            public void failed(String message) {
                if (mView != null) {
                    mView.onFailed(message);
                }
            }
        });
    }

    public void register(LogBaseRequestBean bean) {
        mModle.register(bean, new MVPCallBack<RegisterResponseBean>() {
            @Override
            public void succeed(RegisterResponseBean mData) {
                if (mView != null) {
                    mView.registerSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                if (mView != null) {
                    mView.onFailed(message);
                }
            }
        });
    }

    public void findPass(LogBaseRequestBean bean) {
        mModle.findPass(bean, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.findPassSuccess();
                }
            }

            @Override
            public void failed(String message) {
                if (mView != null) {
                    mView.onFailed(message);
                }
            }
        });
    }

    public void login(Context mContext, LogBaseRequestBean bean) {
        mModle.login(mContext,bean, new MVPCallBack<LoginResponseBean>() {
            @Override
            public void succeed(LoginResponseBean mData) {
                if (mView != null) {
                    mView.LoginSuccess(mData);
                }
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
