package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/11/3.
 */

public interface SquareReleaseApi {
    @POST("square/text")
    @FormUrlEncoded
    Call<BaseResponseBean<Long>> getReleaseTextApi(@Query("userID") long userID,
                                                  @Field("content") String content);

    @Multipart
    @POST("square/picture")
    Call<BaseResponseBean<Long>> getReleasePictureApi(/*@Part("userID") long userId,
                                                      @Part("publishText") String publishText,*/
                                                      @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("square/video")
    Call<BaseResponseBean<Long>> getReleaseVideoApi(/*@Query("userID") long userId,
                                                    @Query("publishText") String publishText,*/
                                                    @PartMap Map<String, RequestBody> params);
}
