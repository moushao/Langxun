package com.langbai.tdhd.mvp.model;

import android.content.Context;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.CircleApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.PraiseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CircleModel {
    public void getMyOwnCircleList(int page, long ownID, final MVPCallBack<BaseResponseBean> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call = service.getMyOwnCircleListApi(UserHelper.getUserId()
                , ownID,
                page);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<CircleBaseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<CircleBaseBean>>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getPage() != 0) {
                    mBack.succeed(response.body());
                } else {
                    mBack.failed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call, Throwable t) {
            }
        });
    }

    //获取朋友圈列表
    public void getCircleList(int page, final MVPCallBack<BaseResponseBean> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call = service.getCircleListApi(UserHelper.getUserId(),
                page);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<CircleBaseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<CircleBaseBean>>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getPage() != 0) {
                    mBack.succeed(response.body());
                } else {
                    mBack.failed("");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<CircleBaseBean>>> call, Throwable t) {
            }
        });
    }

    //点赞朋友圈
    public static void addLike(long userPublishID, final MVPCallBack mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<PraiseBean>> call = service.getCircleLikeApi(
                UserHelper.getInstance().getLogUser().getUserInfoID(), userPublishID);
        call.enqueue(new Callback<BaseResponseBean<PraiseBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<PraiseBean>> call, Response<BaseResponseBean<PraiseBean>>
                    response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData().getFriendPraiseID());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<PraiseBean>> call, Throwable t) {
            }
        });
    }

    //取消喜欢朋友圈
    public static void cancleLike(Long userPublishID, Long friendPraiseID, final MVPCallBack mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean> call = service.getCircleCancleLikeApi(
                UserHelper.getInstance().getLogUser().getUserInfoID(), userPublishID, friendPraiseID);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean>
                    response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
            }
        });
    }

    //对某一条朋友圈添加回复
    public void addComment(long statusID, String commentContent, final MVPCallBack mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<Long>> call = service.getAddCommentApi(
                statusID,
                commentContent,
                UserHelper.getInstance().getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<Long>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<Long>> call, Response<BaseResponseBean<Long>>
                    response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<Long>> call, Throwable t) {
            }
        });
    }

    //对某个人的评论进行回复
    public void addCommentToPerson(long statusID, String commentContent, CommentBean commentInfo, final
    MVPCallBack<Long> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<Long>> call = service.getReviewCommentApi(
                statusID,
                commentContent,
                UserHelper.getInstance().getLogUser().getUserInfoID(),
                commentInfo.getFormID());

        call.enqueue(new Callback<BaseResponseBean<Long>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<Long>> call, Response<BaseResponseBean<Long>>
                    response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    mBack.succeed(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<Long>> call, Throwable t) {
            }
        });
    }

    //删除我自己评论
    public void deleteComment(CommentBean commentInfo, final MVPCallBack<String> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean> call = service.getDeleteCommentApi(commentInfo.getCommentID(), UserHelper.getUserId());
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean>
                    response) {
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

    //删除自己发布的朋友圈
    public void deleteRelease(long statusID, final MVPCallBack<String> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean> call = service.getDeleteReleaseApi(UserHelper.getUserId(), statusID);
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

    public void changeWallPicture(final Context context, Map<String, RequestBody> params, final MVPCallBack
            <LoginResponseBean> mBack) {
        CircleApi service = ApiManager.getInstance().getCircleApi();
        Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call = service.getChangeWallPictureApi(params);

        call.enqueue(new Callback<BaseResponseBean<ArrayList<LoginResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<LoginResponseBean>>> response) {
                if (response.body() == null)
                    return;
                if ("100".equals(response.body().getStatus())) {
                    DiskCacheManager manager = new DiskCacheManager(context, Constants.LOGIN_USER);
                    manager.put(Constants.LOGIN_USER, response.body().getData().get(0));
                    UserHelper.getInstance().setLogUser(response.body().getData().get(0));
                    mBack.succeed(response.body().getData().get(0));
                } else {

                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call, Throwable t) {

            }
        });
    }


}
