package com.langbai.tdhd.mvp.model;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.MeetingApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/10/16.
 */

public class MeetingModel {
    public void getGroupList(final MVPCallBack<ArrayList<GroupResponseBean>> mBack) {
        MeetingApi service = ApiManager.getInstance().getMeetingApi();
        Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call = service.getGroupListApi(UserHelper.getInstance()
                .getLogUser().getUserInfoID(), UserHelper.getInstance().getLogUser().getUserType());

        call.enqueue(new Callback<BaseResponseBean<ArrayList<GroupResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<GroupResponseBean>>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });

    }

    public void CreateMeeting(String groupName, String groupDiscreption, long[] userIDs, String groupIDs,
                              final MVPCallBack<Meeting> mBack) {
        MeetingApi service = ApiManager.getInstance().getMeetingApi();
        Call<BaseResponseBean<ArrayList<Meeting>>> call = service.getCreateMeetingApi(
                UserHelper.getInstance().getLogUser().getUserInfoID(),
                groupName,
                groupDiscreption,
                100, userIDs, groupIDs);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<Meeting>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<Meeting>>> call,
                                   Response<BaseResponseBean<ArrayList<Meeting>>> response) {
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData().get(0));
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<Meeting>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });

    }

    public void joinMeeting(String meetingLock, final MVPCallBack<MeetingBase> mBack) {
        MeetingApi service = ApiManager.getInstance().getMeetingApi();
        Call<MeetingBase> call = service.getJoinMeetingApi(UserHelper.getInstance().getLogUser()
                .getUserInfoID() + "", meetingLock);
        call.enqueue(new Callback<MeetingBase>() {
            @Override
            public void onResponse(Call<MeetingBase> call, Response<MeetingBase> response) {
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<MeetingBase> call, Throwable t) {
                RetrofitErrorUtlis.getNetErrorMessage(t);
            }
        });
    }
}
