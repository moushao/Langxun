package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EventMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.FriendsSettingBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.FriendsSettingPresenter;
import com.langbai.tdhd.mvp.view.FriendsSettingView;
import com.langbai.tdhd.widget.Dialog.AlertDialog;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsSettingActivity extends BaseActivity implements FriendsSettingView {


    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.switch_my) SwitchButton mSwitchMy;
    @BindView(R.id.switch_he) SwitchButton mSwitchHe;
    @BindView(R.id.delete_friend) TextView mDeleteFriend;
    @BindView(R.id.title_tv) TextView mTitleTv;

    long friendID;
    private String controlID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_setting;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new FriendsSettingPresenter();
    }

    @Override
    protected void initEventAndData() {
        controlID = getIntent().getExtras().getString("EMID");
        friendID = getIntent().getExtras().getLong("FRIEND_ID");
        onTintStatusBar();
        initWidget();
        ((FriendsSettingPresenter) mPresenter).getFriendsSetting(UserHelper.getUserId(), friendID);
    }

    private void initWidget() {
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("好友管理");
        mSwitchMy.setOnCheckedChangeListener(changeListener);
        mSwitchHe.setOnCheckedChangeListener(changeListener);
    }

    private SwitchButton.OnCheckedChangeListener changeListener = new SwitchButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            int item = -1;

            switch (view.getId()) {
                case R.id.switch_my:
                    item = 0;
                    break;
                case R.id.switch_he:
                    item = 1;
                    break;
            }
            ((FriendsSettingPresenter) mPresenter).handleFriendsStatus(item, isChecked, UserHelper.getUserId(),
                    friendID);
        }
    };


    @OnClick({R.id.title_back, R.id.delete_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.delete_friend:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        new AlertDialog(this)
                .setWidthRatio(0.7f)
                .builder()
                .hideTitleLayout()
                .setMsg("确定删除好友?")
                .setNegativeButton(("确 定"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FriendsSettingPresenter) mPresenter).deleteFirends(UserHelper.getUserId(),
                                friendID);
                    }
                })
                .setPositiveButton("取 消", null)
                .setMessageGravity(Gravity.CENTER)
                .show();
    }

    @Override
    public void getFriendsStatusSuccess(FriendsSettingBean mData) {
        mSwitchHe.setChecked(mData.isOwnBan());
        mSwitchMy.setChecked(mData.isFriendBan());
    }

    @Override
    public void deleteSuccess() {
        EMClient.getInstance().contactManager().aysncDeleteContact(controlID, new
                EMCallBack() {
                    @Override
                    public void onSuccess() {
                        disDialog();
                        //删除成功后，发送消息，通知回话列表删除此人
                        EventBus.getDefault().post(new EventMessage(EventMessage.DELETE_USER, controlID));
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        disDialog();
                        showBaseMessageDialog("好友删除失败,请重试");
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {
        disLoadDialog();
    }

    @Override
    public void onFailed(String message) {

    }

    public static void startAction(Context mContext, String EMID, long friendID, String from) {
        Intent itt = new Intent(mContext, FriendsSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putString("EMID", EMID);
        bundle.putLong("FRIEND_ID", friendID);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
