package com.langbai.tdhd.utils.appupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


import com.langbai.tdhd.R;
import com.langbai.tdhd.activity.MainActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.utils.ToastUtils;
import com.langbai.tdhd.widget.Dialog.ApkUpDialog;

import java.io.File;

/**
 * Created by Mou on 2017/7/26.
 */

/**
 * 使用方法: AppUpdateManager.getInstance(mContext, mData.getUrl(), mData.getCommand()).downloadAPK();
 */

public class AppUpdateManager {
    // handler用到的key
    public static final String KEY_DOWNLOAD_RESULT = "downloadResult";
    //文件名字
    public static final String KEY_FILENAME = "fileName";
    public static final String KEY_PERCENT = "percent";

    // 下载进度
    public static final int MSG_DOWNLOAD_PROGRESS = 0;
    // 下载结果
    public static final int MSG_DOWNLOAD_RESULT = 1;

    // 下载成功
    public static final int FLAG_DOWNLOAD_SUCCESS = 0;
    // 取消下载
    public static final int FLAG_CANCEL_UPDATE = -1;
    // 空间不足
    public static final int FLAG_NO_ENOUGH_SPACE = 1;
    // 下载失败或者写入失败
    public static final int FLAG_DOWNLOAD_ERROR = 2;
    String downloadUrl = "";
    String versionName = "com.langbai.tdhd";
    String command = "";
    private Context mContext;
    private static AppUpdateManager instance;
    // 通知
    private Notification notification = null;
    private NotificationManager notificationManager = null;
    private NotificationCompat.Builder builder = null;
    // 更新服务连接
    private ServiceConnection updateServiceConnection = null;
    // 是否已绑定服务
    private boolean isBinded = false;
    // 定义更新服务
    private AppUpdateService updateService = null;

    public static AppUpdateManager getInstance(Context context, String url, String command) {
        if (instance == null) {
            synchronized (AppUpdateManager.class) {
                instance = new AppUpdateManager(context, url, command);
            }
        }
        return instance;
    }

    public AppUpdateManager(Context context, String url, String command) {
        this.mContext = context;
        this.downloadUrl = url;
        this.command = command;
        builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("下载完成");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        notification = builder.build();
        // 获取通知服务
        notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        initServiceConnection();
    }

    private void initUpDialog() {
        new ApkUpDialog(mContext, command).builder().setPositiveButton("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里启动服务下载apk
                bindUpdateService();
            }
        }).setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消下载
                unBindService();
            }
        }).show();
    }

    /**
     * 初始化ServiceConnection
     */
    private void initServiceConnection() {
        updateServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("ServiceConnection", "service connected");
                AppUpdateService.AppUpdateBinder aBinder = (AppUpdateService.AppUpdateBinder) service;
                updateService = aBinder.getService();
                isBinded = true;
                // 为service设置handler
                updateService.setHandler(handler);
                // TODO: 执行下载任务
                updateService.downloadStart(downloadUrl, versionName);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("ServiceConnection", "service disconnected");
                updateService = null;
                isBinded = false;
            }
        };
    }

    public void downloadAPK() {
        initUpDialog();
    }

    /**
     * 混合调用
     * 先以startService方式启动服务
     * 再以bindService方式绑定服务
     */
    private void bindUpdateService() {
        Intent intent = new Intent(mContext, AppUpdateService.class);
        mContext.startService(intent);
        mContext.bindService(intent, updateServiceConnection, Context.BIND_AUTO_CREATE);
    }

    // 用来显示ui的Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DOWNLOAD_PROGRESS: // 更新进度
                    updateProgress(msg);
                    break;
                case MSG_DOWNLOAD_RESULT: // 下载结果
                    handleDownloadResult(msg);
                    break;
            }
        }
    };

    public void handleDownloadResult(Message msg) {

        // 拿到下载结果
        String fileName = msg.getData().getString(KEY_FILENAME);
        int downloadResult = msg.getData().getInt(KEY_DOWNLOAD_RESULT);

        switch (downloadResult) {
            case FLAG_CANCEL_UPDATE: // 取消下载
                ToastUtils.showToast(mContext, "取消更新");

                showNotification("取消下载", "已取消下载", new Intent(mContext, MainActivity.class));
                break;
            case FLAG_DOWNLOAD_ERROR: // 下载错误
                ToastUtils.showToast(mContext, "下载错误");
                showNotification("更新出错", "更新出错，请稍后再试", new Intent(mContext, MainActivity.class));

                break;
            case FLAG_NO_ENOUGH_SPACE: // 空间不足
                ToastUtils.showToast(mContext, "空间不足");
                showNotification("下载出错，", "存储空间不足", new Intent(mContext, MainActivity.class));

                break;
            case FLAG_DOWNLOAD_SUCCESS: // 下载成功
                showNotification("下载完成", "正在安装", new Intent(mContext, MainActivity.class));
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.setDataAndType(Uri.fromFile(new File(Constants.PHONE_PATH + "APK/com.tdhd.user.apk")),
                        "application/vnd" + ".android.package-archive");
                mContext.startActivity(installIntent);
                break;
        }


        if (updateService != null) {
            unBindService();
        }
    }

    /**
     * 显示通知
     *
     * @param nTitle
     * @param nMessage
     * @param intent
     */
    public void showNotification(String nTitle, String nMessage, Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, AppUpdateService.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        setNotification(nTitle, nTitle, nMessage, contentIntent);
        notificationManager.notify(AppUpdateService.NOTIFICATION_ID, notification);
    }

    /**
     * 更新下载进度
     *
     * @param msg
     */
    private void updateProgress(Message msg) {
        if (updateService != null) {
            Notification sNotification = updateService.getNotification();
            NotificationManager sNotificationManager = updateService.getNotificationManager();
            // 得到下载进度百分比
            int percentage = msg.getData().getInt(AppUpdateManager.KEY_PERCENT);
            if (sNotification != null) {
                RemoteViews remoteViews = sNotification.contentView;
                if (remoteViews != null) {
                    remoteViews.setTextViewText(R.id.notification_update_progress_text, percentage + "%");
                    remoteViews.setProgressBar(R.id.notification_update_progress_bar, 100, percentage, false);
                    sNotificationManager.notify(AppUpdateService.NOTIFICATION_ID, sNotification);
                } else {
                    Log.d("update", "remoteview null");
                }
            }
        }
    }

    /**
     * 取消绑定服务
     */
    private void unBindService() {
        Intent intent = new Intent(mContext, AppUpdateService.class);
        if (isBinded) {
            Log.d("service", "unbind service");
            mContext.unbindService(updateServiceConnection);
            mContext.stopService(intent);
            updateService = null;
            isBinded = false;
        } else {
            Log.d("service", "not binded");
        }
    }

    /**
     * 设置消息通知内容
     *
     * @param ticker
     * @param title
     * @param text
     * @param intent
     */
    private void setNotification(String ticker, String title, String text, PendingIntent intent) {

        if (notification != null) {
            // change notification and repo
            if (builder != null) {
                builder.setTicker(ticker);
                builder.setContentTitle(title);
                builder.setContentText(text);
                builder.setAutoCancel(true);
                builder.setContent(null);
                builder.setContentIntent(intent);
                notification = builder.build();
            }
        }
    }


}
