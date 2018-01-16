package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;

import java.util.ArrayList;
import java.util.List;
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
 * Created by Moushao on 2017/10/12.
 */

public interface GroupApi {
    /**
     * 创建群聊
     */
    @Multipart
    @POST("group/groupPhoneSave")
    Call<BaseResponseBean<GroupResponseBean>> getCreateGroupApi(@Part("arrMember[]") long[] arryUser,
                                                                @PartMap Map<String, RequestBody> params);

    /**
     * 获取群聊
     */
    @POST("group/userGroupList")
    Call<BaseResponseBean<ArrayList<GroupResponseBean>>> getGroupListApi(@Query("userID") Long userID);

    /**
     * 获取当前群成员
     */
    @POST("group/groupPerson")
    Call<BaseResponseBean<ArrayList<GroupMember>>> getGroupMemberListApi(@Query("userID") Long userID,
                                                                         @Query("groupChatID") long groupChatID);

    /**
     * 获取当前群详细信息
     */
    @POST("group/selGroupInfo")
    Call<BaseResponseBean<GroupResponseBean>> getGroupInfoApi(@Query("id") String groupChatID);

    /**
     * 添加成员
     */
    @POST("group/phoneSaveMember")
    Call<BaseResponseBean> getAddGroupMembersApi(@Query("groupChatID") long groupChatID,
                                                 @Query("userIDs[]") long[] userIDs);

    /**
     * 删除成员或者退出群
     */
    @POST("group/deleteUser")
    Call<BaseResponseBean> getDeleteGroupMembersApi(@Query("userID") long userID,//要删除的群成员Id
                                                    @Query("id") long groupChatID,//群id
                                                    @Query("ownID") long ownID);//自己的Id

    /**
     * 删除当前群
     */
    @POST("group/groupDelete")
    Call<BaseResponseBean> getDeleteGroupApi(@Query("id") long groupChatID,
                                             @Query("userID") long userID);

    /**
     * 更改群公告
     */
    @FormUrlEncoded
    @POST("group/groupUpdate")
    Call<BaseResponseBean> getChangeGroupBulletinApi(@Query("id") long id, @Field("descriptions") String bulletin);

}                                                           
