package com.langbai.tdhd.mvp.presenter;

import com.langbai.tdhd.bean.FriendsSettingBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.FriendsSettingModel;
import com.langbai.tdhd.mvp.view.FriendsSettingView;

/**
 * Created by Moushao on 2017/11/23.
 */

public class FriendsSettingPresenter extends BasePresenter<FriendsSettingView> {
    FriendsSettingModel mModel = new FriendsSettingModel();

    public void getFriendsSetting(long userId, long friendID) {
        mModel.getFriendsSetting(userId, friendID, new MVPCallBack<FriendsSettingBean>() {
            @Override
            public void succeed(FriendsSettingBean mData) {
                if (mView != null) {
                    mView.getFriendsStatusSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void handleFriendsStatus(int item, boolean isChecked, long userId, long friendID) {
        mModel.handleFriendsStatus(item, isChecked, userId, friendID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {

            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void deleteFirends(long userId, long friendID) {
        mView.showLoadProgressDialog("删除中...");
        mModel.deleteFirends(userId, friendID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.deleteSuccess();
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }
}
