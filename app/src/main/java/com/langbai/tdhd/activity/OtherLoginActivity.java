package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.LogBaseRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.AppManager;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.LoginBasePresenter;
import com.langbai.tdhd.mvp.view.AccountView;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.InputBroadUtils;
import com.langbai.tdhd.utils.MD5Util;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.utils.SharedPreferencesHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherLoginActivity extends BaseActivity implements AccountView {
    public static final String TAG = "OtherLoginActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.log_icon) ImageView mLogIcon;
    @BindView(R.id.platform) TextView mPlatform;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.phone_ed) EditText mTel;
    @BindView(R.id.password_ed) EditText mPass;
    @BindView(R.id.login) TextView mLogin;
    @BindView(R.id.toolbar) FrameLayout mToolbarLayout;
    private Context mContext;
    private int logType;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_login;
    }

    @Override
    public BasePresenter getPresenter() {
        return new LoginBasePresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        SetTranslanteBar();
        mToolbarLayout.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
        initWidget();
        initData();

    }

    private void initData() {
        DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
        LoginResponseBean bean = manager.getSerializable(Constants.LOGIN_USER);
        if (bean != null) {
            mTel.setText(bean.getPhone());
            mPass.setText(bean.getPassword());
        }
        logType = getIntent().getExtras().getInt("TYPE");
        if (logType == 2) {
            mTitleTv.setText("TDC登录");
            mLogIcon.setImageResource(R.drawable.logo_tongdui);
            mPlatform.setText("TDC");
        } else if (logType == 3) {
            mTitleTv.setText("XNC登录");
            mLogIcon.setImageResource(R.drawable.logo_xiaoniu);
            mPlatform.setText("XNC");
        }
    }

    private void initWidget() {
        mToolbarLayout.setBackgroundColor(Color.parseColor("#00000000"));
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_back, R.id.login, R.id.layout})
    public void onViewClicked(View view) {
        if (!(view instanceof EditText)) {
            InputBroadUtils.CloseBroadByView(mContext, view);
        }
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.login:
                if (checkData())
                    LogIn();
                break;
        }
    }

    /**
     * 登录网络请求
     */
    private void LogIn() {
        LogBaseRequestBean bean = new LogBaseRequestBean();
        bean.account = mTel.getText().toString();
        bean.password = mPass.getText().toString();
        bean.type = this.getIntent().getIntExtra("TYPE", 2);
        ((LoginBasePresenter) mPresenter).login(mContext, bean);
    }

    private boolean checkData() {
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(mTel.getText().toString().trim())) {
            showToast("电话号码不正确,请重新输入");
            return false;
        }
        if (mPass.getText().length() < 6) {
            showToast("请输入六位以上密码");
            return false;
        }
        return true;
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    public void registerSuccess(RegisterResponseBean mData) {

    }

    @Override
    public void findPassSuccess() {

    }

    @Override
    public void LoginSuccess(final LoginResponseBean mData) {
        DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
        manager.put(Constants.LOGIN_USER, mData);
        UserHelper.getInstance().setLogUser(mData);
        //用户昵称
        String nickName = mData.getType() == 2 ? mData.getRealName() + "(TD)" : mData.getRealName() + "(XN)";
        String avatarUrl = mData.getUserIcon();// 用户头像（绝对路径）
        UserCacheManager.save(UserHelper.getEMID(mData), nickName, avatarUrl);

        //TODO 环信登录
        EMClient.getInstance().login(UserHelper.getEMID(mData), UserHelper.getEMID(mData), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                SharedPreferencesHelper.putBoolean(mContext, "IS_RELOGIN", false);
                AppManager.getAppManager().finishActivity(LoginActivity.class);
                MainActivity.startAction(mContext, TAG);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.e("Login", "登录失败code：" + code + "  mesage：" + message);
                showBaseMessageDialog("登录聊天服务器失败");
            }
        });

    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, int type, String from) {
        Intent itt = new Intent(mContext, OtherLoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putInt("TYPE", type);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

}
