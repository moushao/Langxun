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
 * 类名: {@link SettingActivity}
 * <br/> 功能描述:设置总界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class SettingActivity extends BaseActivity {
    public static final String TAG = "SettingActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.sky_class) TextView mSkyClass;
    @BindView(R.id.new_guide) TextView mNewGuide;
    @BindView(R.id.always_question) TextView mAlwaysQuestion;
    @BindView(R.id.about_us) TextView mAboutUs;
    @BindView(R.id.feed_back) TextView mFeedBack;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
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

        mTitleTv.setText("系统设置");
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setImageResource(R.drawable.icon_back);
    }

    @OnClick({R.id.title_back, R.id.sky_class, R.id.new_guide, R.id.always_question, R.id.about_us, R.id.feed_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.sky_class:
                ClassSettingActivity.startAction(mContext, TAG);
                break;
            case R.id.new_guide:
                NoviceGuidanceActivity.startAction(mContext, TAG);
                break;
            case R.id.always_question:
                QuestionActivity.startAction(mContext, TAG);
                break;
            case R.id.about_us:
                AboutUsActivity.startAction(mContext, TAG);
                break;
            case R.id.feed_back:
                FeedbackActivity.startAction(mContext, TAG);
                break;
        }
    }


    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
