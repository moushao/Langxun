package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/16.
 */

public interface MeetingView extends BaseView {
    void getGroupListSuccess(ArrayList<GroupResponseBean> mData);

    void createMeetingSuccess(Meeting mData);

    void joinMeetingSuccess(MeetingBase mData);
}
