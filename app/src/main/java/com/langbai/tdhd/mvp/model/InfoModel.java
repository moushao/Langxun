package com.langbai.tdhd.mvp.model;

import android.content.Context;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.InfoChangeApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.InfoRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/9/26.
 */

public class InfoModel {

    public void changeInfo(final Context context, InfoRequestBean bean, final MVPCallBack<LoginResponseBean> mBack) {
        InfoChangeApi service = ApiManager.getInstance().getInfoChangeApi();
        Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call = service.getInfoChangeApi(bean.userInfoID, bean
                        .userIcon, bean.age, bean.sex,
                bean.signName, bean.area);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<LoginResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<LoginResponseBean>>> response) {
                if (response.body() == null)
                    return;
                if ("100".equals(response.body().getStatus())) {
                    DiskCacheManager manager = new DiskCacheManager(context, Constants.LOGIN_USER);
                    manager.put(Constants.LOGIN_USER, response.body().getData().get(0));
                    UserHelper.getInstance().setLogUser(response.body().getData().get(0));
                    mBack.succeed(response.body().getData().get(0));
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
