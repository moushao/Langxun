package com.langbai.tdhd.mvp.presenter;

import android.text.Editable;

import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.MeetingModel;
import com.langbai.tdhd.mvp.view.MeetingView;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/16.
 */

public class MeetingPresenter extends BasePresenter<MeetingView> {
    MeetingModel mModel = new MeetingModel();

    public void getGroupList() {
        mModel.getGroupList(new MVPCallBack<ArrayList<GroupResponseBean>>() {

            @Override
            public void succeed(ArrayList<GroupResponseBean> mData) {
                if (mView != null) {
                    mView.getGroupListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFialed(message);
            }
        });
    }

    void requestFialed(String message) {
        if (mView != null) {
            mView.disDialog();
            mView.onFailed(message);
        }
    }

    public void CreateMeeting(String groupName, String groupDiscreption, long[] userIDs, String groupIDs) {
        mView.showLoadProgressDialog("");
        mModel.CreateMeeting(groupName, groupDiscreption, userIDs, groupIDs, new MVPCallBack<Meeting>() {
            @Override
            public void succeed(Meeting mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.createMeetingSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFialed(message);
            }
        });
    }

    public void joinMeeting(String meetingLock) {
        mModel.joinMeeting(meetingLock, new MVPCallBack<MeetingBase>() {
            @Override
            public void succeed(MeetingBase mData) {
                if (mView != null) {
                    mView.joinMeetingSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFialed(message); 
            }
        });
    }
}
