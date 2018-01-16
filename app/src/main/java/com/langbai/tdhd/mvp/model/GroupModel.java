package com.langbai.tdhd.mvp.model;

import android.content.Context;

import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.GroupApi;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.GroupUtils;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moushao on 2017/10/12.
 */

public class GroupModel {
    /**
     * 创建群聊
     */
    public void createGroup(final Context mContext, long[] userArray, Map<String, RequestBody> params, final
    MVPCallBack<String> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean<GroupResponseBean>> call = service.getCreateGroupApi(userArray, params);
        call.enqueue(new Callback<BaseResponseBean<GroupResponseBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<GroupResponseBean>> call,
                                   Response<BaseResponseBean<GroupResponseBean>> response) {
                if ("100".equals(response.body().getStatus())) {
                    new GroupUtils(mContext).saveGroupItem(response.body().getData());
                    mBack.succeed("");
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<GroupResponseBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });

    }

    /**
     * 获取群列表
     */
    public void getGroupList(final Context mContext, final MVPCallBack<ArrayList<GroupResponseBean>> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call = service.getGroupListApi(UserHelper.getInstance()
                .getLogUser()
                .getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean<ArrayList<GroupResponseBean>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call,
                                   Response<BaseResponseBean<ArrayList<GroupResponseBean>>> response) {
                if ("100".equals(response.body().getStatus())) {
                    //保存群信息到本地
                    new GroupUtils(mContext).saveGroupList(response.body().getData());
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<GroupResponseBean>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    /**
     * 根据群在后台的id,获取当前群中成员
     */
    public void getGroupMemberList(long groupChatID, final MVPCallBack<ArrayList<GroupMember>> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean<ArrayList<GroupMember>>> call = service.getGroupMemberListApi(UserHelper.getInstance()
                .getLogUser
                        ().getUserInfoID(), groupChatID);
        call.enqueue(new Callback<BaseResponseBean<ArrayList<GroupMember>>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<ArrayList<GroupMember>>> call,
                                   Response<BaseResponseBean<ArrayList<GroupMember>>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<ArrayList<GroupMember>>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    /**
     * 获取群信息
     */
    public void getGroupInfo(String groupChatID, final MVPCallBack<GroupResponseBean> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean<GroupResponseBean>> call = service.getGroupInfoApi(groupChatID);
        call.enqueue(new Callback<BaseResponseBean<GroupResponseBean>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<GroupResponseBean>> call,
                                   Response<BaseResponseBean<GroupResponseBean>> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed(response.body().getData());
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<GroupResponseBean>> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });

    }

    /**
     * 给群添加新成员
     */
    public void addGroupMembers(long groupChatID, long[] userIDs, final MVPCallBack<String> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean> call = service.getAddGroupMembersApi(groupChatID, userIDs);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if ("100".equals(response.body().getStatus())) {
                    mBack.succeed("");
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }

        });
    }

    /**
     * 删除群成员
     */
    public void deleteGroupMembers(long userID, long groupChatID, final MVPCallBack<String> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean> call = service.getDeleteGroupMembersApi(
                userID,
                groupChatID,
                UserHelper.getInstance().getLogUser().getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body().isSuccess()) {
                    mBack.succeed("");
                } else {
                    mBack.failed(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }

        });
    }

    /**
     * 群主删除当前群
     */

    public void deleteGroup(long groupChatID, final MVPCallBack<String> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean> call = service.getDeleteGroupApi(groupChatID, UserHelper.getInstance().getLogUser()
                .getUserInfoID());
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body().success) {
                    mBack.succeed("");
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }

        });
    }

    public void changeGroupBulletin(long id, String bulletin, final MVPCallBack<String> mBack) {
        GroupApi service = ApiManager.getInstance().getGroupApi();
        Call<BaseResponseBean> call = service.getChangeGroupBulletinApi(id, bulletin);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() != null && response.body().getStatus().equals("100")) {
                    mBack.succeed("");
                } else {
                    mBack.failed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                mBack.failed(RetrofitErrorUtlis.getNetErrorMessage(t));
            }

        });
    }
}
