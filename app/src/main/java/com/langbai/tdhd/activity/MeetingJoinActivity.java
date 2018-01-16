package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.MeetingPresenter;
import com.langbai.tdhd.mvp.view.MeetingView;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link MeetingJoinActivity}
 * <br/> 功能描述:加入会议界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class MeetingJoinActivity extends BaseActivity implements MeetingView {
    public static final String TAG = "MeetingJoinActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.room_number) EditText mRoomNumber;
    @BindView(R.id.join_meeting) TextView mJoin;
    private Context mContext;
    private Meeting meeting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_join;
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
        if (MeetingCreateActivity.TAG.equals(getIntent().getExtras().getString(Constants.FROM))) {
            meeting = (Meeting) getIntent().getExtras().getSerializable("MEETING");
            mRoomNumber.setText(meeting.getMeetingLock());
        }
    }

    private void initWidget() {
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("进入会议");
    }

    @OnClick({R.id.title_back, R.id.join_meeting})
    public void onViewClicked(View view) {
        hideKeyboard();
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.join_meeting:
                if (TextUtils.isEmpty(mRoomNumber.getText().toString()))
                    return;
                ((MeetingPresenter) mPresenter).joinMeeting(mRoomNumber.getText().toString());
                break;
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
        showBaseMessageDialog(message);
    }

    @Override
    public void getGroupListSuccess(ArrayList<GroupResponseBean> mData) {

    }

    @Override
    public void createMeetingSuccess(Meeting mData) {

    }

    @Override
    public void joinMeetingSuccess(final MeetingBase mData) {
        //进入会议成功后切换环信,进入聊天界面传入聊天室的type
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                changeEMAccount(mData);
            }

            @Override
            public void onError(int i, String s) {
                showToast("进入会议失败,请稍后重试");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void changeEMAccount(final MeetingBase mData) {
        final String userId = "meet" + UserHelper.getInstance().getLogUser().getUserInfoID();

        EMClient.getInstance().login(userId, userId, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                UserCacheManager.save(userId,
                        getUserMeetingNick(),
                        UserHelper.getInstance().getLogUser().getUserIcon());
                goToChat(mData);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtil.e("环信登录", "code" + code + "   message" + message);
                showBaseMessageDialog("聊天服务器登录失败");
            }
        });
    }

    private String getUserMeetingNick() {
        String nickName;
        switch (UserHelper.getUserType()) {
            case 1:
                nickName = UserHelper.getUserRealName() + "(KF-会议)";
                break;
            case 2:
                nickName = UserHelper.getUserRealName() + "(TD-会议)";
                break;
            case 3:
                nickName = UserHelper.getUserRealName() + "(XN-会议)";
                break;
            default:
                nickName = UserHelper.getUserRealName();
                break;
        }
        return nickName;
    }

    private void goToChat(final MeetingBase mData) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, ChatActivity.class);
                // it's room chat
                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                intent.putStringArrayListExtra(Constant.MEETING_GROUP, mData.getMeetingGroup());
                intent.putExtra(Constant.EXTRA_USER_ID, mData.getMeetDetail().get(0).getMeetingHXID());
                intent.putExtra(Constant.MEETING_OWNER, mData.getMeetDetail().get(0).getOwner());
                startActivity(intent);
                finish();
            }
        });


    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, Meeting meeting, String from) {
        Intent itt = new Intent(mContext, MeetingJoinActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putSerializable("MEETING", meeting);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
