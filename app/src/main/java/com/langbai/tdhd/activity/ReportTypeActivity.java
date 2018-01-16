package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportTypeActivity extends BaseActivity {
    public static final String TAG = "ReportTypeActivity";
    public static final int REQUEST_CODE = 994;
    public static final String REQUEST_RESULT = "REQUEST_RESULT";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.cheat) TextView mCheat;
    @BindView(R.id.pornographic) TextView mPornographic;
    @BindView(R.id.false_information) TextView mFalseInformation;
    @BindView(R.id.induced_behavior) TextView mInducedBehavior;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report_type;
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
        initWidget();
    }

    private void initWidget() {
        onTintStatusBar();


    }

    @OnClick({R.id.title_back, R.id.cheat, R.id.pornographic, R.id.false_information, R.id.induced_behavior})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.cheat:
                backToSquareActivity(0);
                break;
            case R.id.pornographic:
                break;
            case R.id.false_information:
                break;
            case R.id.induced_behavior:
                break;
        }
    }

    private void backToSquareActivity(int i) {
        Intent itt = new Intent();
        itt.putExtra(REQUEST_RESULT, i);
        setResult(RESULT_OK, itt);
        finish();
    }


    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, ReportTypeActivity.class);
        /*Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);*/
        ((SquareActivity) mContext).startActivityForResult(itt, ReportTypeActivity.REQUEST_CODE);
    }
}
