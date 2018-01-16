package com.langbai.tdhd.common;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.langbai.tdhd.DemoHelper;
import com.langbai.tdhd.utils.CrashHandler;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.SharedPreferencesHelper;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 类名: MyApplication
 * <br/> 功能描述:
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/6/21
 */

public class MyApplication extends MultiDexApplication {
    /**
     * 系统通知栏提醒
     */
    public static NotificationManager notifyManager = null;
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;
        LeakCanary.install(this);
        isFirstLoad();
        initHotfix();
        initBugly();
        //CrashHandler.getInstance().init(mMyApplication);
        DemoHelper.getInstance().init(this);
        initIM();
        initSmartRefresh();
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0.0";
        }
        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int
                            handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                        LogUtil.e("hotfix", code + ":" + info);
                    }

                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private void initBugly() {
        Context context = getApplicationContext();
        //获取当前进程名
        String processName = getAppName(android.os.Process.myPid());
        //设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(this.getPackageName()));
               /* 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
                输出详细的Bugly SDK的Log；
                每一条Crash都会被立即上报；
                自定义日志将会在Logcat中输出。
                建议在测试阶段建议设置成true，发布时设置为false*/
        CrashReport.initCrashReport(getApplicationContext(), "05b0ae24f4", true);
    }

    //初始化环信
    private void initIM() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(this, options);
        //是否自动登录
        options.setAutoLogin(true);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);
        EaseUI.getInstance().init(this, options);
    }

    private void isFirstLoad() {
        //判断是否为第一次登录,如果是,则进入资料引导页,否则直接进入主界面
        Constants.IS_FIRST_LOAD = SharedPreferencesHelper.getBoolean(this, "FIRST", true);
        Constants.IS_RELOGIN = SharedPreferencesHelper.getBoolean(this, "IS_RELOGIN", true);
        SharedPreferencesHelper.putBoolean(getApplicationContext(), "FIRST", false);
    }

    private void initSmartRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (cacheDir != null && !cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }

    @Override
    public void onTerminate() {
        if (this.notifyManager != null) {
            notifyManager.cancelAll();
        }
        super.onTerminate();
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode() {
        Context context = mMyApplication.getApplicationContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


