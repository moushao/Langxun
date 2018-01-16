package com.langbai.tdhd.mvp.presenter;

import com.j256.ormlite.logger.Log;
import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.NewFriendsModel;
import com.langbai.tdhd.mvp.view.NewFriendsView;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/9.
 */

public class NewFriendsPresenter extends BasePresenter<NewFriendsView> {
    NewFriendsModel mModel = new NewFriendsModel();

    public void getApplyList() {
        mModel.getApplyList(new MVPCallBack<ArrayList<ApplyResponseBean>>() {
            @Override
            public void succeed(ArrayList<ApplyResponseBean> mData) {
                if (mView != null) {
                    mView.getApplyList(mData);
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

    public void getGroomList() {
        mModel.getGroomList(new MVPCallBack<ArrayList<LoginResponseBean>>() {
            @Override
            public void succeed(ArrayList<LoginResponseBean> mData) {
                if (mView != null) {
                    mView.getGroomList(mData);
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

    public void getAgreeApply(long  userID, int type) {
        mView.showLoadProgressDialog("");
        mModel.getAgreeApply(userID, type, new MVPCallBack<LoginResponseBean>() {
            @Override
            public void succeed(LoginResponseBean mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.getAgreeApplyInfo(mData);
                }
            }

            @Override
            public void failed(String message) {
                if (mView != null) {
                    mView.disDialog();
                    mView.onFailed(message);
                }
            }
        });
    }
}
