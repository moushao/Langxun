package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.adapter.ApplyHolder;
import com.langbai.tdhd.adapter.GroomHolder;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.NewFriendsPresenter;
import com.langbai.tdhd.mvp.view.NewFriendsView;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.widget.CircleImageView;
import com.langbai.tdhd.widget.Dialog.AlertDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名: {@link NewFriendsActivity}
 * <br/> 功能描述:新朋友界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/10/9
 */
public class NewFriendsActivity extends BaseActivity implements NewFriendsView {
    public static final String TAG = "NewFriendsActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.request_rcyc) RecyclerView mApplyRcyc;
    @BindView(R.id.no_request_tv) TextView mNoTv;
    @BindView(R.id.groom_rcyc) RecyclerView mGroomRcyc;
    @BindView(R.id.groom_head) CircleImageView mGroomHead;
    @BindView(R.id.groom_name) TextView mGroomName;
    @BindView(R.id.groom_area) TextView mGroomArea;
    @BindView(R.id.add) TextView mAdd;
    @BindView(R.id.groom_layout) LinearLayout mGroomLayout;
    private Context mContext;
    private VBaseAdapter groomAdapter, applyAdapter;
    private int agreePosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_friends;
    }

    @Override
    public BasePresenter getPresenter() {
        return new NewFriendsPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();

        ((NewFriendsPresenter) mPresenter).getApplyList();
        ((NewFriendsPresenter) mPresenter).getGroomList();
    }

    private void initWidget() {
        mTitleTv.setText("新朋友");
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
        initApplyRecyclerView();
        initGroomRecyclerView();
    }

    @OnClick({R.id.title_back, R.id.add})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.add:
                AddFriendActivity.startAction(mContext, (LoginResponseBean) mAdd.getTag(), TAG);
                break;
        }
    }


    private void initApplyRecyclerView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.HORIZONTAL);
        mApplyRcyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mApplyRcyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 5);

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        applyAdapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<ApplyResponseBean>())//
                .setLayout(R.layout.recyc_apply_friends)//
                .setLayoutHelper(new LinearLayoutHelper())//
                .setHolder(ApplyHolder.class)//
                .setListener(new ItemListener<ApplyResponseBean>() {
                    @Override
                    public void onItemClick(View view, int position, ApplyResponseBean mData) {
                        agreePosition = position;
                        UserCacheManager.save(UserHelper.getEMID(mData.phone, mData.type), UserHelper.getNickNAme(mData
                                .realName, mData.type), mData.userIcon);
                        agreeApply(mData);
                    }
                });

        delegateAdapter.addAdapter(applyAdapter);
        mApplyRcyc.setAdapter(delegateAdapter);
    }

    /**
     * 同意好友申请
     */
    private void agreeApply(ApplyResponseBean mData) {
        ((NewFriendsPresenter) mPresenter).getAgreeApply(mData.applyPersonID, mData.type);
    }

    private void initGroomRecyclerView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.HORIZONTAL);
        mGroomRcyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mGroomRcyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 4);

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        groomAdapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<LoginResponseBean>())//
                .setLayout(R.layout.recyc_groom_friends)//
                .setLayoutHelper(new LinearLayoutHelper())//
                .setHolder(GroomHolder.class)//
                .setListener(new ItemListener<LoginResponseBean>() {
                    @Override
                    public void onItemClick(View view, int position, LoginResponseBean mData) {
                        setFirstGroomFriends(mData);
                    }
                });

        delegateAdapter.addAdapter(groomAdapter);
        mGroomRcyc.setAdapter(delegateAdapter);
    }

    private LinearLayoutHelper getLinearLayoutHelp() {
        LinearLayoutHelper layoutHelper = new LinearLayoutHelper();
        return layoutHelper;
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
    public void getApplyList(ArrayList<ApplyResponseBean> mData) {
        if (mData.size() != 0) {
            mApplyRcyc.setVisibility(View.VISIBLE);
            mNoTv.setVisibility(View.GONE);
            applyAdapter.setData(mData);
            applyAdapter.notifyDataSetChanged();
        } else {
            mApplyRcyc.setVisibility(View.GONE);
        }
    }

    /**
     * 同意好友申请信息请求成功
     */
    @Override
    public void getAgreeApplyInfo(final LoginResponseBean mData) {
        ApplyResponseBean user = (ApplyResponseBean) applyAdapter.getDatas().get(agreePosition);
        EMClient.getInstance().contactManager().asyncAcceptInvitation(UserHelper.getEMID(user.phone, user
                .type), new EMCallBack() {
            @Override
            public void onSuccess() {

                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            applyAdapter.removeItem(agreePosition);
                            if (applyAdapter.getItemCount() == 0) {
                                mApplyRcyc.setVisibility(View.GONE);
                                mNoTv.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("环信同意添加好友失败", "code:" + i + " 错误信息:" + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        
    }

    @Override
    public void getGroomList(ArrayList<LoginResponseBean> mData) {
        if (mData.size() != 0) {
            mGroomLayout.setVisibility(View.VISIBLE);
            setFirstGroomFriends(mData.get(0));
            groomAdapter.setData(mData);
            groomAdapter.notifyDataSetChanged();
        } else {
            mGroomLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    private void setFirstGroomFriends(LoginResponseBean mData) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
        Glide.with(mContext).load(mData.getUserIcon()).apply(options).into(mGroomHead);
        switch (mData.getType()) {
            case 1:
                mGroomArea.setText("KF");
                break;
            case 2:
                mGroomArea.setText("TD");
                break;
            case 3:
                mGroomArea.setText("XN");
                break;
        }

        mGroomName.setText(mData.getRealName());
        mAdd.setTag(mData);

    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, NewFriendsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
