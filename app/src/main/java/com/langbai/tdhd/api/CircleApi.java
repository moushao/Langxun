package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.PraiseBean;

import java.util.ArrayList;
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
 * Created by Moushao on 2017/10/20.
 */

public interface CircleApi {
    //获取自己的朋友圈,个人相册
    @POST("space/selectOwn")
    Call<BaseResponseBean<ArrayList<CircleBaseBean>>> getMyOwnCircleListApi(
            @Query("userID") long userID,
            @Query("ownID") long userId1,
            @Query("page") int page);

    //朋友圈
    @POST("space/selectSpace")
    Call<BaseResponseBean<ArrayList<CircleBaseBean>>> getCircleListApi(
            @Query("userID") long userID,
            @Query("page") int page);
    //点赞
    @POST("space/friendPraise")
    Call<BaseResponseBean<PraiseBean>> getCircleLikeApi(
            @Query("userID") long userInfoID,           //本人id
            @Query("userPublishID") long userPublishID);     //这条朋友圈内容的id

    @POST("space/friendPraise")
    Call<BaseResponseBean> getCircleCancleLikeApi(
            @Query("userID") long userInfoID,           //本人id
            @Query("userPublishID") long userPublishID,  //这条朋友圈内容的id
            @Query("friendPraiseID") long friendPraiseID);// 我点赞这条朋友圈的点赞id

    @FormUrlEncoded
    @POST("friendReview/review")
    Call<BaseResponseBean<Long>> getAddCommentApi(
            @Query("userPublishID") long statusID,  //这条朋友圈内容的id
            @Field("reviewText") String reviewText, //回复文本
            @Query("userID") long userID);          //我的id

    @FormUrlEncoded
    @POST("friendReview/review")
    Call<BaseResponseBean<Long>> getReviewCommentApi(
            //这条朋友圈内容的i
            @Query("userPublishID") long statusID,
            //回复文本 
            @Field("reviewText") String reviewText,
            //我的id 
            @Query("userID") long userID,
            //被回复人的id
            @Query("replyPersonID") long replyPersonID);

    @POST("friendReview/delete")
    Call<BaseResponseBean> getDeleteCommentApi(
            @Query("friendReviewID") long friendReviewID,
            @Query("userID") long userID);

    @POST("userPublish/textdelete")
    Call<BaseResponseBean> getDeleteReleaseApi(
            @Query("userID") long userId,
            @Query("userPublishID") long userPublishID);

    @Multipart
    @POST("userInfo/userInfoUpdate")
    Call<BaseResponseBean<ArrayList<LoginResponseBean>>> getChangeWallPictureApi(
            @PartMap Map<String, RequestBody> params);
}
