package com.langbai.tdhd.utils;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Moushao on 2017/9/8.
 */

public class RetrofitErrorUtlis {
    public static String getNetErrorMessage(Throwable t) {
        if (t instanceof SocketTimeoutException) {
            return "服务器读取超时,请重试";
        } else if (t instanceof ConnectException) {
            return "链接失败,请重试";
        } else if (t instanceof RuntimeException) {
            return "请求失败,请重试";
        } else if (t instanceof JsonSyntaxException) {
            return "数据解析失败";
        }
        return "";
    }
}
