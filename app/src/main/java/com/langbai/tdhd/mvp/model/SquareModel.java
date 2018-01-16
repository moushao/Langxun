package com.langbai.tdhd.mvp.model;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.SquareApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;
import com.langbai.tdhd.utils.TimeUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquareModel {
    SquareApi service = ApiManager.getInstance().getSquareApi();

    //获取自己的广场内容
    public void getMyOwnSquareList(int page,final MVPCallBack<BaseResponseBean> mBack) {
        Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call = service.getMyOwnSquareListApi(UserHelper.getUserId(),
                TimeUtil.getCurrentTime(),page);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<SquareBaseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<SquareBaseBean>>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //获取广场内容
    public void getSquareList(int page, final MVPCallBack<BaseResponseBean> mBack) {
        Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call = service.getSquareListApi(TimeUtil.getCurrentTime(),
                page);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<SquareBaseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<SquareBaseBean>>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<SquareBaseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //取消发布
    public void getDeleteRelase(long squarePublishID, final MVPCallBack<String> mBack) {
        Call<BaseResponseBean> call = service.getDeleteRelaseApi(UserHelper.getUserId(), squarePublishID);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void getReportContent(long squarePublishID, int reportType, final MVPCallBack<String> mBack) {
        Call<BaseResponseBean> call = service.getReportContentApi(UserHelper.getUserId(), squarePublishID, reportType);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //取消点赞
    public void cancleLike(long squarePublishID, long hasLikeID, final MVPCallBack<String> mBack) {
        Call<BaseResponseBean> call = service.getCancleLikeApi(UserHelper.getUserId(), squarePublishID, hasLikeID);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //点赞
    public void LikeSquare(long squarePublishID, final MVPCallBack<SquareLikeBean> mBack) {
        Call<BaseResponseBean<SquareLikeBean>> call = service.getLikeApi(UserHelper.getUserId(), squarePublishID);
        call.enqueue(new Callback<BaseResponseBean<SquareLikeBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<SquareLikeBean>> call,
                                   Response<BaseResponseBean<SquareLikeBean>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<SquareLikeBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //添加评论
    public void addSquareComment(String commentContent, Long userID, long squarePublishID,
                                 final MVPCallBack<SquareCommentBean> mBack) {
        SquareApi service = ApiManager.getInstance().getSquareApi();
        Call<BaseResponseBean<ArrayList<SquareCommentBean>>> call = service.getAddCommentApi(commentContent, UserHelper
                .getUserId(), userID, squarePublishID);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<SquareCommentBean>>>() {

            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<SquareCommentBean>>> call,
                                   Response<BaseResponseBean<ArrayList<SquareCommentBean>>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData().get(0));
                }

            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<SquareCommentBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    //删除评论
    public void deleteComment(long reviewID, final MVPCallBack<String> mBack) {
        SquareApi service = ApiManager.getInstance().getSquareApi();
        Call<BaseResponseBean> call = service.getDeleteCommentApi(reviewID);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {

            }
        });
    }
}
