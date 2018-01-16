package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/26.
 */

public interface InfoChangeApi {
    @FormUrlEncoded
    @POST("userInfo/userInfoUpdate")
    Call<BaseResponseBean<ArrayList<LoginResponseBean>>> getInfoChangeApi(@Query("userInfoID") Long userID, //
                                                                          @Query("userIcon") String userIcon,  //
                                                                          @Query("age") int age,    //
                                                                          @Field("sex") String sex,    //
                                                                          @Field("signName") String signName, //
                                                                          @Field("area") String area

    );
}
