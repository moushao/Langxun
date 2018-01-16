package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.adapter.message.EMAMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.runtimepermissions.PermissionsManager;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.chat.ChatFragment;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.MeetingApi;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;
import com.langbai.tdhd.widget.Dialog.AlertDialog;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 类名: {@link ChatActivity}
 * <br/> 功能描述:聊天
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/25
 */
public class ChatActivity extends BaseActivity {
    public final static String TAG = "ChatActivity";
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initInjector() {
        onTintStatusBar();
    }

    @Override
    protected void initEventAndData() {
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString(Constant.EXTRA_USER_ID);
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());

        if (EaseConstant.CHATTYPE_CHATROOM == getIntent().getExtras().getInt(Constant.EXTRA_CHAT_TYPE)) {
            chatFragment.setOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UserHelper.getEMID().equals(getIntent().getExtras().getString
                            (Constant.MEETING_OWNER))) {
                        showNotice();
                    } else {
                        LogOutMeetingEM();
                    }
                }


            });
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    private void showNotice() {
        new AlertDialog(ChatActivity.this).setWidthRatio(0.7f).builder()
                .hideTitleLayout().setMsg("是否退出并终止会议?").setNegativeButton(("取消"), null)
                .setPositiveButton("确 定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForDeleteChatRoom();
                    }
                }).setMessageGravity(Gravity.CENTER).show();
    }

    private void requestForDeleteChatRoom() {
        MeetingApi service = ApiManager.getInstance().getMeetingApi();
        Call<BaseResponseBean> call = service.getEndMeetingApi(toChatUsername);
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    LogOutMeetingEM();
                } else {
                    showBaseMessageDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                showBaseMessageDialog(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    public void LogOutMeetingEM() {
        //进入会议成功后切换环信,进入聊天界面传入聊天室的type
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                changeEMAccount();
            }

            @Override
            public void onError(int i, String s) {
                showToast("退出会议失败,请稍后重试");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void changeEMAccount() {
        String userId = UserHelper.getEMID();
        EMClient.getInstance().login(userId, userId, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtil.e("login", "环信退出会议登录:" + "code" + code + "   message" + message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username)) {
            super.onNewIntent(intent);
        } else {
            finish();
            startActivity(intent);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
    
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * @param ct      上下文
     * @param account 和谁聊天
     */
    public static void startAction(Context ct, String account) {
        Intent itt = new Intent(ct, ChatActivity.class);
        itt.putExtra(EaseConstant.EXTRA_USER_ID, account);
        itt.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMAMessage.EMAChatType.SINGLE);
        ct.startActivity(itt);
    }


}
