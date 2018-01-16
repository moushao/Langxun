package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseRequestBean;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/25.
 */

public interface RegisterApi {
    @FormUrlEncoded
    @POST("login/ptRegister")
    Call<BaseResponseBean<RegisterResponseBean>> getRegisterApi(@Field("realName") String realName,//
                                                                @Query("phone") String phone,//
                                                                @Query("password") String password,//
                                                                @Query("code") String code,//
                                                                @Query("serviceMobile") String serviceMobile);
}
