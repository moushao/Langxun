package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * This activity has a margin.
 */
public class SmallCaptureActivity extends BaseActivity {
    public static final String TAG = "SmallCaptureActivity";
    @BindView(R.id.zxing_barcode_scanner) DecoratedBarcodeView barcodeScannerView;
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_zxing_capture;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        mTitleTv.setText("扫描二维码");
        mTitleBack.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[]
            grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, SmallCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
