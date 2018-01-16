package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.MeetingFriends;

import java.util.List;

/**
 * Created by Moushao on 2017/10/17.
 */

public interface PickUserView extends BaseView {
    void getUserListForGroupSuccess(List<FriendsResponseBean> mData);

    void getUserListForMeetingSuccess(List<MeetingFriends> mData);
}
