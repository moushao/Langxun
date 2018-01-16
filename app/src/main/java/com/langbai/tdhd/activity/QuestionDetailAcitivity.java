package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名: {@link QuestionDetailAcitivity}
 * <br/> 功能描述:常见问题的回答
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/21
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class QuestionDetailAcitivity extends BaseActivity {
    public static final String TAG = "QuestionDetailAcitivity";
    public static final String QUSTION_TYPE = "QUSTION_TYPE";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.answer) TextView mAnswer;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question_detail_acitivity;
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
        mTitleTv.setText("问题解答");
        String answer = "";
        switch (getIntent().getIntExtra(QUSTION_TYPE, -1)) {
            case 1:
                answer = "默认平台只提供忘记密码功能，XNC和TDC用户不提供该项功能";
                break;
            case 2:
                answer = "在主界面下拉菜单点击发起会议，填写会议相关信息（会议名称，会议描述，参与会议群，会议参与人），点击完成可进行发起会议操作，会议秘钥会自动生成并发送给相关会议参与人。";
                break;
            case 3:
                answer = "会议秘钥由发起会议人发送信息通知你，收到会议秘钥后在下拉菜单点击加入会议，输入会议秘钥，点击进入会议即可完成进入会议操作。";
                break;
            case 4:
                answer = "聊天记录只能缓存三天";
                break;
            case 5:
                answer = "会议在固定时间段内只能单次进行,当前已有会议正在进行中";
                break;
            case 6:
                answer = "TD和XN用户创建的群组，只有群主可以邀请人进群；KF用户创建的群组，所有群成员都可以邀请人入群";
                break;
            case 7:
                answer = "TD和XN具有一级权限的用户可以创建会议，可以推送所有的群；TD和XN具有二级权限的用户可以创建会议，可以推送自己所在的群；TD和XN具有三级权限的用户只能加入会议；KF" +
                        "具有一级权限的用户可以创建会议，可以推送所有的群；KF具有二级权限的用户可以创建会议，可以推送自己所在的群；KF具有三级权限的用户无法参与会议。\n";
                break;
            default:
                break;
        }
        mAnswer.setText(answer);
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, int type, String from) {
        Intent itt = new Intent(mContext, QuestionDetailAcitivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putInt(QUSTION_TYPE, type);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }


    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }
}
