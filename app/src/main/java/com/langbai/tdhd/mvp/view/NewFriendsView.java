package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.ApplyResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/9.
 */

public interface NewFriendsView extends BaseView {
    void getGroomList(ArrayList<LoginResponseBean> mData);

    void getApplyList(ArrayList<ApplyResponseBean> mData);

    void getAgreeApplyInfo(LoginResponseBean mData);
}
