package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.Setting;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.DiskCacheManager;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link ClassSettingActivity}
 * <br/> 功能描述:空中课堂相关设置界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class ClassSettingActivity extends BaseActivity {
    public static final String TAG = "ClassSettingActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.more) TextView mMore;
    @BindView(R.id.new_message_btn) SwitchButton mNewMessageBtn;
    @BindView(R.id.voice_btn) SwitchButton mVoiceBtn;
    @BindView(R.id.shock_btn) SwitchButton mShockBtn;
    @BindView(R.id.recevier_btn) SwitchButton mRecevierBtn;
    @BindView(R.id.dont_disturb) SwitchButton mDontDisturbBtn;
    private Context mContext;
    Setting bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_setting;
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
        DiskCacheManager manager = new DiskCacheManager(this, Constants.SYSTEM_SETTING);
        bean = manager.getSerializable(Constants.SYSTEM_SETTING);
        initWidget();
    }

    private void initWidget() {
        mTitleTv.setText("课堂设置");
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setImageResource(R.drawable.icon_back);
        //        mMore.setText("完成");
        //        mMore.setVisibility(View.VISIBLE);

        mNewMessageBtn.setOnCheckedChangeListener(changeListener);
        mVoiceBtn.setOnCheckedChangeListener(changeListener);
        mShockBtn.setOnCheckedChangeListener(changeListener);
        mRecevierBtn.setOnCheckedChangeListener(changeListener);
        mDontDisturbBtn.setOnCheckedChangeListener(changeListener);
        if (bean != null) {
            mNewMessageBtn.setChecked(bean.isReciveNew());
            mVoiceBtn.setChecked(bean.isVoice());
            mShockBtn.setChecked(bean.isShock());
            mRecevierBtn.setChecked(bean.isHeadphone());
            mDontDisturbBtn.setChecked(bean.isNotDisturb());
        } else {
            bean = new Setting(true, true, true, true, false);
        }
    }

    @OnClick({R.id.title_back, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.more:
                break;
        }
    }

    private SwitchButton.OnCheckedChangeListener changeListener = new SwitchButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            switch (view.getId()) {
                case R.id.new_message_btn:
                    bean.setReciveNew(isChecked);
                    break;
                case R.id.voice_btn:
                    bean.setVoice(isChecked);
                    break;
                case R.id.shock_btn:
                    bean.setShock(isChecked);
                    break;
                case R.id.recevier_btn:
                    bean.setHeadphone(isChecked);
                    break;
                case R.id.dont_disturb:
                    bean.setNotDisturb(isChecked);
                    break;
            }
            DiskCacheManager manager = new DiskCacheManager(mContext, Constants.SYSTEM_SETTING);
            manager.put(Constants.SYSTEM_SETTING, bean);
        }
    };

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, ClassSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
