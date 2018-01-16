package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 类名: {@link FeedbackActivity}
 * <br/> 功能描述:反馈
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/25
 */
public class FeedbackActivity extends BaseActivity {
    private final static String TAG = "FeedbackActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.totalNum) TextView mTotalNum;
    @BindView(R.id.image_more) ImageView mImageMore;
    @BindView(R.id.toolbar_layout) FrameLayout mToolbarLayout;
    @BindView(R.id.feed) EditText mMessage;
    @BindView(R.id.more) TextView mMore;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        mTitleTv.setText("吐槽大会");
        mMore.setText("提交");
        mMore.setVisibility(View.INVISIBLE);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setImageResource(R.drawable.icon_back);
        initTextWatcher();
    }

    private void initTextWatcher() {
        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTotalNum.setText(mMessage.getText().length() + "/150");
                if (mMessage.getText().length() != 0) {
                    mMore.setVisibility(View.VISIBLE);
                } else {
                    mMore.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.title_back, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                addFeedBack();
                break;
        }
    }

    private void addFeedBack() {
        showToast("ti jiao");
    }


    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, FeedbackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
