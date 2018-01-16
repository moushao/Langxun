package com.langbai.tdhd.mvp.model;


import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.FriendsSettingApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.FriendsSettingBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/11/23.
 */

public class FriendsSettingModel {

    public void getFriendsSetting(long userId, long friendID, final MVPCallBack<FriendsSettingBean> mBack) {
        FriendsSettingApi service = ApiManager.getInstance().getFriendsSettingApi();
        Call<BaseResponseBean<FriendsSettingBean>> call = service.getFriendsSettingStatus(userId, friendID);
        call.enqueue(new Callback<BaseResponseBean<FriendsSettingBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<FriendsSettingBean>> call,
                                   Response<BaseResponseBean<FriendsSettingBean>> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("100")) {
                        mBack.succeed(response.body().getData());
                    } else {
                        mBack.failed(response.body().getMessage());
                    }
                } else {
                    mBack.failed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<FriendsSettingBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void handleFriendsStatus(int item, boolean isChecked, long userId, long friendID, MVPCallBack<String>
            mBack) {

        FriendsSettingApi service = ApiManager.getInstance().getFriendsSettingApi();
        Call<BaseResponseBean> call = null;
        switch (item) {
            case 0:
                call = isChecked ? service.getBanFriendsSeeApi(userId, friendID) : service
                        .getAllowFriendsSeeApi(userId, friendID);
                break;
            case 1:
                call = isChecked ? service.getBanFriendsCircleApi(userId, friendID) : service
                        .getOpenFriendsCircleApi(userId, friendID);
                break;
        }

        if (call != null)
            call.enqueue(new Callback<BaseResponseBean>() {
                @Override
                public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                    if (response.body() != null)
                        if ("100".equals(response.body().getStatus())) {

                        }
                }

                @Override
                public void onFailure(Call<BaseResponseBean> call, Throwable t) {

                }
            });
    }

    public void deleteFirends(long userId, long friendID, final MVPCallBack<String> mvpCallBack) {
        FriendsSettingApi service = ApiManager.getInstance().getFriendsSettingApi();
        Call<BaseResponseBean> call = service.getDeleteFriendsApi(userId, friendID);

        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() != null)
                    if ("100".equals(response.body().getStatus())) {
                        mvpCallBack.succeed("");
                    } else {
                        mvpCallBack.failed(response.body().getMessage());
                    }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mvpCallBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
