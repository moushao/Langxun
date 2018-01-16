package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/10/24.
 */

public interface UPApi {
    @POST("version/version")
    Call<BaseResponseBean<String>> getUPApi(@Query("versionNum") String versionNum,
                                    @Query("versionType") String versionType);
}
