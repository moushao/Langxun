package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.bean.Meeting;
import com.langbai.tdhd.bean.MeetingBase;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.MeetingPresenter;
import com.langbai.tdhd.mvp.view.MeetingView;
import com.langbai.tdhd.utils.UserUtils;
import com.langbai.tdhd.widget.Dialog.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名: {@link MeetingCreateActivity}
 * <br/> 功能描述:会议创建界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class MeetingCreateActivity extends BaseActivity implements MeetingView {
    public static final String TAG = "MeetingCreateActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.meeting_name) EditText mMeetingName;
    @BindView(R.id.person) EditText mPerson;
    @BindView(R.id.meeting_brief) EditText mMeetingBrief;
    @BindView(R.id.select_friends) TextView mSelectFriends;
    @BindView(R.id.has_select_friends) TextView mHasSelectFriends;
    @BindView(R.id.select_group) TextView mSelectGroup;
    @BindView(R.id.has_select_group) TextView mHasSelectGroup;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_create;
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
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("发起会议");
        mMore.setText("完成");
        mPerson.setText(UserHelper.getInstance().getLogUser().getRealName());
    }

    @OnClick({R.id.title_back, R.id.select_friends, R.id.more, R.id.select_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.select_friends:
                Intent ittUSer = new Intent(mContext, PickUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FROM, TAG);
                ittUSer.putExtras(bundle);
                startActivityForResult(ittUSer, PickUserActivity.REQUEST_CODE);
                break;
            case R.id.select_group:
                Intent ittGroup = new Intent(mContext, PickGroupActivity.class);
                Bundle Groupbundle = new Bundle();
                Groupbundle.putString(Constants.FROM, TAG);
                ittGroup.putExtras(Groupbundle);
                startActivityForResult(ittGroup, PickGroupActivity.REQUEST_CODE);
                break;
            case R.id.more:
                if (checkData())
                    ((MeetingPresenter) mPresenter).CreateMeeting(
                            mMeetingName.getText().toString()
                            , mMeetingBrief.getText().toString(),
                            (long[]) mHasSelectFriends.getTag(),
                            (String) mHasSelectGroup.getTag());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 997:

                    mHasSelectGroup.setTag(data.getExtras().getString(PickGroupActivity.GROUP_SELECTED));
                    mHasSelectGroup.setText("已经选择" + data.getExtras().getString(PickGroupActivity.GROUP_NUMBER) + "个群");
                    break;


                case PickUserActivity.REQUEST_CODE:
                    //发起会议,提交的好有中需要包含自己
                    long[] userIDs = data.getLongArrayExtra(PickUserActivity.USER_SELECTED);
                    long[] addIDs = new long[userIDs.length + 1];

                    for (int i = 0; i < userIDs.length; i++) {
                        addIDs[i] = userIDs[i];
                    }
                    addIDs[userIDs.length] = UserHelper.getInstance().getLogUser().getUserInfoID();
                    mHasSelectFriends.setTag(addIDs);
                    mHasSelectFriends.setText("已经选择" + data.getLongArrayExtra(PickUserActivity.USER_SELECTED).length
                            + "个好友");
                    break;
            }
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mMeetingName.getText())) {
            showBaseMessageDialog("请输入会议名称");
            return false;
        }
        if (TextUtils.isEmpty(mMeetingBrief.getText())) {
            showBaseMessageDialog("请输入会议描述");
            return false;
        }
        if (mHasSelectFriends.getTag() == null) {
            showBaseMessageDialog("请先选择好友");
            return false;
        }
        if (mHasSelectGroup.getTag() == null) {
            showBaseMessageDialog("请选择参与会议的群");
            return false;
        }
        return true;
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
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
    public void createMeetingSuccess(final Meeting mData) {


        if (mData != null) {
            //sendMettingNotice(mData);
            new AlertDialog(mContext).setWidthRatio(0.7f).builder()
                    .hideTitleLayout().setMsg("创建会议成功,是否立即进入会议?").setNegativeButton(("取消"), new View.OnClickListener
                    () {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setPositiveButton("确 定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MeetingJoinActivity.startAction(mContext, mData, TAG);
                    finish();
                }
            }).setMessageGravity(Gravity.CENTER).show();
        }
    }

    //发消息给加入会议的人，通知他们加入会议
    private void sendMettingNotice(final Meeting mData) {
        int i = 0;
        final HashMap<Long, String> map = new UserUtils(mContext).getUserInfo();
        for (final Long userId : (long[]) mHasSelectFriends.getTag()) {
            //判断不要给自己发消息
            if (userId.equals(UserHelper.getInstance().getLogUser().getUserInfoID()))
                break;
            i++;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    EMMessage message = EMMessage.createTxtSendMessage("邀请你参加会议,房间号为:" + mData.getMeetingLock(),
                            map.get(userId));
                    EMClient.getInstance().chatManager().sendMessage(message);
                }
            }, 200 * i);
        }
    }

    @Override
    public void joinMeetingSuccess(MeetingBase mData) {

    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, MeetingCreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
