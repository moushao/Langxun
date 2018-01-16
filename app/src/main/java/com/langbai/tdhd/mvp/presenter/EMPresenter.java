package com.langbai.tdhd.mvp.presenter;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.EMModel;
import com.langbai.tdhd.mvp.model.GroupModel;
import com.langbai.tdhd.mvp.view.EMView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moushao on 2017/9/28.
 */

public class EMPresenter extends BasePresenter<EMView> {
    EMModel mEMModle = new EMModel();

    public void getContractList(Context context) {
        mEMModle.getContractList(context, new MVPCallBack<List<EaseUser>>() {
            @Override
            public void succeed(List<EaseUser> mData) {
                if (mView != null) {
                    mView.getContractListSuccess(mData);
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

    public void searchFriends(String s) {
        mView.showLoadProgressDialog("");
        mEMModle.searchFriends(s, new MVPCallBack<ArrayList<LoginResponseBean>>() {
            @Override
            public void succeed(ArrayList<LoginResponseBean> mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.FindFriendsSuccess(mData);
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

    public void getGroupList(Context context) {
        new GroupModel().getGroupList(context, new MVPCallBack<ArrayList<GroupResponseBean>>() {
            @Override
            public void succeed(ArrayList<GroupResponseBean> mData) {
                if (mView != null) {
                    //mView.getGroupListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void deleteFirends(long userId, long friendID) {
        mEMModle.deleteFirends(userId, friendID, new MVPCallBack<String>() {
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
