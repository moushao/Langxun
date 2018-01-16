package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link QuestionActivity}
 * <br/> 功能描述:常见问题的汇总
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/21
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class QuestionActivity extends BaseActivity {
    public static final String TAG = "QuestionActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.how_to_change_password) TextView mHowToChangePassword;
    @BindView(R.id.how_to_start_meeting) TextView mHowToStartMeeting;
    @BindView(R.id.how_to_join_meeting) TextView mHowToJoinMeeting;
    @BindView(R.id.why_cannot_see_chat_history) TextView mWhyCannotSeeChatHistory;
    @BindView(R.id.star_meeting_failed) TextView mStarMeetingFailed;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
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
        onTintStatusBar();
        initWidget();
    }

    private void initWidget() {
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("常见问题");
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, QuestionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }


    @OnClick({R.id.title_back, R.id.toolbar_layout, R.id.how_to_change_password, R.id.how_to_start_meeting, R.id
            .how_to_join_meeting, R.id.why_cannot_see_chat_history, R.id.star_meeting_failed, R.id
            .cannot_handle_metting, R.id.cannot_handle_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.how_to_change_password:
                QuestionDetailAcitivity.startAction(mContext, 1, TAG);
                break;
            case R.id.how_to_start_meeting:
                QuestionDetailAcitivity.startAction(mContext, 2, TAG);
                break;
            case R.id.how_to_join_meeting:
                QuestionDetailAcitivity.startAction(mContext, 3, TAG);
                break;
            case R.id.why_cannot_see_chat_history:
                QuestionDetailAcitivity.startAction(mContext, 4, TAG);
                break;
            case R.id.star_meeting_failed:
                QuestionDetailAcitivity.startAction(mContext, 5, TAG);
                break;
            case R.id.cannot_handle_group:
                QuestionDetailAcitivity.startAction(mContext, 6, TAG);
                break;
            case R.id.cannot_handle_metting:
                QuestionDetailAcitivity.startAction(mContext, 7, TAG);
                break;
        }
    }
}
