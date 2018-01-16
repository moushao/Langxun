package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.GroupUserHolder;
import com.langbai.tdhd.adapter.MeetingUserHolder;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.GroupMember;
import com.langbai.tdhd.bean.MeetingFriends;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.PickUserPresenter;
import com.langbai.tdhd.mvp.view.PickUserView;
import com.langbai.tdhd.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PickUserActivity extends BaseActivity implements PickUserView {
    public static final int REQUEST_CODE = 998;
    @BindView(R.id.title_back) ImageView mTitleBack;
    public static final String TAG = "PickUserActivity";
    public static final String USER_SELECTED = "USER_SELECTED";
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.recyc) RecyclerView mRecyc;
    private VBaseAdapter groupUserAdapter, meetingUserAdapter;
    private Context mContext;
    private ArrayList<FriendsResponseBean> list = new ArrayList<>();
    private HashMap<Integer, Long> userMap = new HashMap<>();
    private String from;
    //    private HashMap<Long, Long> selectMap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick_user;
    }

    @Override
    public BasePresenter getPresenter() {
        return new PickUserPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        from = getIntent().getExtras().getString(Constants.FROM);
        onTintStatusBar();
        initWidget();
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("我的朋友");
        mMore.setText("完成");
        /**
         * 选择好友的界面,创建会议和创建群聊,获取的接口不一样,所以要初始化不同的recyc以及调用不同的接口
         */
        if (from.equals(GroupCreateActivity.TAG) || from.equals(GroupDetailsActivity.TAG)) {
            //从创建群的界面跳过来
            initGroupUserRecyclerView();
            ((PickUserPresenter) mPresenter).getGroupUserList(mContext);
        } else if (from.equals(MeetingCreateActivity.TAG)) {
            //从发起会议的界面跳过来
            initMeetingUserRecyclerView();
            ((PickUserPresenter) mPresenter).getMeetingUserList(mContext);
        }

        //        if (from.equals(GroupDetailsActivity.TAG)) {
        //            ArrayList<GroupMember> list = (ArrayList<GroupMember>) getIntent().getExtras().getSerializable
        //                    (PickUserActivity.USER_SELECTED);
        //            if (list != null && list.size() != 0) {
        //                selectMap = new HashMap<>();
        //                for (GroupMember bean : list) {
        //                    selectMap.put(bean.getUserID(), bean.getUserID());
        //                }
        //            }
        //        }
    }

    @OnClick({R.id.title_back, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.more:
                back();
                break;
        }
    }

    private void back() {
        if (userMap.size() != 0) {
            long[] users = new long[userMap.size()];
            long[] userEMID = new long[userMap.size()];
            int i = 0;
            for (int key : userMap.keySet()) {
                users[i] = userMap.get(key);
                i++;
            }
            Intent itt = new Intent();
            itt.putExtra(USER_SELECTED, users);
            setResult(RESULT_OK, itt);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void initMeetingUserRecyclerView() {
        DelegateAdapter delegateAdapter = new DelegateAdapter(getVirtualLayoutManager(), false);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        meetingUserAdapter = new VBaseAdapter(mContext)//
                .setData(list)//
                .setLayout(R.layout.recyc_user_item)//
                .setLayoutHelper(getLinearLayouut())//
                .setHolder(MeetingUserHolder.class)//
                .setListener(new ItemListener<Boolean>() {
                    @Override
                    public void onItemClick(View view, int position, Boolean isCheck) {
                        SaveData(position, (long) view.getTag(), isCheck);
                    }
                });

        delegateAdapter.addAdapter(meetingUserAdapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    private void initGroupUserRecyclerView() {
        DelegateAdapter delegateAdapter = new DelegateAdapter(getVirtualLayoutManager(), false);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        groupUserAdapter = new VBaseAdapter(mContext)//
                .setData(list)//
                .setLayout(R.layout.recyc_user_item)//
                .setLayoutHelper(getLinearLayouut())//
                .setHolder(GroupUserHolder.class)//
                .setListener(new ItemListener<Boolean>() {
                    @Override
                    public void onItemClick(View view, int position, Boolean isCheck) {
                        SaveData(position, (long) view.getTag(), isCheck);

                    }
                });

        delegateAdapter.addAdapter(groupUserAdapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    private void SaveData(int position, long userID, Boolean isCheck) {
        if (isCheck) {
            userMap.put(position, userID);
        } else {
            if (userMap.containsKey(position))
                userMap.remove(position);
        }
        if (userMap.size() != 0) {
            mTitleTv.setText("我的朋友(" + userMap.size() + ")");
        } else {
            mTitleTv.setText("我的朋友");
        }
    }

    private LayoutHelper getLinearLayouut() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        helper.setMargin(0, 1, 0, 0);
        helper.setDividerHeight(1);
        return helper;
    }

    @NonNull
    private VirtualLayoutManager getVirtualLayoutManager() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);
        return virtualLayoutManager;
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading("");
    }

    @Override
    public void disDialog() {
        disLoadDialog();
    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    public void getUserListForGroupSuccess(List<FriendsResponseBean> mData) {
        if (mData.size() != 0) {

            if (from.equals(GroupDetailsActivity.TAG)) {
                //如果是从群聊详情界面过来的，好友列表删除已经是群成员的人
                ArrayList<GroupMember> list = (ArrayList<GroupMember>) getIntent().getExtras().getSerializable
                        (PickUserActivity.USER_SELECTED);
                if (list != null || list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Iterator<FriendsResponseBean> it = mData.iterator();
                        while (it.hasNext()) {
                            FriendsResponseBean bean = it.next();
                            if (list.get(i).getUserID() == bean.getFriendID()) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
            groupUserAdapter.setData(mData);
            groupUserAdapter.notifyDataSetChanged();
            new UserUtils(mContext).saveUserInfo(mData);
        }
    }

    @Override
    public void getUserListForMeetingSuccess(List<MeetingFriends> mData) {
        if (mData.size() != 0) {
            meetingUserAdapter.setData(mData);
            meetingUserAdapter.notifyDataSetChanged();
            new UserUtils(mContext).saveUserInfoByMeetingFriends(mData);
        }
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, PickUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
