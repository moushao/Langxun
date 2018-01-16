package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/10/9.
 */

public interface NewFriendsApi {
    /**
     * 别人申请添加我为好友的申请人列表
     */
    @POST("friend/selFriendApply")
    Call<BaseResponseBean<ArrayList<ApplyResponseBean>>> getApplyList(@Query("userID") Long userID);

    /**
     * 可能认识的人
     */
    @POST("friend/selMayKnowUser")
    Call<BaseResponseBean<ArrayList<LoginResponseBean>>> getGroomList(@Query("userID") Long userID);

    /**
     * 可能认识的人
     */
    @FormUrlEncoded
    @POST("friend/saveFriendApply")
    Call<BaseResponseBean> getApplyFriend(@Query("userID") Long userID,
                                          @Query("applyPersonID") long applyPersonID,
                                          @Field("applyText") String applyText);

    /**
     * 同意添加好友信息
     */
    @POST("friend/saveFriend")
    Call<BaseResponseBean> getAgreeApplyInfo(@Query("userID") Long userID,
                                             @Query("friendID") Long friendID);

    /**
     * 创建群聊获取的好友列表
     */
    @POST("friend/selUserFriend")
    Call<BaseResponseBean> getUserList(@Query("userID") Long userID);
}
