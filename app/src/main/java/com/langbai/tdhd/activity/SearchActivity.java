package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.adapter.SearchUserHolder;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.EMPresenter;
import com.langbai.tdhd.mvp.view.EMView;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements EMView {
    public static final String TAG = "SearchActivity";
    public static final String PHONE = "PHONE";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    //    @BindView(R.id.expand) ExpandableListView mExpand;
    @BindView(R.id.query) EditText mQuery;
    @BindView(R.id.search_clear) ImageButton mSearchClear;
    @BindView(R.id.new_friends) TextView mNewFriends;
    @BindView(R.id.sacn) TextView mSacn;
    @BindView(R.id.layout) LinearLayout mItemLayout;
    @BindView(R.id.about) TextView mAbout;
    @BindView(R.id.recyc) RecyclerView mRecyc;
    @BindView(R.id.nothing) View mNothing;
    @BindView(R.id.recyc_layout) LinearLayout mRecycLayout;
    @BindView(R.id.serch_text) TextView mSerchText;
    @BindView(R.id.serch_layout) LinearLayout mSerchLayout;
    private Context mContext;
    private VBaseAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public BasePresenter getPresenter() {
        return new EMPresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
        initRecyc();
        if (SmallCaptureActivity.TAG.equals(getIntent().getExtras().get(Constants.FROM))) {
            String phone = getIntent().getExtras().getString(PHONE);
            mQuery.setText(phone);
            mAbout.setText("正在搜索" + phone + "...");
            ((EMPresenter) mPresenter).searchFriends(phone);
        }
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("添加");
        mQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mRecycLayout.setVisibility(View.GONE);
                String text = mQuery.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    mSerchLayout.setVisibility(View.GONE);
                    mItemLayout.setVisibility(View.VISIBLE);
                } else {
                    mSerchLayout.setVisibility(View.VISIBLE);
                    mItemLayout.setVisibility(View.GONE);
                    mSerchText.setText("点击搜索: " + text);
                }
            }
        });
    }

    @OnClick({R.id.title_back, R.id.new_friends, R.id.sacn, R.id.nothing, R.id.serch_layout})
    public void onViewClicked(View view) {
        hideKeyboard();
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.new_friends:
                // 进入申请与通知页面
                NewFriendsActivity.startAction(mContext, TAG);
                break;
            case R.id.sacn:
                if (getIntent().getExtras().getString(Constants.FROM).equals(SmallCaptureActivity.TAG)) {
                    finish();
                } else {
                    gotoScan();
                }
                break;
            case R.id.serch_layout:
                mAbout.setText("正在搜索" + mQuery.getText().toString() + "...");
                ((EMPresenter) mPresenter).searchFriends(mQuery.getText().toString());
                break;
            case R.id.nothing:
                mRecycLayout.setVisibility(View.GONE);
                mItemLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    //跳转二维码扫描界面
    private void gotoScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCameraId(0); //前置或者后置摄像头  
        integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启  
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    private void initRecyc() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 3);

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        adapter = new VBaseAdapter(mContext)//
                .setData(new ArrayList<LoginResponseBean>())//
                .setLayout(R.layout.recyc_search_user_item)//
                .setLayoutHelper(getLinearLayouut())//
                .setHolder(SearchUserHolder.class)//
                .setListener(new ItemListener<LoginResponseBean>() {
                    @Override
                    public void onItemClick(View view, int position, LoginResponseBean bean) {
                        hideKeyboard();
                        AddFriendActivity.startAction(mContext, bean, TAG);

                    }
                });

        delegateAdapter.addAdapter(adapter);
        mRecyc.setAdapter(delegateAdapter);
    }

    private LayoutHelper getLinearLayouut() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        helper.setMargin(0, 1, 0, 0);
        helper.setDividerHeight(1);
        return helper;
    }

    private boolean isNumber(String text) {

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
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
        mAbout.setText("未查询到" + mQuery.getText().toString() + "的相关信息");
        mSerchLayout.setVisibility(View.GONE);
        mRecycLayout.setVisibility(View.VISIBLE);
        adapter.setData(new ArrayList<LoginResponseBean>());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getContractListSuccess(List<EaseUser> mData) {

    }

    @Override
    public void FindFriendsSuccess(ArrayList<LoginResponseBean> mData) {
        mItemLayout.setVisibility(View.GONE);
        mSerchLayout.setVisibility(View.GONE);
        mRecycLayout.setVisibility(View.VISIBLE);
        if (mData.size() == 0) {
            mAbout.setText("未查询到" + mQuery.getText().toString() + "的相关信息");
        } else {
            mAbout.setText("与" + mQuery.getText().toString() + "有关搜索");
            adapter.setData(mData);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteSuccess() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Log.d("MainActivity", "扫描失败");
                    Toast.makeText(mContext, "扫描失败", Toast.LENGTH_LONG).show();

                } else {
                    String content = result.getContents();
                    Log.d("Scanned", "Scanned：" + content);
                    if (UserHelper.isLegalQrContent(content)) {
                        if (content.contains(UserHelper.getInstance().getLogUser().getPhone())) {
                            InfoActivity.startAction(mContext, TAG);
                        } else {
                            FriendsInfoActivity.startAction(mContext, content, EaseConstant.CHATTYPE_SINGLE, TAG);
                        }
                    } else {
                        showToast(content);
                    }
                }
            } else {
                //This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String phone, String from) {
        Intent itt = new Intent(mContext, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putString(PHONE, phone);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
