package com.langbai.tdhd.mvp.view;

import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.bean.FriendsSettingBean;

import java.util.List;

/**
 * MVP基础view
 */
public interface BaseView {

    void showLoadProgressDialog(String str);

    void disDialog();

    void showToast(String message);

    void onFailed(String message);

}