package com.langbai.tdhd.mvp.model;

import android.app.Application;
import android.content.Context;

import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.CodeApi;
import com.langbai.tdhd.api.FindPassApi;
import com.langbai.tdhd.api.LoginApi;
import com.langbai.tdhd.api.RegisterApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LogBaseRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.MD5Util;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/25.
 */

public class LoginBaseModel {

    /**
     * 获取验证码
     */
    public void getCode(String phone, final MVPCallBack<String> mBack) {
        CodeApi service = ApiManager.getInstance().getCodeApi();
        Call<BaseResponseBean> call = service.GetCodeApi(phone);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (!"100".equals(response.body().getStatus())) {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    /**
     * 注册
     */
    public void register(LogBaseRequestBean bean, final MVPCallBack<RegisterResponseBean> mBack) {
        RegisterApi service = ApiManager.getInstance().getRegisterApi();
        Call<BaseResponseBean<RegisterResponseBean>> call = service.getRegisterApi(bean.realName, bean.phone, bean
                .password, bean.code, "");
        call.enqueue(new Callback<BaseResponseBean<RegisterResponseBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<RegisterResponseBean>> call,
                                   Response<BaseResponseBean<RegisterResponseBean>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<RegisterResponseBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    /**
     * 找回密码
     */
    public void findPass(LogBaseRequestBean bean, final MVPCallBack<String> mBack) {
        FindPassApi service = ApiManager.getInstance().getFindPassApi();
        Call<BaseResponseBean> call = service.getFindPassApi(bean.phone, bean.password, bean.code);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed("");
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

    /**
     * 登录
     */
    public void login(final Context mContext, final LogBaseRequestBean bean, final MVPCallBack<LoginResponseBean>
            mBack) {
        LoginApi service = ApiManager.getInstance().LoginApi();
        Call<BaseResponseBean<LoginResponseBean>> call = service.getLoginApi(
                bean.account,
                MD5Util.getMD5Result(bean.getPassword()),
                bean.type);
        call.enqueue(new Callback<BaseResponseBean<LoginResponseBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<LoginResponseBean>> call,
                                   Response<BaseResponseBean<LoginResponseBean>> response) {
                if (response.body() == null) {
                    mBack.failed("");
                    return;
                }
                if ("100".equals(response.body().getStatus())) {
                    response.body().getData().setPassword(bean.password);
                    DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
                    manager.put(Constants.LOGIN_USER, response.body().getData());
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<LoginResponseBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }
}
