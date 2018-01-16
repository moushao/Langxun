package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.easeui.EaseConstant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.fragment.ClassFragment;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.ImageUtils;
import com.langbai.tdhd.utils.qr.BGAQRCodeUtil;
import com.langbai.tdhd.utils.qr.QRCodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link MyQrCodeActivity}
 * <br/> 功能描述:我的二维码界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class MyQrCodeActivity extends BaseActivity {
    public static final String TAG = "MyQrCodeActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.head_qr) ImageView mHeadQr;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.qr) ImageView mQr;
    private Context mContext;
    private RequestOptions optionsHead = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.login_head)
            .error(R.drawable.login_head)
            .priority(Priority.HIGH);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_qr_code;
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
        // ImageLoadMnanger.INSTANCE.loadImageErrorHead(mHeadQr, UserHelper.getInstance().getLogUser().getUserIcon());
        Glide.with(mContext)
                .load(UserHelper.getInstance().getLogUser().getUserIcon())
                .apply(optionsHead)
                .into(new DrawableImageViewTarget(mHeadQr) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super
                            Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        mHeadQr.setImageDrawable(resource);
                        createEnglishQRCode(resource);
                    }
                });
    }

    private void initWidget() {
        mTitleTv.setText("我的二维码");
        mMore.setText("扫描");
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setImageResource(R.drawable.icon_back);
        mQr.setVisibility(View.INVISIBLE);
    }

    private void createEnglishQRCode(final Drawable resource) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
        .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB
        %E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = ImageUtils.drawableToBitmap(resource);
                return QRCodeEncoder.syncEncodeQRCode(
                        UserHelper.getInstance().getLogUser().getPhone() + UserHelper.getUserType(),
                        BGAQRCodeUtil.dp2px(MyQrCodeActivity.this, 200),
                        Color.BLACK,
                        Color.WHITE,
                        logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    mQr.setImageBitmap(bitmap);
                    mQr.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MyQrCodeActivity.this, "二维码生成失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @OnClick({R.id.title_back, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                gotoScan();
                break;
        }
    }

    private void gotoScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCameraId(0); //前置或者后置摄像头  
        integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启  
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Log.d("MainActivity", "扫描失败");
                    Toast.makeText(mContext, "扫描失败", Toast.LENGTH_LONG).show();

                } else {
                    String content = result.getContents();
                    Log.d("Scanned", "Scanned：" + content);
                    if (UserHelper.isLegalQrContent(content)) {
                        if (content.contains(UserHelper.getInstance().getLogUser().getPhone())) {
                            InfoActivity.startAction(mContext, TAG);
                        } else {
                            FriendsInfoActivity.startAction(mContext, content, EaseConstant.CHATTYPE_SINGLE, TAG);
                        }
                    } else {
                        showToast(content);
                    }
                }
            } else {
                //This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, MyQrCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
}
