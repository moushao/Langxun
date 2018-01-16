package com.langbai.tdhd.mvp.presenter;

import android.content.Context;

import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.MeetingFriends;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.PickUserModel;
import com.langbai.tdhd.mvp.view.PickUserView;

import java.util.List;

/**
 * Created by Moushao on 2017/10/17.
 */

public class PickUserPresenter extends BasePresenter <PickUserView>{
    PickUserModel mModel = new PickUserModel();

    public void getGroupUserList(Context context) {
        mView.showLoadProgressDialog("");
        mModel.getGroupUserList(context, new MVPCallBack<List<FriendsResponseBean>>() {
            @Override
            public void succeed(List<FriendsResponseBean> mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.getUserListForGroupSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }

            
        });
    }

    public void getMeetingUserList(Context context) {
        mView.showLoadProgressDialog("");
        mModel.getMeetingUserList(context, new MVPCallBack<List<MeetingFriends>>() {
            @Override
            public void succeed(List<MeetingFriends> mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.getUserListForMeetingSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }


        });
    }

//    private void requestFailed(String message) {
//        if (mView != null) {
//            mView.disDialog();
//            mView.onFailed(message);
//        }
//    }
}
