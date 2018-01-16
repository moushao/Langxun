package com.langbai.tdhd.api;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.bean.MeetingFriends;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Moushao on 2017/10/16.
 */

public interface MeetingApi {
    /**
     * 获取会议可选择群聊列表
     */
    @POST("meeting/selAllGroup")
    Call<BaseResponseBean<ArrayList<GroupResponseBean>>>
    getGroupListApi(@Query("userID") Long userID,
                    @Query("userType") int userType);

    /**
     * 创建会议
     */
    @FormUrlEncoded
    @POST("meeting/save")
    Call<BaseResponseBean<ArrayList<Meeting>>> getCreateMeetingApi(
            @Query("owner") Long owner,//发起人
            @Field("meetingName") String meetingName,//会议名称
            @Field("meetingDescript") String meetingDescript,//会议秒速
            @Query("meetingMaxusers") int meetingMaxusers,//最大人数
            @Query("arryUser[]") long[] arryUser,//参会人员id
            @Query("arryGroup") String arryGroup);//群聊id

    /**
     * 创建会议,获取可加入会议的用户
     */
    @POST("meeting/selMeetFriend")
    Call<BaseResponseBean<List<MeetingFriends>>> getMeetFriendsList(@Query("userID") Long userID);

    /**
     * 进入会议
     */
    @POST("meeting/enterMeeting")
    Call<MeetingBase> getJoinMeetingApi(@Query("phone") String phone,              //电话号码
                                        @Query("meetingLock") String meetingLock);//房间号

    @POST("meeting/endMeeting")
    Call<BaseResponseBean> getEndMeetingApi(@Query("meetingHXID") String meetingHXID);
}
