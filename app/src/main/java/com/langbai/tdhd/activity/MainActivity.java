package com.langbai.tdhd.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.langbai.tdhd.R;
import com.langbai.tdhd.adapter.DataCompareFragmentAdapter;
import com.langbai.tdhd.api.ApiManager;
import com.langbai.tdhd.api.UPApi;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.common.AppManager;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.fragment.ClassFragment;
import com.langbai.tdhd.fragment.FindFragment;
import com.langbai.tdhd.fragment.PersonFragment;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.appupdate.AppUpdateManager;
import com.langbai.tdhd.utils.permission.CheckPermListener;
import com.langbai.tdhd.widget.CustomViewPager;
import com.langbai.tdhd.widget.Dialog.AlertDialog;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";
    @BindView(R.id.pager) CustomViewPager mPager;
    @BindView(R.id.class_icon) ImageView mClassIcon;
    @BindView(R.id.class_tv) TextView mClassTv;
    @BindView(R.id.class_layout) LinearLayout mClassLayout;
    @BindView(R.id.find_icon) ImageView mFindIcon;
    @BindView(R.id.find_tv) TextView mFindTv;
    @BindView(R.id.find_layout) LinearLayout mFindLayout;
    @BindView(R.id.personal_icon) ImageView mPersonalIcon;
    @BindView(R.id.personal_tv) TextView mPersonalTv;
    @BindView(R.id.person_layout) LinearLayout mPersonLayout;
    @BindView(R.id.content_layout) LinearLayout mContentLayout;
    private Context mContext;
    private int lastIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        Stack<Activity> activityStack = AppManager.getActivityStack();
        for (Activity activity : activityStack) {
            Log.e("activityStack:", activity.toString());
        }
        initWidget();
        loadEM();

        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                checkUp();
                            }
                        }, R.string.need_permision, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void loadEM() {
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().asyncGetJoinedGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {


            @Override
            public void onSuccess(List<EMGroup> emGroups) {
                Log.e("群聊获取成功", new Gson().toJson(emGroups));
                EMClient.getInstance().groupManager().loadAllGroups();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("群聊获取失败", "code:" + i + "   mesaage:" + s);

            }
        });
    }


    private void initWidget() {
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ClassFragment());
        //        fragmentList.icon_add(new Fragment_Second());
        fragmentList.add(new FindFragment());
        //        fragmentList.icon_add(new Fragment_Third());
        fragmentList.add(new PersonFragment());
        DataCompareFragmentAdapter adapter = new DataCompareFragmentAdapter(getSupportFragmentManager(), new
                String[]{"浪讯", "发现", "我"}, fragmentList);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(3);
        mPager.setPagingEnabled(false);
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

    @OnClick({R.id.class_layout, R.id.find_layout, R.id.person_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.class_layout:
                mPager.setCurrentItem(0);
                break;
            case R.id.find_layout:
                mPager.setCurrentItem(1);
                break;
            case R.id.person_layout:
                mPager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 更新检测版本号
     */
    private void checkUp() {
        String pkName = this.getPackageName();
        String versionName = "";
        try {
            versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(versionName))
            return;
        UPApi service = ApiManager.getInstance().getUPApi();
        Call<BaseResponseBean<String>> call = service.getUPApi(versionName, "2");
        call.enqueue(new Callback<BaseResponseBean<String>>() {
            @Override
            public void onResponse(Call<BaseResponseBean<String>> call, Response<BaseResponseBean<String>> response) {
                if (response.body() == null)
                    return;
                if (response.body().getStatus().equals("100")) {
                    //FileDownloader.setup(mContext);
                    //AppUpdateManager.getInstance(mContext, response.body().getData(), "2").downloadAPK();
                    final String url = response.body().getData();
                    new AlertDialog(mContext)
                            .builder()
                            .setMessageGravity(Gravity.CENTER)
                            .setTitle("版本更新")
                            .setMsg("有新版本啦,请点击更新!")
                            .setPositiveButton("更  新", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openWebView(url);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseBean<String>> call, Throwable t) {

            }
        });
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
     * 底部导航栏的改变
     */
    private void changeIconState(int position) {
        if (lastIndex == position)
            return;
        switch (lastIndex) {
            case 0:
                mClassIcon.setImageResource(R.drawable.icon_class_un);
                mClassTv.setTextColor(Color.parseColor("#7b8191"));
                break;
            case 1:
                mFindIcon.setImageResource(R.drawable.icon_find_un);
                mFindTv.setTextColor(Color.parseColor("#7b8191"));
                break;
            case 2:
                mPersonalIcon.setImageResource(R.drawable.icon_person_un);
                mPersonalTv.setTextColor(Color.parseColor("#7b8191"));
                break;
        }
        switch (position) {
            case 0:
                mClassIcon.setImageResource(R.drawable.icon_class);
                mClassTv.setTextColor(Color.parseColor("#2399E4"));
                break;
            case 1:
                mFindIcon.setImageResource(R.drawable.icon_find);
                mFindTv.setTextColor(Color.parseColor("#2399E4"));
                break;
            case 2:
                mPersonalIcon.setImageResource(R.drawable.icon_person);
                mPersonalTv.setTextColor(Color.parseColor("#2399E4"));
                break;
        }
        lastIndex = position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 更新未读消息数目的总数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            //            mRedIcon.setVisibility(View.VISIBLE);
        } else {
            //            mRedIcon.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息数目
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
