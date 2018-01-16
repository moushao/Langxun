package com.langbai.tdhd.mvp.model;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.NewFriendsApi;
import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/10/9.
 */

public class NewFriendsModel {
    /**
     * 获取好友申请列表
     */
    public void getApplyList(final MVPCallBack<ArrayList<ApplyResponseBean>> mBack) {
        NewFriendsApi service = ApiManager.getInstance().getNewFriendsApi();
        Call<BaseResponseBean<ArrayList<ApplyResponseBean>>> call = service.getApplyList(UserHelper.getInstance()
                .getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<ArrayList<ApplyResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<ApplyResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<ApplyResponseBean>>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<ApplyResponseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    /**
     * 获取推荐好友列表
     */
    public void getGroomList(final MVPCallBack<ArrayList<LoginResponseBean>> mBack) {
        NewFriendsApi service = ApiManager.getInstance().getNewFriendsApi();
        Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call = service.getGroomList(UserHelper.getInstance()
                .getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<ArrayList<LoginResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<LoginResponseBean>>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }


    public void getAgreeApply(long userID, int type, final MVPCallBack<LoginResponseBean> mBack) {
        NewFriendsApi service = ApiManager.getInstance().getNewFriendsApi();
        Call<BaseResponseBean> call = service.getAgreeApplyInfo(
                UserHelper.getInstance().getLogUser().getUserInfoID(),
                userID);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call,
                                   Response<BaseResponseBean> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(null);
                } else {
                    mBack.failed(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
