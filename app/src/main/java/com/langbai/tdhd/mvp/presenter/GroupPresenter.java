package com.langbai.tdhd.mvp.presenter;

import android.content.Context;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.GroupModel;
import com.langbai.tdhd.mvp.view.GroupView;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Moushao on 2017/10/12.
 */

public class GroupPresenter extends BasePresenter<GroupView> {
    GroupModel mModel = new GroupModel();

    public void createGroup(Context mContext,long[] userArray, Map<String, RequestBody> params) {
        mView.showLoadProgressDialog("群聊创建中...");
        mModel.createGroup(mContext,userArray,params, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.disDialog();
                    mView.createGroupSuccess();
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void getGroupList(Context mContext) {
        mModel.getGroupList(mContext,new MVPCallBack<ArrayList<GroupResponseBean>>() {
            @Override
            public void succeed(ArrayList<GroupResponseBean> mData) {
                if (mView != null) {
                    mView.getGroupListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void getGroupMemberList(long groupChatID) {
        mModel.getGroupMemberList(groupChatID, new MVPCallBack<ArrayList<GroupMember>>() {

            @Override
            public void succeed(ArrayList<GroupMember> mData) {
                if (mView != null) {
                    mView.getGroupMenberListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }


    public void getGroupInfo(String groupChatID) {
        mModel.getGroupInfo(groupChatID, new MVPCallBack<GroupResponseBean>() {
            @Override
            public void succeed(GroupResponseBean mData) {
                if (mView != null) {
                    mView.getGroupInfoSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void addGroupMembers(long groupChatID, long[] userIDs) {
        mModel.addGroupMembers(groupChatID, userIDs, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.addGroupMembersSuccess();
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void deleteGroupMembers(long userID, long groupChatID) {
        mModel.deleteGroupMembers(userID, groupChatID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.deleteGroupMembersSuccess();
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }


//    private void requestFailed(String message) {
//        if (mView != null) {
//            mView.onFailed(message);
//        }
//    }

    public void deleteGroup(long groupChatID) {
        mModel.deleteGroup(groupChatID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.deleteOrQuitGroupSuccess();
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void changeGroupBulletin(long id, String bulletin) {
        mModel.changeGroupBulletin(id,bulletin,new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                
            }

            @Override
            public void failed(String message) {

            }
        });
    }
}
