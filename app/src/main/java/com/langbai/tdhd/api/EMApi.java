package com.langbai.tdhd.api;


import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.SearchBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/9/28.
 */

public interface EMApi {
    //通过id查询好友信息
    @POST("friend/selFriendNo")
    Call<BaseResponseBean<ArrayList<LoginResponseBean>>> getUserByIdApi(@Query("userID") Long userID,// 被申请人ID，接收申请人
                                                                        @Query("friendAccount") String findID,
                                                                        @Query("type") int type);//平台类型

    //通过电话号码或者姓名查找好友
    @FormUrlEncoded
    @POST("userInfo/userList")
    Call<SearchBean> getUserByPhoneOrNameApi(
            @Field("u") String userID,//  被申请人ID，接收申请人
            @Query("page") int page,
            @Query("rows") int rows);//平台类型
    // 申请人ID，发出申请人

    //获取联系人好友
    @POST("friend/selUserFriend")
    Observable<BaseResponseBean<ArrayList<FriendsResponseBean>>> getFriendsApi(@Query("userID") long userID);

    //申请添加好友
    @FormUrlEncoded
    @POST("friend/saveFriendApply")
    Call<BaseResponseBean> getAddFriendsApi(@Query("userID") int i,                         // 被申请人ID，接收申请人
                                            @Query("applyPersonID") Long applyPersonID,     // 申请人ID，发出申请人
                                            @Field("applyText") String content);            // 申请信息，可为空
}                                                                                                     
