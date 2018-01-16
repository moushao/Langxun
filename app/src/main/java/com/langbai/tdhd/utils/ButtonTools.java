package com.langbai.tdhd.utils;

import android.os.Handler;
import android.view.View;

/**
 * 类名: {@link ButtonTools}
 * <br/> 功能描述: 按钮多次点击事件
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/17
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class ButtonTools {
    private static long lastClickTime;

    public static boolean isFastDoubleClick(int waitTime) {

        long time = System.currentTimeMillis();
        if (time - lastClickTime >= waitTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void disabledView(final View v, int delay) {
        if (v.isClickable()) {
            v.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setClickable(true);
                }
            }, delay * 1000);
        }
    }
}