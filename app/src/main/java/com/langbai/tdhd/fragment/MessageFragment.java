package com.langbai.tdhd.fragment;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseFragment;
import com.langbai.tdhd.mvp.presenter.BasePresenter;

/**
 * Created by Moushao on 2017/9/14.
 */

public class MessageFragment extends BaseFragment {
    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void lazyLoadData() {

    }
}
