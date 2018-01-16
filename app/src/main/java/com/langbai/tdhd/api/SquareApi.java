package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/11/1.
 */

public interface SquareApi {
    //获取自己的广场
    @POST("square/selOwnSquare")
    Call<BaseResponseBean<ArrayList<SquareBaseBean>>> getMyOwnSquareListApi(
            @Query("userID") long userID,
            @Query("time") long time,
            @Query("page") int page);

    //获取广场列表
    @POST("square/selSquare")
    Call<BaseResponseBean<ArrayList<SquareBaseBean>>> getSquareListApi(
            @Query("time") long time,
            @Query("page") int page);

    //举报发布  
    @POST("squareReport/report")
    Call<BaseResponseBean> getReportContentApi(
            @Query("reportID ") long reportID,
            @Query("squarePublishID") long squarePublishID,
            @Query("reportType") int reportType);

    //删除自己的发布
    @POST("square/delSquare")
    Call<BaseResponseBean> getDeleteRelaseApi(
            @Query("userID") long userId,
            @Query("squarePublishID") long squarePublishID);

    //朋友圈取消点赞
    @POST("square/squarePraise")
    Call<BaseResponseBean> getCancleLikeApi(
            @Query("likeID") long likeID,
            @Query("squarePublishID") long squarePublishID,
            @Query("squarePraiseID") long squarePraiseID);

    //朋友圈取消点赞
    @POST("square/squarePraise")
    Call<BaseResponseBean<SquareLikeBean>> getLikeApi(
            @Query("likeID") long likeID,
            @Query("squarePublishID") long squarePublishID);

    //广场评论
    @FormUrlEncoded
    @POST("square/squareReview")
    Call<BaseResponseBean<ArrayList<SquareCommentBean>>> getAddCommentApi(
            @Field("content") String content,
            @Query("formID") Long formID,
            @Query("toID") Long toID,
            @Query("squarePublishID") long
                    squarePublishID);

    @POST("square/delete")
    Call<BaseResponseBean> getDeleteCommentApi(@Query("squareReviewID") long squareReviewID);
}
