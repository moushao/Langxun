package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 找回密码Api
 */

public interface FindPassApi {
    @POST("login/ptRetrieval")
    Call<BaseResponseBean> getFindPassApi(//
                                          @Query("phone") String phone,//
                                          @Query("password") String password,//
                                          @Query("code") String code//
    );

}
