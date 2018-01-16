package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.FriendsSettingBean;

/**
 * Created by Moushao on 2017/11/23.
 */

public interface FriendsSettingView extends BaseView {
    void getFriendsStatusSuccess(FriendsSettingBean mData);

    void deleteSuccess();
}
