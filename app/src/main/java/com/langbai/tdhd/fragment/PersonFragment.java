package com.langbai.tdhd.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.InfoActivity;
import com.langbai.tdhd.activity.LoginActivity;
import com.langbai.tdhd.activity.MainActivity;
import com.langbai.tdhd.activity.MyCircleActivity;
import com.langbai.tdhd.activity.MyQrCodeActivity;
import com.langbai.tdhd.activity.MySquareActivity;
import com.langbai.tdhd.activity.SettingActivity;
import com.langbai.tdhd.base.BaseFragment;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.SharedPreferencesHelper;
import com.langbai.tdhd.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Moushao on 2017/9/14.
 */

public class PersonFragment extends BaseFragment {
    public static final String TAG = "PersonFragment";


    @BindView(R.id.head) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.brief) TextView mBrief;
    @BindView(R.id.scan) ImageView mScan;
    @BindView(R.id.info) TextView mInfo;
    @BindView(R.id.album) TextView mAlbum;
    @BindView(R.id.square) TextView mSquare;
    @BindView(R.id.setting) TextView mSetting;
    @BindView(R.id.quit) TextView mQuit;

    private Context mContext;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initInjector() {
        mContext = getActivity();
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        SetTranslanteBar();
        LoginResponseBean userBean = UserHelper.getInstance().getLogUser();
        if (userBean == null || TextUtils.isEmpty(userBean.getUserIcon())) {
            mHead.setImageResource(R.drawable.login_head);
        } else {
            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.login_head).error(R
                    .drawable.login_head).priority(Priority.HIGH);
            Glide.with(mContext).load(userBean.getUserIcon()).apply(options).into(mHead);
            mName.setText(userBean.getRealName());
            mBrief.setText(userBean.getSignName());
        }
    }

    @Override
    protected void lazyLoadData() {
    }


    @OnClick({R.id.head, R.id.scan, R.id.info, R.id.album, R.id.square, R.id.setting, R.id.quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head:
                break;
            case R.id.scan:
                MyQrCodeActivity.startAction(mContext, TAG);
                break;
            case R.id.info:
                InfoActivity.startAction(mContext, TAG);
                break;
            case R.id.album:
                MyCircleActivity.startAction(mContext, UserHelper.getInstance().getLogUser(), TAG);
                break;
            case R.id.square:
                MySquareActivity.startAction(mContext, TAG);
                break;
            case R.id.setting:
                SettingActivity.startAction(mContext, TAG);
                break;
            case R.id.quit:
                quitEM();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventMessage event) {
        if (event.getCode().equals("head_pic")) {
            if (mHead != null) {
                Glide.with(mContext).load(event.getMessage()).apply(new RequestOptions().centerCrop().placeholder(R
                        .mipmap.ic_launcher).error(R.mipmap.ic_launcher).priority(Priority.HIGH)).into(mHead);
            }
        } else if (event.getCode().equals("user_brief")) {
            mBrief.setText(event.getMessage());
        }

    }

    private void quitEM() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                gotoLogin();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void gotoLogin() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferencesHelper.putBoolean(mContext, "IS_RELOGIN", true);
                LoginActivity.startAction(mContext, PersonFragment.TAG);
                getActivity().finish();
            }
        });
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
}
