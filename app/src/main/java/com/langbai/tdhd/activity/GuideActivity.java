package com.langbai.tdhd.activity;

import android.content.Context;


import com.hyphenate.chat.EMClient;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.DiskCacheManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 类名: {@link GuideActivity}
 * <br/> 功能描述:导航启动页面
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/25
 */
public class GuideActivity extends BaseActivity {
    private final static String TAG = "GuideActivity";
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide_activity;
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        SetTranslanteBar();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //首次登录,跳转引导界面
                        if (Constants.IS_FIRST_LOAD) {
                            NavigationActivity.startAction(mContext, TAG);
                        } else {
                            //本地没有用户信息,跳转登录界面,有则跳转主界面
                            DiskCacheManager manager = new DiskCacheManager(mContext, Constants.LOGIN_USER);
                            LoginResponseBean bean = manager.getSerializable(Constants.LOGIN_USER);
                            if (Constants.IS_RELOGIN || bean == null || !EMClient.getInstance().isConnected()) {
                                LoginActivity.startAction(mContext, TAG);
                            } else {
                                UserHelper.getInstance().setLogUser(bean);
                                MainActivity.startAction(mContext, TAG);
                            }

                        }
                        finish();
                    }
                });

            }
        }, 500);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
