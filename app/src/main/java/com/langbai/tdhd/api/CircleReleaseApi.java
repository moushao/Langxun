package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/10/26.
 */

public interface CircleReleaseApi {
    @FormUrlEncoded
    @POST("userPublish/text")
    Call<BaseResponseBean<Long>> getReleaseTextApi(@Query("userID") long userId,
                                                   @Field("publishText") String publishText
    );


    //        @FormUrlEncoded
    @Multipart
    @POST("userPublish/picture")
    Call<BaseResponseBean<Long>> getReleasePictureApi(/*@Part("userID") long userId,
                                                      @Part("publishText") String publishText,*/
                                                      @PartMap Map<String, RequestBody> params);

    //    @FormUrlEncoded
    @Multipart
    @POST("userPublish/video")
    Call<BaseResponseBean<Long>> getReleaseVideoApi(/*@Query("userID") long userId,
                                                    @Query("publishText") String publishText,*/
                                                    @PartMap Map<String, RequestBody> params);
}
