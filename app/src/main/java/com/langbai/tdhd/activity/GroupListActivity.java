package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.GroupHolder;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupListActivity extends BaseActivity implements GroupView {
    public static final String TAG = "GroupListActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.group) RecyclerView mRecyc;
    private Context mContext;
    private VBaseAdapter groupAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_list;
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
        initWidget();
        initApplyRecyclerView();
        requestGroupList();
    }


    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("我的群聊");
    }

    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }


    private void initApplyRecyclerView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        groupAdapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<GroupResponseBean>())//
                .setLayout(R.layout.recyc_user_item)//
                .setLayoutHelper(getLinearLayouut())//
                .setHolder(GroupHolder.class)//
                .setListener(new ItemListener<GroupResponseBean>() {
                    @Override
                    public void onItemClick(View view, int position, GroupResponseBean bean) {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        // it is group chat
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        intent.putExtra(Constant.EXTRA_USER_ID, bean.getGroupid());
                        startActivity(intent);
                    }
                });

        delegateAdapter.addAdapter(groupAdapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    private void requestGroupList() {
        ((GroupPresenter) mPresenter).getGroupList(mContext);
    }

    private LayoutHelper getLinearLayouut() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        helper.setMargin(0, 1, 0, 0);
        helper.setDividerHeight(1);
        return helper;
    }


    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void createGroupSuccess() {

    }

    @Override
    public void getGroupListSuccess(ArrayList<GroupResponseBean> mData) {
        if (mData.size() != 0) {
            groupAdapter.setData(mData);
            groupAdapter.notifyDataSetChanged();
            //保存群信息到本地
        }
    }

    @Override
    public void getGroupMenberListSuccess(ArrayList<GroupMember> mData) {

    }

    @Override
    public void getGroupInfoSuccess(GroupResponseBean mData) {

    }

    @Override
    public void addGroupMembersSuccess() {

    }

    @Override
    public void deleteGroupMembersSuccess() {

    }

    @Override
    public void deleteOrQuitGroupSuccess() {

    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Subscribe
    public void onEventMainThread(EventMessage event) {
        if (event.getCode().equals(EventMessage.GROUP_CHANGED)) {
            requestGroupList();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, GroupListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
