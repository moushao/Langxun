package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.mvp.view.BaseView;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/12.
 */
public interface GroupView extends BaseView {
    void createGroupSuccess();

    void getGroupListSuccess(ArrayList<GroupResponseBean> mData);

    void getGroupMenberListSuccess(ArrayList<GroupMember> mData);

    void getGroupInfoSuccess(GroupResponseBean mData);

    void addGroupMembersSuccess();

    void deleteGroupMembersSuccess();

    void deleteOrQuitGroupSuccess();
}
