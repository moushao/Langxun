package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.LoginBasePresenter;
import com.langbai.tdhd.mvp.view.AccountView;
import com.langbai.tdhd.utils.MD5Util;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.widget.Dialog.AlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名: {@link FindPasswordActivity}
 * <br/> 功能描述:找回密码界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class FindPasswordActivity extends BaseActivity implements AccountView {
    public static final String TAG = "FindPasswordActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.tel) EditText mTel;
    @BindView(R.id.verification_code) EditText mCode;
    @BindView(R.id.get_code) TextView mGetCode;
    @BindView(R.id.pass_new) EditText mPassNew;
    @BindView(R.id.pass_re) EditText mPassRe;
    @BindView(R.id.toolbar_layout) FrameLayout mToolbarLayout;
    private Context mContext;
    private CountDownTimer timer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password;
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
        setOnChangeListener();
    }


    private void initWidget() {
        mToolbarLayout.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
        mToolbarLayout.setBackgroundColor(Color.parseColor("#00000000"));
        mTitleBack.setImageResource(R.drawable.icon_back_white);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("忘记密码");
        mMore.setText("确认");
        mGetCode.setClickable(false);
    }

    @OnClick({R.id.more, R.id.title_back, R.id.get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.get_code:
                starCountDown();
                getCode();
                break;
            case R.id.more:
                if (checkData())
                    findPassByRequest();
                break;
        }
    }

    /**
     * 找回密码网络请求
     */
    private void findPassByRequest() {
        LogBaseRequestBean bean = new LogBaseRequestBean();
        bean.phone = mTel.getText().toString();
        bean.code = mCode.getText().toString();
        bean.password = MD5Util.getMD5Result(mPassNew.getText().toString());
        ((LoginBasePresenter) mPresenter).findPass(bean);
    }

    /**
     * 获取验证码网络请求
     */
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
     * 数据校验
     */
    private boolean checkData() {
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(mTel.getText().toString().trim())) {
            showToast("电话号码不正确,请重新输入");
            return false;
        }
        if (mPassNew.length() < 6) {
            showToast("请输入六位以上密码");
            return false;
        }
        if (!mPassNew.getText().toString().equals(mPassRe.getText().toString())) {
            showToast("两次密码输入不正确,请重新输入");
            mPassNew.setText("");
            mPassRe.setText("");
            return false;
        }
        return true;
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
    public void onFailed(String message) {
        showBaseMessageDialog(message);
    }

    @Override
    public void registerSuccess(RegisterResponseBean mData) {

    }

    /**
     * 网络请求修改密码,成功后回调
     */
    @Override
    public void findPassSuccess() {
        Constants.PHONE = mTel.getText().toString().trim();
        new AlertDialog(mContext).builder().setCancelable(false).setMsg("恭喜你,密码找回成功").setMessageGravity(Gravity
                .CENTER).setNegativeButton("确 定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);
                finish();
            }
        }).show();
    }

    @Override
    public void LoginSuccess(LoginResponseBean mData) {

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, FindPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
