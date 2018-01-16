package com.langbai.tdhd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.widget.CustomViewPager;
import com.luck.picture.lib.tools.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 类名: {@link NavigationActivity}
 * <br/> 功能描述:首次启动的导航界面
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/27
 */
public class NavigationActivity extends BaseActivity {
    public static final String TAG = "NavigationActivity";
    @BindView(R.id.pager) CustomViewPager mPager;
    private List<View> views;

    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        SetTranslanteBar();
        initWidget();
    }

    private void initWidget() {
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);

        views.add(inflater.inflate(R.layout.pager_navigation_one, null));
        views.add(inflater.inflate(R.layout.pager_navigation_two, null));
        views.add(inflater.inflate(R.layout.pager_navigation_three, null));
        PagerAdapter adapter = new PagerAdapter(views);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(3);
    }

    private class PagerAdapter extends android.support.v4.view.PagerAdapter {
        private List<View> views;

        public PagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }


        @Override
        public int getCount() {
            return (views == null) ? 0 : views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            if (position == views.size() - 1) {
                TextView tvEnter = (TextView) container.findViewById(R.id.start);
                tvEnter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //在此记录曾经浏览过导航页，下次启动将不会进入导航页
                        LoginActivity.startAction(mContext, TAG);
                        finish();
                    }
                });
            }
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, NavigationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

}
