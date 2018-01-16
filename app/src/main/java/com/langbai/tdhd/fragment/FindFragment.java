package com.langbai.tdhd.fragment;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.activity.CircleActivity;
import com.langbai.tdhd.activity.SquareActivity;
import com.langbai.tdhd.base.BaseFragment;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Moushao on 2017/9/14.
 */

public class FindFragment extends BaseFragment {
    public static final String TAG = "FindFragment";
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.toolbar_layout) FrameLayout mToolbarLayout;
    @BindView(R.id.circle_of_friends) LinearLayout mCircle;
    @BindView(R.id.square) LinearLayout mSquare;
    Unbinder unbinder;
    private Context mContext;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initInjector() {
        mContext = getActivity();
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();
    }

    @Override
    protected void lazyLoadData() {

    }

    private void initWidget() {
        mTitleTv.setText("发 现");
        mTitleTv.setVisibility(View.VISIBLE);
        mToolbarLayout.setPadding(0, ScreenUtil.getBarHeight(mContext), 0, 0);
    }


    @OnClick({R.id.circle_of_friends, R.id.square})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.circle_of_friends:
                CircleActivity.startAction(mContext, TAG);
                break;
            case R.id.square:
                SquareActivity.startAction(mContext, TAG);
                //                showToast("码农正在开发,稍后开放");
                break;
        }
    }
}
