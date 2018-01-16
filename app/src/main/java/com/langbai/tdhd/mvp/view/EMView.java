package com.langbai.tdhd.mvp.view;

import com.hyphenate.easeui.domain.EaseUser;
import com.langbai.tdhd.bean.LoginResponseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moushao on 2017/9/28.
 */

public interface EMView extends BaseView {
    void getContractListSuccess(List<EaseUser> mData);

    void FindFriendsSuccess(ArrayList<LoginResponseBean> mData);

    void deleteSuccess();
}
