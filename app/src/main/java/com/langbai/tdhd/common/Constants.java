package com.langbai.tdhd.common;


import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * 类名: {@link Constants}
 * <br/> 功能描述: 存储全局数据的基本类
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/19
 */
public class Constants {
    public static String CURRENT_PRICE = "";
    /**
     * 是否为第一次登录
     */
    public static boolean IS_FIRST_LOAD = false;
    /**
     * 是否重新登录
     */
    public static boolean IS_RELOGIN = true;
    /**
     * 登录账户
     */
    public static String ACCOUNT = "";
    /**
     * TOKEN
     */
    public static String TOKEN = "";
    /**
     * 从哪儿来
     */
    public static String FROM = "";
    /**
     * inflater 实例
     */
    private static LayoutInflater inflater;
    public static String PHONE = "";
    /**
     * view 实例 这个方法有毒，会爆内存溢出
     *
     * @param context
     * @param parent
     * @param res
     * @return
     */
    public static View inaflate(Context context, ViewGroup parent, int res) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater.inflate(res, parent, false);
    }


    /**
     * 手机存储路径
     * Environment.getExternalStorageDirectory();
     */
    public final static String PHONE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "LangXun" + File.separator;

    /**
     * 基础连接
     */
//        public static String BASE_URL = "http://10.100.50.144:8080/";
    public static String BASE_URL = "http://47.94.40.230:8080/";

    /**
     * DiskCache
     */
    public static final long CACHE_MAXSIZE = 10 * 1024 * 1024;
    public static final String USER_INFO = "USER_INFO";

    public final static String REGISTER_USER = "REGISTER_USER";
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String SYSTEM_SETTING = "SYSTEM_SETTING";
}
