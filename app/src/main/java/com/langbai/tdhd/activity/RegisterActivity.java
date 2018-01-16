package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.LogBaseRequestBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.LoginBasePresenter;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.view.AccountView;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.langbai.tdhd.utils.MD5Util;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;
import com.langbai.tdhd.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名: {@link RegisterActivity}
 * <br/> 功能描述:用户注册界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class RegisterActivity extends BaseActivity implements AccountView {
    public static final String TAG = "RegisterActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.phone_ed) EditText mTel;
    @BindView(R.id.password_ed) EditText mPassword;
    @BindView(R.id.password_re_ed) EditText mPasswordRe;
    @BindView(R.id.code_ed) EditText mCode;
    @BindView(R.id.get_code) TextView mGetCode;
    @BindView(R.id.name_ed) EditText mName;
    @BindView(R.id.toolbar) FrameLayout mToolbarLayout;
    private Context mContext;
    private CountDownTimer timer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
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
        initWidget();

    }

    private void initWidget() {
        mToolbarLayout.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
        mToolbarLayout.setBackgroundColor(Color.parseColor("#00000000"));
        mTitleBack.setImageResource(R.drawable.icon_back_white);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("注册");
        mMore.setText("提交");
        mGetCode.setClickable(false);
        setOnChangeListener();
        mTel.setText(getIntent().getStringExtra("phone"));
    }

    @OnClick({R.id.title_back, R.id.more, R.id.get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                if (checkData())
                    regiesterUser();
                break;
            case R.id.get_code:
                starCountDown();
                getCode();
                break;
        }
    }

    private void regiesterUser() {
        LogBaseRequestBean bean = new LogBaseRequestBean();
        bean.realName = mName.getText().toString();
        bean.phone = mTel.getText().toString();
        bean.password = MD5Util.getMD5Result(mPassword.getText().toString());
        bean.code = mCode.getText().toString();
        bean.serviceMobile = "";
        ((LoginBasePresenter) mPresenter).register(bean);
    }

    private boolean checkData() {
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(mTel.getText().toString().trim())) {
            showToast("电话号码不正确,请重新输入");
            return false;
        }
        if (mPassword.getText().length() < 6) {
            showToast("请输入六位以上密码");
            return false;
        }
        if (!mPassword.getText().toString().equals(mPasswordRe.getText().toString())) {
            showToast("两次密码输入不对");
            mPassword.setText("");
            mPasswordRe.setText("");
            return false;
        }
        if (TextUtils.isEmpty(mCode.getText())) {
            showToast("请输入验证码");
            return false;
        }
        if (TextUtils.isEmpty(mName.getText())) {
            showToast("请输入真实姓名");
            return false;
        }
        return true;
    }

    public void getCode() {
        ((LoginBasePresenter) mPresenter).getCode(mTel.getText().toString());
    }

    private void setOnChangeListener() {
        mTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PhoneFormatCheckUtils.isChinaPhoneLegal(mTel.getText().toString().trim())) {
                    mGetCode.setBackgroundResource(R.drawable.code_blue_background);
                    mGetCode.setClickable(true);
                } else {
                    mGetCode.setBackgroundResource(R.drawable.code_gray_background);
                    mGetCode.setClickable(false);
                }
            }
        });
    }

    /**
     * <br/> 方法名称: starCountDown
     * <br/> 方法详述: 开始倒计时
     */
    private void starCountDown() {
        mGetCode.setClickable(false);
        mGetCode.setBackgroundResource(R.drawable.code_gray_background);
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                mGetCode.setText(millisUntilFinished / 1000 + "后重新获取");
            }

            public void onFinish() {
                mGetCode.setText("获取验证码");
                mGetCode.setClickable(true);
                mGetCode.setBackgroundResource(R.drawable.code_blue_background);
            }

        };
        timer.start();
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void registerSuccess(RegisterResponseBean mData) {
        Constants.PHONE = mData.getPhone();
        DiskCacheManager manager = new DiskCacheManager(mContext, Constants.REGISTER_USER);
        manager.put(Constants.REGISTER_USER, mData);
        setResult(-1);
        finish();
    }

    @Override
    public void findPassSuccess() {

    }

    @Override
    public void LoginSuccess(LoginResponseBean mData) {
    }


    @Override
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            //取消定时器,否则报空
            timer.cancel();
        }
        super.onDestroy();
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
