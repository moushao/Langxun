package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.EMApi;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 类名: {@link FriendsInfoActivity}
 * <br/> 功能描述: 好友个人信息界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/10/16
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class FriendsInfoActivity extends BaseActivity {
    public static final String TAG = "FriendsInfoActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.sex_icon) ImageView mSexIcon;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.friend_head) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.sex_age) TextView mSexAge;
    @BindView(R.id.area) TextView mArea;
    @BindView(R.id.brief) TextView mBrief;
    @BindView(R.id.album) TextView mAlbum;
    @BindView(R.id.platform) TextView mPlatform;
    @BindView(R.id.send_message) TextView mSendMessage;
    private Context mContext;
    private String account;
    private LoginResponseBean user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_info;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        account = getIntent().getExtras().getString("USER_NAME");
        onTintStatusBar();
        initWidget();
        requestFriendsInfo();
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("详细资料");
        mMore.setText("更多");
        //        mMore.setVisibility(View.INVISIBLE);
        //        if (getIntent().getExtras().getInt("CHAT_TYPE") == Constant.CHATTYPE_SINGLE)
        //            mSendMessage.setVisibility(View.INVISIBLE);
        //        if (account.equals(UserHelper.getEMID())) {
        //            upUI(UserHelper.getInstance().getLogUser());
        //            mSendMessage.setVisibility(View.INVISIBLE);
        //        } else {
        //        }

    }

    @OnClick({R.id.title_back, R.id.more, R.id.album, R.id.send_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                FriendsSettingActivity.startAction(mContext,
                        UserHelper.getEMID(user.getPhone(), user.getType()),
                        user.getUserInfoID()
                        , TAG);
                break;
            case R.id.album:
                if (user != null)
                    MyCircleActivity.startAction(mContext, user, TAG);
                break;
            case R.id.send_message:

                if (mSendMessage.getText().toString().equals("发送消息")) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra(Constant.EXTRA_USER_ID, account);
                    startActivity(intent);
                } else {
                    AddFriendActivity.startAction(mContext, user, TAG);
                }
                break;
        }
    }

    private void requestFriendsInfo() {
        EMApi service = ApiManager.getInstance().getFriendsApi();

        try {
            retrofit2.Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call = service.getUserByIdApi(
                    UserHelper.getInstance().getLogUser().getUserInfoID(),
                    UserHelper.getUserPhoneByEMID(account),
                    UserHelper.getUserType(account));

            call.enqueue(new Callback<BaseResponseBean<ArrayList<LoginResponseBean>>>() {
                @Override
                public void onResponse(retrofit2.Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call,
                                       Response<BaseResponseBean<ArrayList<LoginResponseBean>>> response) {
                    if (response.body() == null || response.body().getData() == null || response.body().getData().size()
                            == 0)
                        return;
                    if ("100".equals(response.body().getStatus())) {
                        mSendMessage.setText("发送消息");
                        mAlbum.setVisibility(View.VISIBLE);
                    } else if ("300".equals(response.body().getStatus()) || "400".equals(response.body().getStatus())) {
                        mSendMessage.setText("添加好友");
                    }
                    UserCacheManager.save(getIntent().getExtras().getString("USER_NAME"),
                            UserHelper.getNickNAme(response.body().getData().get(0).getRealName(), response.body()
                                    .getData().get(0).getType()),
                            response.body().getData().get(0).getUserIcon());

                    upUI(response.body().getData().get(0));
                }

                @Override
                public void onFailure(retrofit2.Call<BaseResponseBean<ArrayList<LoginResponseBean>>> call, Throwable
                        t) {
                    disLoadDialog();
                }
            });

        } catch (Exception c) {
            disLoadDialog();
        }
    }

    /**
     * 更新ui界面
     */
    private void upUI(final LoginResponseBean bean) {

        if (isFinishing())
            return;
        user = bean;
        ImageLoadMnanger.INSTANCE.loadImageErrorHead(mHead, bean.getUserIcon());
        mName.setText(bean.getRealName());
        if ("男".equals(bean.getSex())) {
            mSexAge.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
            mSexIcon.setImageResource(R.drawable.icon_boy);
        }
        mSexAge.setText(bean.getAge() + "");
        mSexAge.setVisibility(View.VISIBLE);
        mSexIcon.setVisibility(View.VISIBLE);
        if (!getIntent().getExtras().getString(Constants.FROM).equals(ChatActivity.TAG)) {
            mSendMessage.setVisibility(View.VISIBLE);
        }
        mArea.setText(TextUtils.isEmpty(bean.getArea()) ? "暂无位置信息" : bean.getArea());
        mBrief.setText(TextUtils.isEmpty(bean.getSignName()) ? "暂无签名" : bean.getSignName());
        mMore.setVisibility(View.VISIBLE);
        switch (bean.getType()) {
            case 2:
                mPlatform.setText("TD");
                break;
            case 3:
                mPlatform.setText("XN");
                break;
        }
        mPlatform.setVisibility(View.VISIBLE);
    }


    /**
     * @param mContext 上下文
     * @param username 环信用户
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String username, int chatType, String from) {
        Intent itt = new Intent(mContext, FriendsInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putString("USER_NAME", username);
        bundle.putInt("CHAT_TYPE", chatType);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
