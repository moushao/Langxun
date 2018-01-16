package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link EditActivity}
 * <br/> 功能描述:编辑年龄和个人简介的界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class EditActivity extends BaseActivity {
    public final static String TAG = "EditActivity";
    public final static String TYPE = "TYPE";
    public final static String BRIFE = "BRIFE";
    public final static String AGE = "AGE";
    public final static String GROUP_BULLETIN = "GROUP_BULLETIN";
    public final static String CONTENT = "CONTENT";
    public final static int AGE_REQUEST = 100;
    public final static int BRIFE_REQUEST = 101;
    public final static int GROUP_BULLETIN_REQUEST = 103;
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.age) EditText mAge;
    @BindView(R.id.clean) ImageView mClean;
    @BindView(R.id.age_layout) FrameLayout mAgeLayout;
    @BindView(R.id.brife) EditText mBrife;
    @BindView(R.id.totalNum) TextView mTotalNum;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.brife_layout) FrameLayout mBrifeLayout;
    private Context mContext;
    private String requestType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
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
        requestType = getIntent().getStringExtra(TYPE);
        initWidget();
    }

    private void initWidget() {
        mTitleBack.setImageResource(R.drawable.icon_back);
        mTitleBack.setVisibility(View.VISIBLE);
        mMore.setText("完成");
        //        mMore.setVisibility(View.VISIBLE);
        if (requestType.equals(AGE)) {
            mAgeLayout.setVisibility(View.VISIBLE);
            mTitleTv.setText("年龄");
            initOldTextWatcher();
            mAge.setText(getIntent().getStringExtra(CONTENT));
        } else if (requestType.equals(BRIFE)) {
            mAgeLayout.setVisibility(View.GONE);
            mBrifeLayout.setVisibility(View.VISIBLE);
            mTitleTv.setText("个性签名");
            initBrifeTextWatcher();
            mBrife.setText(getIntent().getStringExtra(CONTENT));
        } else if (requestType.equals(GROUP_BULLETIN)) {
            mAgeLayout.setVisibility(View.GONE);
            mBrifeLayout.setVisibility(View.VISIBLE);
            mTitleTv.setText("群公告");
            initBrifeTextWatcher();
            mBrife.setText(getIntent().getStringExtra(CONTENT));
        }
    }

    private void initOldTextWatcher() {
        mAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mAge.getText().length() != 0) {
                    mMore.setVisibility(View.VISIBLE);
                } else {
                    mMore.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initBrifeTextWatcher() {
        mBrife.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBrife.getText().length() != 0) {
                    mMore.setVisibility(View.VISIBLE);
                } else {
                    mMore.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @OnClick({R.id.title_back, R.id.clean, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.clean:
                mAge.setText("");
                break;
            case R.id.more:
                backInfoActivity();
                break;
        }
    }

    private void backInfoActivity() {
        String content = "";
        if (requestType.equals(AGE)) {
            content = mAge.getText().toString();
            int age = Integer.valueOf(mAge.getText().toString());
            if (age > 65 || age < 18) {
                showToast("请输入正确的年龄");
                return;
            }
        } else if (requestType.equals(BRIFE) || requestType.equals(GROUP_BULLETIN)) {
            content = mBrife.getText().toString();
        }

        if (TextUtils.isEmpty(content)) {
            setResult(RESULT_CANCELED);
            finish();
        }

        Intent itt = new Intent();
        itt.putExtra(CONTENT, content);
        setResult(RESULT_OK, itt);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
