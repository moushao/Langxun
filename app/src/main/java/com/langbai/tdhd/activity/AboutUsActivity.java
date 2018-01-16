package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * 类名: {@link AboutUsActivity}
 * <br/> 功能描述:关于我们
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class AboutUsActivity extends BaseActivity {
    public static final String TAG = "AboutUsActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.editon) TextView mEditon;
    @BindView(R.id.website) TextView mWebsite;
    @BindView(R.id.platform) TextView mPlatform;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
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
        mTitleTv.setText("关于我们");
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "";
        }
        mEditon.setText(appVersion);
    }

    @OnClick({R.id.title_back, R.id.website, R.id.platform})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.website:
                openWebView("https://www.sztdcoin.com/");
                break;
            case R.id.platform:
                openWebView("https://www.sztdcoin.com/Article/Index/0101");
                break;
        }
    }

    public void openWebView(String url) {
        Intent intent = new Intent();
        //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);

    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, AboutUsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
