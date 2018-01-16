package com.langbai.tdhd.activity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.gson.Gson;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.PickGroupHolder;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.MeetingPresenter;
import com.langbai.tdhd.mvp.view.MeetingView;
import com.langbai.tdhd.utils.GroupUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PickGroupActivity extends BaseActivity implements MeetingView {
    public static final String TAG = "PickGroupActivity";
    public static final int REQUEST_CODE = 997;
    @BindView(R.id.title_back) ImageView mTitleBack;
    public static final String GROUP_SELECTED = "GROUP_SELECTED";
    public static final String GROUP_NUMBER = "GROUP_NUMBER";
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.recyc) RecyclerView mRecyc;
    private VBaseAdapter pickGroupAdapter;
    private Context mContext;
    private ArrayList<FriendsResponseBean> list = new ArrayList<>();
    private HashMap<Integer, String> groupMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick_group;
    }

    @Override
    public BasePresenter getPresenter() {
        return new MeetingPresenter();
    }


    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();

    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("我的群聊");
        mMore.setText("完成");
        initApplyRecyclerView();
        ((MeetingPresenter) mPresenter).getGroupList();

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

    /**
     * 返回群聊json数组
     */
    private void back() {
        if (groupMap.size() != 0) {
            String s = "{";
            for (int i : groupMap.keySet()) {

            }
            Collection<String> collection = groupMap.values();
            Iterator<String> iterator = collection.iterator();
            ArrayList<String> sList = new ArrayList<>();
            while (iterator.hasNext()) {
                sList.add(iterator.next());
            }
            Intent itt = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(GROUP_SELECTED, new Gson().toJson(sList));
            bundle.putString(GROUP_NUMBER, groupMap.size()+"");
            itt.putExtras(bundle);
            setResult(RESULT_OK, itt);
        } else {
            setResult(RESULT_CANCELED);
        }
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
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        pickGroupAdapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<GroupResponseBean>())//
                .setLayout(R.layout.recyc_user_item)//
                .setLayoutHelper(getLinearLayouut())//
                .setHolder(PickGroupHolder.class)//
                .setListener(new ItemListener<Boolean>() {
                    @Override
                    public void onItemClick(View view, int position, Boolean isCheck) {
                        if (isCheck) {
                            groupMap.put(position, ((GroupResponseBean) pickGroupAdapter.getDatas().get(position))
                                    .getGroupid());
                        } else {
                            if (groupMap.containsKey(position))
                                groupMap.remove(position);
                        }
                        if (groupMap.size() != 0) {
                            mTitleTv.setText("我的群聊(" + groupMap.size() + ")");
                        } else {
                            mTitleTv.setText("我的群聊");
                        }

                    }
                });

        delegateAdapter.addAdapter(pickGroupAdapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    private LayoutHelper getLinearLayouut() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        helper.setMargin(0, 1, 0, 0);
        helper.setDividerHeight(1);
        return helper;
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
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
    public void getGroupListSuccess(ArrayList<GroupResponseBean> mData) {
        if (mData.size() != 0) {
            pickGroupAdapter.setData(mData);
            pickGroupAdapter.notifyDataSetChanged();
            new GroupUtils(mContext).saveGroupList(mData);
        }
    }

    @Override
    public void createMeetingSuccess(Meeting mData) {
        
    }

    @Override
    public void joinMeetingSuccess(MeetingBase mData) {
        
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, PickGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
