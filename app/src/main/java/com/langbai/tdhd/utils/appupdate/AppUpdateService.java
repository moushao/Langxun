package com.langbai.tdhd.utils.appupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;


import com.langbai.tdhd.R;
import com.langbai.tdhd.activity.MainActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.utils.LogUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.InputStream;

import okhttp3.Call;

/**
 * Created by Mou on 2017/7/26.
 */

public class AppUpdateService extends Service {
    private final AppUpdateBinder aBinder = new AppUpdateBinder();
    // 消息通知对象
    private Notification notification = null;
    // 消息通知管理对象
    private NotificationManager notificationManager = null;
    // UI线程处理句柄
    private Handler handler;
    // 消息通知id
    public static final int NOTIFICATION_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initNotification();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return aBinder;
    }

    public class AppUpdateBinder extends Binder {
        public AppUpdateService getService() {
            return AppUpdateService.this;
        }
    }


    /**
     * 初始化消息通知
     */
    public void initNotification() {
        long when = System.currentTimeMillis();

        // V7包下的NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // Ticker是状态栏显示的提示
        builder.setTicker("应用更新");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setWhen(when);
        builder.setAutoCancel(true);
        // 自定义contentView
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_update);
        contentView.setTextViewText(R.id.notification_update_progress_text, "0%");
        contentView.setImageViewResource(R.id.notification_update_image, R.mipmap.ic_launcher);
        // 为notification设置contentView
        builder.setContent(contentView);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // 构建一个notification
        notification = builder.build();
    }

    /**
     * 开一个线程来下载apk文件
     *
     * @param url     apk地址
     * @param version 版本
     */
    public void downloadStart(String url, String version) {
        DownloadRunnable downloadRunnable = new DownloadRunnable(url, version);
        Thread thread = new Thread(downloadRunnable);
        thread.start();
    }

    class DownloadRunnable implements Runnable {
        private String url;
        private String version;
        private InputStream input = null;
        private int flag = 0;
        private String fileName;

        public DownloadRunnable(String url, String version) {
            this.url = url;
            this.version = version;
        }

        private int downloadPercentage = 0;

        @Override
        public void run() {
            //开始下载
            File file = new File(Constants.PHONE_PATH + "APK/com.tdhd.user.apk");
            if (file.exists())
                file.delete();
            //FileDownlodder的更多教程:https://github.com/lingochamp/FileDownloader/blob/master/README-zh.md
            FileDownloader.getImpl().create(url)
                    .setPath(Constants.PHONE_PATH + "APK/com.tdhd.user.apk")
                    .setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            //进入队列挂起成功
                            LogUtil.e("下载", "pending");
                        }

                        @Override
                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int
                                soFarBytes, int totalBytes) {
                            //链接下载地址成功
                            LogUtil.e("下载", "connected");

                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            //下载进度
                            int tempPercent = downloadPercentage;
                            downloadPercentage = (int) (soFarBytes * 100.0F / totalBytes);
                            if (downloadPercentage != tempPercent) {
                                Message msg = new Message();
                                msg.what = AppUpdateManager.MSG_DOWNLOAD_PROGRESS;
                                Bundle bundle = new Bundle();
                                bundle.putInt(AppUpdateManager.KEY_PERCENT, downloadPercentage);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }

                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {
                            //灰度下载完成
                            LogUtil.e("下载", "blockComplete");

                        }

                        @Override
                        protected void retry(final BaseDownloadTask task, final Throwable ex, final int
                                retryingTimes, final int soFarBytes) {
                            //
                            LogUtil.e("下载", "retry");

                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            //完成
                            LogUtil.e("下载", "completed");
                            flag = AppUpdateManager.FLAG_DOWNLOAD_SUCCESS;
                            sendDownloadResult(flag, fileName);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            //暂停下载
                            LogUtil.e("下载", "paused");

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            LogUtil.e("下载", "error");

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                            //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
                            LogUtil.e("下载", "warn");

                        }
                    }).start();


        }
    }


    /**
     * 通过handle通知消息
     *
     * @param flag
     * @param fileName
     */
    private void sendDownloadResult(int flag, String fileName) {
        Message msg = new Message();
        msg.what = AppUpdateManager.MSG_DOWNLOAD_RESULT;
        Bundle bundle = new Bundle();
        bundle.putInt(AppUpdateManager.KEY_DOWNLOAD_RESULT, flag);
        bundle.putString(AppUpdateManager.KEY_FILENAME, fileName);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * 服务被启动时回调此方法
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
        // 随后系统会尝试重新创建service，由于服务状态为开始状态，
        // 所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
        // 如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
        return START_STICKY;
    }


    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    /**
     * 设置handler
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Notification getNotification() {
        return notification;
    }
}
