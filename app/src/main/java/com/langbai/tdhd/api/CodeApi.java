package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseRequestBean;
import com.langbai.tdhd.bean.BaseResponseBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/25.
 */

public interface CodeApi {
    @POST("login/sendCode")
    Call<BaseResponseBean> GetCodeApi(@Query("phone") String phone);
}
