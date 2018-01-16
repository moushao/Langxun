package com.langbai.tdhd.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EventMessage;
import com.langbai.tdhd.DemoHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.FriendsInfoActivity;
import com.langbai.tdhd.activity.GroupCreateActivity;
import com.langbai.tdhd.activity.InfoActivity;
import com.langbai.tdhd.activity.MeetingCreateActivity;
import com.langbai.tdhd.activity.MeetingJoinActivity;
import com.langbai.tdhd.activity.SearchActivity;
import com.langbai.tdhd.activity.SmallCaptureActivity;
import com.langbai.tdhd.activity.chat.ContactListFragment;
import com.langbai.tdhd.activity.chat.TalkListFragment;
import com.langbai.tdhd.adapter.DataCompareFragmentAdapter;
import com.langbai.tdhd.base.BaseFragment;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.ScreenUtil;
import com.langbai.tdhd.widget.CircleImageView;
import com.langbai.tdhd.widget.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Moushao on 2017/9/14.
 */

public class ClassFragment extends BaseFragment {
    public static final String TAG = "ClassFragment";
    @BindView(R.id.message) TextView mMessage;
    @BindView(R.id.mail_list) TextView mMai;
    @BindView(R.id.image_more) ImageView mImageMore;
    @BindView(R.id.toolbar_layout) FrameLayout mToolbarLayout;
    @BindView(R.id.title_layout) RelativeLayout mTitleLayout;
    @BindView(R.id.head) CircleImageView mHead;
    @BindView(R.id.class_pager) CustomViewPager mPager;
    private Context mContext;
    private CustomPopWindow popMenu;
    private int lastIndex;
    private TalkListFragment mConversation = new TalkListFragment();

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class;
    }

    @Override
    protected void initInjector() {
        mContext = getActivity();
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
        initFragmentUI();
        //注册一个监听连接状态的listener


    }

 

    @Override
    protected void lazyLoadData() {

    }

    private void initFragmentUI() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(mConversation);
        fragmentList.add(new ContactListFragment());
        DataCompareFragmentAdapter adapter = new DataCompareFragmentAdapter(getChildFragmentManager(), new
                String[]{"消息", "通讯录"}, fragmentList);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);
        //mPager.setPagingEnabled(false);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeIconState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initWidget() {
        mToolbarLayout.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
        mTitleLayout.setVisibility(View.VISIBLE);
        mImageMore.setVisibility(View.VISIBLE);
        mHead.setVisibility(View.VISIBLE);
        mImageMore.setVisibility(View.VISIBLE);
        LoginResponseBean userBean = UserHelper.getInstance().getLogUser();
        if (userBean == null || TextUtils.isEmpty(userBean.getUserIcon())) {
            mHead.setImageResource(R.drawable.login_head);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.login_head)
                    .error(R.drawable.login_head)
                    .priority(Priority.HIGH);
            Glide.with(mContext).load(userBean.getUserIcon()).apply(options).into(mHead);
        }

    }


    @OnClick({R.id.message, R.id.mail_list, R.id.image_more, R.id.head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head:
                InfoActivity.startAction(mContext, TAG);
                break;
            case R.id.message:
                mPager.setCurrentItem(0);
                break;
            case R.id.mail_list:
                mPager.setCurrentItem(1);
                break;
            case R.id.image_more:
                initMenuDialog();
                break;
        }
    }

    private void changeIconState(int position) {
        if (lastIndex == position)
            return;
        switch (position) {
            case 0:
                mMessage.setTextColor(Color.parseColor("#029df2"));
                mMessage.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_title_left));
                mMai.setTextColor(Color.parseColor("#FFFFFF"));
                mMai.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_title_right_un));
                break;
            case 1:
                mMessage.setTextColor(Color.parseColor("#FFFFFF"));
                mMessage.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_title_left_un));
                mMai.setTextColor(Color.parseColor("#029df2"));
                mMai.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_title_right));
                break;
        }
        lastIndex = position;
    }

    private void initMenuDialog() {
        TextView joinMeeting;
        TextView startMeeting;
        TextView createGroup;
        View view3;
        View view4;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_menu, null);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popMenu != null) {
                    popMenu.dissmiss();
                }
                String showContent = "";
                switch (v.getId()) {
                    case R.id.add_friends:
                        SearchActivity.startAction(mContext, "", TAG);
                        break;
                    case R.id.scan:
                        //SmallCaptureActivity.startAction(mContext, TAG);

                        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ClassFragment.this);
                        integrator.setCaptureActivity(SmallCaptureActivity.class);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setCameraId(0); //前置或者后置摄像头  
                        integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启  
                        integrator.setBarcodeImageEnabled(true);
                        integrator.setOrientationLocked(true);
                        integrator.initiateScan();

                        break;
                    case R.id.join_meeting:
                        MeetingJoinActivity.startAction(mContext, null, TAG);
                        break;
                    case R.id.star_meeting:
                        MeetingCreateActivity.startAction(mContext, TAG);
                        break;
                    case R.id.create_group:
                        GroupCreateActivity.startAction(mContext, TAG);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.add_friends).setOnClickListener(listener);
        contentView.findViewById(R.id.scan).setOnClickListener(listener);
        joinMeeting = contentView.findViewById(R.id.join_meeting);
        startMeeting = contentView.findViewById(R.id.star_meeting);
        createGroup = contentView.findViewById(R.id.create_group);
        view3 = contentView.findViewById(R.id.view3);
        view4 = contentView.findViewById(R.id.view4);

        joinMeeting.setOnClickListener(listener);
        startMeeting.setOnClickListener(listener);
        createGroup.setOnClickListener(listener);

        //平台用户,三级权限不能发起会议和加入会议
        if (UserHelper.getInstance().getLogUser().getType() == 1 && UserHelper.getInstance().getLogUser().getUserType
                () == 3) {
            startMeeting.setVisibility(View.GONE);
            joinMeeting.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }

        if (UserHelper.getInstance().getLogUser().getType() != 1 && UserHelper.getInstance().getLogUser().getUserType
                () == 3) {
            createGroup.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }

        popMenu = new CustomPopWindow.PopupWindowBuilder(mContext).setView(contentView).setFocusable(true)
                .setOutsideTouchable(true).enableBackgroundDark(true).setBgDarkAlpha(0.9f).create();
        popMenu.showAsDropDown(mImageMore, -mImageMore.getWidth(), 10);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventMessage message) {
        if (message.getCode().equals("head_pic"))
            if (mHead != null) {
                Glide.with(mContext).load(message.getMessage()).apply(new RequestOptions().centerCrop().placeholder(R
                        .mipmap.ic_launcher).error(R.mipmap.ic_launcher).priority(Priority.HIGH)).into(mHead);
            }
    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        mConversation.refresh();
    }

    @Override
    public void onResume() {
        // unregister this event listener when this activity enters the background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(getActivity());
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        super.onResume();
    }

    @Override
    public void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(getActivity());
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
