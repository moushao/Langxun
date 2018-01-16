package com.langbai.tdhd.utils;

import android.content.Context;

import com.langbai.tdhd.Constant;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.MeetingFriends;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Moushao on 2017/10/18.
 */

public class UserUtils {
    private Context mContext;
    private DiskCacheManager mManager;

    public UserUtils(Context context) {
        mContext = context;
        mManager = new DiskCacheManager(context, "USERINFO");
    }

    public HashMap<Long, String> getUserInfo() {
        HashMap<Long, String> map = mManager.getSerializable("USERINFO");
        return map;
    }

    public void saveUserInfoByMeetingFriends(List<MeetingFriends> friendsList) {
        HashMap<Long, String> map = getUserInfo();
        if (map == null)
            map = new HashMap<>();

        for (MeetingFriends bean : friendsList) {
            map.put(bean.getUserInfoID(), bean.getType() == 2 ? bean.getPhone() : bean.getPhone() + bean
                    .getType());
        }

        mManager.put("USERINFO", map);
    }

    public void saveUserInfo(List<FriendsResponseBean> mData) {
        HashMap<Long, String> map = getUserInfo();
        if (map == null)
            map = new HashMap<>();

        for (FriendsResponseBean bean : mData) {
            map.put(bean.getFriendID(), bean.getType() == 2 ? bean.getFriendPhone() : bean.getFriendPhone() + bean
                    .getType());
        }

        mManager.put("USERINFO", map);
    }

    public void saveUserInfo(long id, String emID) {
        HashMap<Long, String> map = getUserInfo();
        if (map == null)
            map = new HashMap<>();
        map.put(id, emID);
        mManager.put("USERINFO", map);
    }
}
