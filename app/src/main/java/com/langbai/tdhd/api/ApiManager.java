package com.langbai.tdhd.api;


import android.support.annotation.NonNull;


import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.convert.CustomGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by PandaQ on 2016/9/8.
 * email : 767807368@qq.com
 * 集中处理Api相关配置的Manager类
 */
public class ApiManager {
    private static ApiManager sApiManager;
    private static OkHttpClient mClient;
    private CodeApi mCodeApi;
    private RegisterApi mRegisterApi;
    private FindPassApi mFindPassApi;
    private LoginApi mLoginApi;
    private EMApi mEMApi;
    private GroupApi mGroupApi;
    private MeetingApi mMettingApi;
    private CircleApi mCircleApi;
    private CircleReleaseApi mReleaseApi;
    private SquareApi mSquareApi;
    private SquareReleaseApi mSquareReleaseApi;
    private FriendsSettingApi mFriendsSettingApi;

    private ApiManager() {

    }

    public static ApiManager getInstance() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        mClient = new OkHttpClient.Builder().addInterceptor(new CustomInterceptor()).connectTimeout(30, TimeUnit
                .SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        return sApiManager;
    }


    @NonNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()//
                .client(mClient).baseUrl(Constants.BASE_URL)//
                .addConverterFactory(CustomGsonConverterFactory.create())//
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
                .build();
    }

    /**
     * 获取验证码
     */
    public CodeApi getCodeApi() {
        if (mCodeApi == null) {
            mCodeApi = getRetrofit().create(CodeApi.class);
        }
        return mCodeApi;
    }

    /**
     * 注册
     */
    public RegisterApi getRegisterApi() {
        if (mRegisterApi == null) {
            mRegisterApi = getRetrofit().create(RegisterApi.class);
        }
        return mRegisterApi;
    }

    /**
     * 找回密码
     */
    public FindPassApi getFindPassApi() {
        if (mFindPassApi == null) {
            mFindPassApi = getRetrofit().create(FindPassApi.class);
        }
        return mFindPassApi;
    }

    public LoginApi LoginApi() {
        if (mLoginApi == null) {
            mLoginApi = getRetrofit().create(LoginApi.class);
        }
        return mLoginApi;
    }

    /**
     * 个人中心修改资料
     */
    public InfoChangeApi getInfoChangeApi() {
        return getRetrofit().create(InfoChangeApi.class);
    }

    /**
     * 获取好友列表
     */
    public EMApi getFriendsApi() {
        if (mEMApi == null) {
            mEMApi = getRetrofit().create(EMApi.class);
        }
        return mEMApi;
    }

    /**
     * 新添加好友等相关api
     */
    public NewFriendsApi getNewFriendsApi() {
        return getRetrofit().create(NewFriendsApi.class);
    }

    /**
     * 群组相关api
     */
    public GroupApi getGroupApi() {
        if (mGroupApi == null) {
            mGroupApi = getRetrofit().create(GroupApi.class);
        }
        return mGroupApi;
    }

    /**
     * 会议相关
     */
    public MeetingApi getMeetingApi() {
        if (mMettingApi == null) {
            mMettingApi = getRetrofit().create(MeetingApi.class);
        }
        return mMettingApi;
    }

    /**
     * 朋友圈相关
     */
    public CircleApi getCircleApi() {
        if (mCircleApi == null) {
            mCircleApi = getRetrofit().create(CircleApi.class);
        }
        return mCircleApi;
    }

    /**
     * 版本更新
     */
    public UPApi getUPApi() {
        return getRetrofit().create(UPApi.class);
    }

    /**
     * 朋友圈发布
     */
    public CircleReleaseApi getCircleReleaseApi() {
        if (mReleaseApi == null) {
            mReleaseApi = getRetrofit().create(CircleReleaseApi.class);
        }
        return mReleaseApi;
    }

    /**
     * 广场相关接口
     */
    public SquareApi getSquareApi() {
        if (mSquareApi == null) {
            mSquareApi = getRetrofit().create(SquareApi.class);
        }
        return mSquareApi;
    }

    /**
     * 广场发布
     */
    public SquareReleaseApi getSquareReleaseApi() {
        if (mSquareReleaseApi == null) {
            mSquareReleaseApi = getRetrofit().create(SquareReleaseApi.class);
        }
        return mSquareReleaseApi;
    }

    public FriendsSettingApi getFriendsSettingApi() {
        if (mFriendsSettingApi == null) {
            mFriendsSettingApi = getRetrofit().create(FriendsSettingApi.class);
        }
        return mFriendsSettingApi;
    }
}
