package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.FriendsSettingBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/11/23.
 */

public interface FriendsSettingApi {
    /**
     * 查询好友相关年设置
     */
    @POST("space/banStatu")
    Call<BaseResponseBean<FriendsSettingBean>> getFriendsSettingStatus(@Query("userID") long userID,
                                                                       @Query("friendID") long friendID);

    /**
     * 禁止朋友看我朋友圈
     */
    @POST("space/ownBan")
    Call<BaseResponseBean> getBanFriendsSeeApi(@Query("userID") long userID,
                                               @Query("friendID") long friendID);

    /**
     * 允许朋友看我朋友圈
     */
    @POST("space/delOwnBan")
    Call<BaseResponseBean> getAllowFriendsSeeApi(@Query("userID") long userID,
                                                 @Query("friendID") long friendID);

    /**
     * 禁止他的朋友圈
     */
    @POST("space/friendBan")
    Call<BaseResponseBean> getBanFriendsCircleApi(@Query("userID") long userID,
                                                  @Query("friendID") long friendID);

    /**
     * 允许看他的朋友圈
     */
    @POST("space/delFriendBan")
    Call<BaseResponseBean> getOpenFriendsCircleApi(@Query("userID") long userID,
                                                   @Query("friendID") long friendID);

    /**
     * 删除好友
     */
    @POST("friend/deleteFriend")
    Call<BaseResponseBean> getDeleteFriendsApi(@Query("userID") long userID,
                                               @Query("friendID") long friendID);
}
