package com.langbai.tdhd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
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
import com.langbai.tdhd.utils.JsonFormat;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.MD5Util;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.utils.SharedPreferencesHelper;

import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements AccountView {
    public static final String TAG = "LoginActivity";
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.phone_ed) EditText mTel;
    @BindView(R.id.password_ed) EditText mPass;
    @BindView(R.id.register) TextView mRegister;
    @BindView(R.id.find_pass) TextView mFindPass;
    @BindView(R.id.login) TextView mLogin;
    @BindView(R.id.tongdui_login) ImageView mTDLogin;
    private Context mContext;
    private boolean normalLogin, EMLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
        mTitle.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
        initWidget();

        AppManager.getAppManager().finishAllActivityWithout(this);
    }

    private void initWidget() {
        DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
        LoginResponseBean bean = manager.getSerializable(Constants.LOGIN_USER);
        if (bean != null) {
            mTel.setText(bean.getPhone());
            mPass.setText(bean.getPassword());
        }
        mTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPass.setText("");
            }
        });
    }

    @OnClick({R.id.register, R.id.find_pass, R.id.login, R.id.tongdui_login, R.id.xiaoniu_login, R.id.log_layout})
    public void onViewClicked(View view) {
        if (!(view instanceof EditText)) {
            InputBroadUtils.CloseBroadByView(mContext, view);
        }
        switch (view.getId()) {
            case R.id.register:
                gotoRegisterActivity();
                break;
            case R.id.find_pass:
                gotoFindPasswordActivity();
                break;
            case R.id.login:
                if (checkData()) {
                    EMLogin();
                    LogIn();
                }

                break;
            case R.id.tongdui_login:
                OtherLoginActivity.startAction(mContext, 2, TAG);
                break;
            case R.id.xiaoniu_login:
                OtherLoginActivity.startAction(mContext, 3, TAG);
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
        bean.type = 1;
        showLoading("登陆中...");
        ((LoginBasePresenter) mPresenter).login(mContext, bean);
    }

    private void EMLogin() {
        //TODO 环信登录
        EMClient.getInstance().login(
                mTel.getText().toString() + "1", mTel.getText().toString() + "1", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMLogin = true;
                        gotoMainActivity();
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        LogUtil.e("环信登录", "code" + code + "   message" + message);
                        disLoadDialog();
                        showBaseMessageDialog("聊天服务器登录失败");
                    }
                });
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

    private void gotoRegisterActivity() {
        Intent itt = new Intent(mContext, RegisterActivity.class);
        itt.putExtra("phone", mTel.getText().toString());
        startActivityForResult(itt, 9999);
    }


    private void gotoFindPasswordActivity() {
        Intent itt = new Intent(mContext, FindPasswordActivity.class);
        itt.putExtra("phone", mTel.getText().toString());
        startActivityForResult(itt, 9999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            mTel.setText(Constants.PHONE);
        }
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {
        disLoadDialog();
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
        UserHelper.getInstance().setLogUser(mData);
        String nickName = mData.getRealName() + "(KF)";// 用户昵称
        String avatarUrl = mData.getUserIcon();// 用户头像（绝对路径）
        UserCacheManager.save(mData.getPhone() + "1", nickName, avatarUrl);
        normalLogin = true;
        gotoMainActivity();
    }


    private void gotoMainActivity() {
        if (normalLogin && EMLogin) {
            disLoadDialog();
            SharedPreferencesHelper.putBoolean(mContext, "IS_RELOGIN", false);
            MainActivity.startAction(mContext, TAG);
            finish();
        }
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
