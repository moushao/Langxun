package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/25.
 */

public interface LoginApi {
    @FormUrlEncoded
    @POST("login/login")
    Call<BaseResponseBean<LoginResponseBean>> getLoginApi(@Field("account") String account,//
                                                          @Field("password") String password,//
                                                          @Query("type") int type);
}
