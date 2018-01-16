package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.RegisterResponseBean;

/**
 * Created by Moushao on 2017/9/25.
 */

public interface AccountView  extends BaseView{
    void registerSuccess(RegisterResponseBean mData);

    void findPassSuccess();

    void LoginSuccess(LoginResponseBean mData);
}
