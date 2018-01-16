package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.NewFriendsApi;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.RetrofitErrorUtlis;
import com.langbai.tdhd.widget.Dialog.AlertDialog;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends BaseActivity {
    public static final String TAG = "AddFriendActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.apply_message) EditText mApplyMessage;
    @BindView(R.id.clean) ImageView mClean;
    @BindView(R.id.more) TextView mMore;
    private Context mContext;
    private LoginResponseBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
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
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("验证信息");
        bean = (LoginResponseBean) this.getIntent().getExtras().getSerializable("APPLY_USER");
        mApplyMessage.setText(UserHelper.getInstance().getNickNAme(UserHelper.getUserRealName(),
                UserHelper.getUserType()) + "请求添加您为好友");
        mMore.setText("完成");
        mApplyMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(mApplyMessage.getText().toString().trim())) {
                    mMore.setVisibility(View.INVISIBLE);
                } else {
                    mMore.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.title_back, R.id.clean, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.clean:
                mApplyMessage.setText("");
                break;
            case R.id.more:
                applyRequst();
                break;
        }
    }

    private void applyRequst() {
        showLoading("");
        NewFriendsApi service = ApiManager.getInstance().getNewFriendsApi();
        Call<BaseResponseBean> call = service.getApplyFriend(
                bean.getUserInfoID(), UserHelper.getInstance().getLogUser().getUserInfoID(), mApplyMessage.getText()
                        .toString());
        call.enqueue(new Callback<BaseResponseBean>() {
            @Override
            public void onResponse(Call<BaseResponseBean> call, Response<BaseResponseBean> response) {

                if ("100".equals(response.body().getStatus())) {
                    //参数为要添加的好友的username和添加理由
                    EMClient.getInstance().contactManager().aysncAddContact(UserHelper.getEMID(bean),
                            mApplyMessage.getText().toString(), new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    if (!isFinishing()) {
                                        disLoadDialog();
                                        showSuccessDialog();
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    disLoadDialog();
                                    showBaseMessageDialog("好友添加失败,请重试");
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                } else {
                    disLoadDialog();
                    showBaseMessageDialog(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseResponseBean> call, Throwable t) {
                showBaseMessageDialog(RetrofitErrorUtlis.getNetErrorMessage(t));
            }
        });
    }

    private void showSuccessDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog(mContext).setWidthRatio(0.7f).builder()
                        .hideTitleLayout().setMsg("申请添加好友成功,等待对方同意").setNegativeButton(("确 定"), new View.OnClickListener
                        () {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setMessageGravity(Gravity.CENTER).show();
            }
        });
    }


    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, LoginResponseBean bean, String from) {
        Intent itt = new Intent(mContext, AddFriendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putSerializable("APPLY_USER", bean);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

}
