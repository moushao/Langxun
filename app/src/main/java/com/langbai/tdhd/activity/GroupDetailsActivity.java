package com.langbai.tdhd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.adapter.GroupDetailsHolder;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.GroupPresenter;
import com.langbai.tdhd.mvp.view.GroupView;
import com.langbai.tdhd.utils.GroupUtils;
import com.langbai.tdhd.widget.Dialog.AlertDialog;
import com.suke.widget.SwitchButton;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupDetailsActivity extends BaseActivity implements GroupView {
    public static final String TAG = "GroupDetailsActivity";
    public static final int REQUEST_CODE = 998;

    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.recyc) RecyclerView mRecyc;
    @BindView(R.id.group_name) TextView mGroupName;
    @BindView(R.id.number) TextView mGroupNumber;
    @BindView(R.id.nick_name) TextView mNickName;
    @BindView(R.id.group_name_layout) LinearLayout mGroupNameLayout;
    @BindView(R.id.group_file_layout) LinearLayout mGroupFileLayout;
    @BindView(R.id.find_message_layout) LinearLayout mFindMessageLayout;
    @BindView(R.id.shield_layout) LinearLayout mShieldLayout;
    @BindView(R.id.shield_view) View mShieldView;
    @BindView(R.id.brief) TextView mBrief;
    @BindView(R.id.add_layout) TextView mAddLayout;
    @BindView(R.id.quit_or_delete) TextView mQuitOrDelete;
    @BindView(R.id.scroll_view) ScrollView mScroll;
    @BindView(R.id.recevier_btn) SwitchButton mSwitch;
    @BindView(R.id.add_view) View maddView;
    private Context mContext;
    private VBaseAdapter userAdapter;
    private long groupChatID;
    private boolean isMyGroup;
    private int userType, platformType;
    private int deletePosition;
    private String groupEMID;
    private EMGroup EMGroup;
    private GroupResponseBean group;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_details;
    }

    @Override
    public BasePresenter getPresenter() {
        return new GroupPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initData();
        initWidget();
    }

    private void initData() {
        groupEMID = this.getIntent().getExtras().getString("GROUP_EMID", "");
        EMGroup = EMClient.getInstance().groupManager().getGroup(groupEMID);
        groupChatID = new GroupUtils(mContext).getGroupBackgroundID(groupEMID);
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("群信息");
        if (groupChatID == -1) {
            mScroll.setVisibility(View.INVISIBLE);
            return;
        } else {
            //TODO 判断用户权限是否可以添加群
            //用户权限
            userType = UserHelper.getInstance().getLogUser().getUserType();
            //平台类型
            platformType = UserHelper.getInstance().getLogUser().getType();
            if (platformType != 1 && userType == 3) {
                //⾮非平台⽤用户（三级权限）：不不可以建群、退群、踢⼈人，可以加⼊入会议
                mQuitOrDelete.setVisibility(View.GONE);
                mAddLayout.setVisibility(View.GONE);
                maddView.setVisibility(View.GONE);
            }
            initUserRecyclerView();
            mSwitch.setChecked(EMGroup.isMsgBlocked());
            mSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    changeIsBlockMsg(isChecked);
                }
            });


            ((GroupPresenter) mPresenter).getGroupMemberList(groupChatID);
            ((GroupPresenter) mPresenter).getGroupInfo(groupEMID);
        }
    }

    @OnClick({R.id.title_back, R.id.group_name_layout, R.id.group_file_layout, R.id.find_message_layout, R.id.brief, R
            .id.add_layout, R.id.quit_or_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.group_name_layout:
                break;
            case R.id.group_file_layout:
                break;
            case R.id.find_message_layout:
                break;
            case R.id.add_layout:
                Intent itt = new Intent(mContext, PickUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FROM, TAG);
                bundle.putSerializable(PickUserActivity.USER_SELECTED, (Serializable) userAdapter.getDatas());
                itt.putExtras(bundle);
                startActivityForResult(itt, PickUserActivity.REQUEST_CODE);
                break;
            case R.id.brief:
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra(EditActivity.TYPE, EditActivity.GROUP_BULLETIN);
                intent.putExtra(EditActivity.CONTENT, mBrief.getText().toString());
                startActivityForResult(intent, EditActivity.GROUP_BULLETIN_REQUEST);
                break;
            case R.id.quit_or_delete:
                showHandleDialog();

                break;
        }
    }

    //更改屏蔽群消息的设置
    private void changeIsBlockMsg(boolean isChecked) {
        if (isChecked) {
            EMClient.getInstance().groupManager().asyncBlockGroupMessage(groupEMID, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("群设置：屏蔽", "屏蔽成功");

                }

                @Override
                public void onError(int i, String s) {
                    Log.e("群设置：屏蔽", "code:" + i + "   message:" + s);
                }


                @Override
                public void onProgress(int i, String s) {

                }
            });
        } else {
            EMClient.getInstance().groupManager().asyncUnblockGroupMessage(groupEMID, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("群设置：屏蔽", "解除屏蔽成功");

                }

                @Override
                public void onError(int i, String s) {
                    Log.e("群设置：屏蔽", "code:" + i + "   message:" + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }

    private void initUserRecyclerView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        //        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 40);

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        userAdapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<GroupMember>())//
                .setLayout(R.layout.recyc_groom_friends)//
                .setLayoutHelper(getGridLayout())//
                .setHolder(GroupDetailsHolder.class)//
                .setListener(new ItemListener<GroupMember>() {
                    @Override
                    public void onItemClick(View view, int position, GroupMember bean) {
                        if ((Boolean) view.getTag()) {
                            //点击跳转
                            String account = bean.getPersonPhone();
                            if (!bean.getType().equals("2")) {
                                account = account + bean.getType();
                            }
                            FriendsInfoActivity.startAction(mContext, account, Constant.CHATTYPE_GROUP, TAG);
                        } else {
                            //长按删除,如果不是自己的群,长按没效果
                            if (!isMyGroup)
                                return;
                            deletePosition = position;
                            showDeleteDialog(bean);
                        }
                    }
                });

        delegateAdapter.addAdapter(userAdapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    //确定操作群成的dialog提醒
    private void showHandleDialog() {
        new AlertDialog(mContext).setWidthRatio(0.7f).builder()
                .hideTitleLayout()
                .setMsg(isMyGroup ? "是否解散此群？" : "是否退出此群？")
                .setPositiveButton(("确 定"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isMyGroup) {
                            //如果我是群主,则解散此群
                            ((GroupPresenter) mPresenter).deleteGroup(groupChatID);
                        } else {
                            //如果我不是群主,则调用删除群成员的接口
                            ((GroupPresenter) mPresenter).deleteGroupMembers(UserHelper.getInstance().getLogUser()
                                    .getUserInfoID(), groupChatID);
                        }
                    }
                })
                .setNegativeButton("取 消", null)
                .setMessageGravity(Gravity.CENTER).show();
    }

    //确定删除群成员的dialog提醒
    private void showDeleteDialog(final GroupMember bean) {
        new AlertDialog(mContext).setWidthRatio(0.7f).builder()
                .hideTitleLayout()
                .setMsg("确定将" + bean.getPersonRealName() + "从群聊中删除？")
                .setPositiveButton(("确 定"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((GroupPresenter) mPresenter).deleteGroupMembers(bean.getUserID(), groupChatID);
                    }
                })
                .setNegativeButton("取 消", null)
                .setMessageGravity(Gravity.CENTER).show();
    }

    private LayoutHelper getGridLayout() {
        //设置Grid布局
        GridLayoutHelper helper = new GridLayoutHelper(4);
        //是否自动扩展
        helper.setAutoExpand(false);
        helper.setMargin(10, 20, 10, 20);
        helper.setBgColor(Color.parseColor("#FFFFFF"));
        //        helper.setVGap(20);
        return helper;
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {
        showLoadProgressDialog(message);
    }

    @Override
    public void createGroupSuccess() {

    }

    @Override
    public void getGroupListSuccess(ArrayList<GroupResponseBean> mData) {

    }

    @Override
    public void getGroupMenberListSuccess(ArrayList<GroupMember> mData) {
        if (mData.size() != 0) {
            userAdapter.setData(mData);
            userAdapter.setItemCount(mData.size() > 12 ? 12 : mData.size());
            userAdapter.notifyDataSetChanged();
            mGroupNumber.setText(mData.size() + "");
        }
    }

    @Override //获取群信息成功后的回调
    public void getGroupInfoSuccess(GroupResponseBean mData) {
        if (mData == null)
            return;
        group = mData;
        mGroupName.setText(mData.getName());
        mNickName.setText(mData.getPersonnickname());
        mBrief.setText(mData.getDescriptions());
        mBrief.setVisibility(View.VISIBLE);
        //判断当前群是否为自己群
        if (mData.getOwner().equals(UserHelper.getInstance().getLogUser().getPhone())) {
            mQuitOrDelete.setText("解散该群");
            isMyGroup = true;
        } else {
            mQuitOrDelete.setText("退出该群");
            isMyGroup = false;
        }

        if (isMyGroup || mData.getType() == 1) {
            mAddLayout.setVisibility(View.VISIBLE);
            maddView.setVisibility(View.VISIBLE);
        }

        if (isMyGroup) {
            mShieldLayout.setVisibility(View.GONE);
            mShieldView.setVisibility(View.GONE);
        }

    }

    @Override //添加群成员成功后的回调
    public void addGroupMembersSuccess() {
        ((GroupPresenter) mPresenter).getGroupMemberList(groupChatID);
    }

    @Override //删除群成员成功后的回调
    public void deleteGroupMembersSuccess() {
        //        ((GroupPresenter) mPresenter).getGroupMemberList(groupChatID);
        if (isMyGroup) {
            //群主是我,刷新界面
            userAdapter.removeItem(deletePosition);
            mGroupNumber.setText(userAdapter.getItemCount() + "");
        } else {
            //群主不是我,关闭界面并回调
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override //群主解散此群，用户退出此群,关闭界面并退出
    public void deleteOrQuitGroupSuccess() {
        finish();
        setResult(RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PickUserActivity.REQUEST_CODE:
                    long[] userIDs = data.getLongArrayExtra(PickUserActivity.USER_SELECTED);
                    ((GroupPresenter) mPresenter).addGroupMembers(groupChatID, userIDs);
                    break;
                case EditActivity.GROUP_BULLETIN_REQUEST:
                    String bulletin = data.getStringExtra(EditActivity.CONTENT);
                    if (!mBrief.getText().toString().equals(bulletin)) {
                        mBrief.setText(bulletin);
                        ((GroupPresenter) mPresenter).changeGroupBulletin(group.getId(), bulletin);
                    }
                    break;
            }
        }

    }

    /**
     * @param mContext  上下文
     * @param GroupEMID 群的环信id
     * @param from      从哪儿跳来的
     */
    public static void startAction(Context mContext, String GroupEMID, String from) {
        Intent itt = new Intent(mContext, GroupDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putString("GROUP_EMID", GroupEMID);
        itt.putExtras(bundle);
        //        mContext.startActivity(itt);
        ((Activity) mContext).startActivityForResult(itt, REQUEST_CODE);
    }
}
