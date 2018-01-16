package com.langbai.tdhd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.adapter.VBaseAdapter;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.circle.commentwidget.CommentBox;
import com.langbai.tdhd.circle.commentwidget.KeyboardControlMnanager;
import com.langbai.tdhd.circle.pullrecyclerview.CircleRecyclerView;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.SquarePresenter;
import com.langbai.tdhd.mvp.view.SquareView;
import com.langbai.tdhd.square.adapter.CommentHolder;
import com.langbai.tdhd.square.adapter.SquareLikeHolder;
import com.langbai.tdhd.utils.TabUtils;
import com.langbai.tdhd.utils.TimeUtil;
import com.langbai.tdhd.widget.CircleImageView;
import com.langbai.tdhd.widget.Dialog.IOSDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class SquareDetailsActivity extends BaseActivity implements CircleRecyclerView.OnPreDispatchTouchListener,
        SquareView {
    public static final String TAG = "SquareDetailsActivity";
    public static final int REQUEST_CODE = 990;
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.recyc) RecyclerView mRecyc;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.content) TextView mContent;
    @BindView(R.id.banner) Banner mBanner;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.player) JZVideoPlayerStandard mPlayer;
    @BindView(R.id.head_detail) CircleImageView mHead;
    @BindView(R.id.name) TextView mName;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.comment) TextView mComment;
    @BindView(R.id.thumb_up) TextView mThumbUp;
    @BindView(R.id.widget_comment) CommentBox commentBox;
    private boolean isChage = false;
    private Context mContext;
    private int viewType;
    private SquareBaseBean bean;
    private List<LocalMedia> medias = new ArrayList<>();
    private DelegateAdapter commentAdatper, likeAdapter;
    private VBaseAdapter VCadatper, VLadapter;
    private ArrayList<SquareCommentBean> commentList = new ArrayList<>();
    private ArrayList<SquareLikeBean> likeList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_square_details;
    }


    @Override
    public BasePresenter getPresenter() {
        return new SquarePresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initData();
        initWidget();
        setBaseData();
    }

    private void initData() {
        viewType = getIntent().getExtras().getInt("VIEW_TYPE");
        bean = (SquareBaseBean) getIntent().getExtras().getSerializable("SQUARE_BEAN");
    }

    private void initWidget() {

        switch (viewType) {
            case 0://只有文字
                mPlayer.setVisibility(View.GONE);
                mBanner.setVisibility(View.GONE);
                break;
            case 1://图片
                mPlayer.setVisibility(View.GONE);
                mBanner.setVisibility(View.VISIBLE);
                initBanner();
                break;
            case 2://视频
                mBanner.setVisibility(View.GONE);
                mPlayer.setVisibility(View.VISIBLE);
                initVideoPlayer();
        }
        initTabUI();
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);
        initKeyboardHeightObserver();
        getVirtuaLayout();
        initCommentAdatper();
        initLikeAdapter();
    }

    @OnClick({R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
        }
    }

    private VirtualLayoutManager getVirtuaLayout() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        virtualLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 20);
        mRecyc.setRecycledViewPool(viewPool);
        return virtualLayoutManager;
    }

    private void initCommentAdatper() {
        if (bean.getCommentList() != null) {
            commentList.addAll(bean.getCommentList());
        }

        commentAdatper = new DelegateAdapter(getVirtuaLayout(), false);

        VCadatper = new VBaseAdapter(mContext)
                .setData(commentList)
                .setLayoutHelper(getLinearLayouut())
                .setLayout(R.layout.recyc_comment_squaer)
                .setHolder(CommentHolder.class)
                .setListener(new ItemListener<SquareCommentBean>() {
                    @Override
                    public void onItemClick(View view, int position, SquareCommentBean mData) {
                        if (mData.getFormID() == UserHelper.getUserId()) {
                            //弹出删除框
                            showMenuDialog(position, mData.getSquareReviewID());
                        } else {
                            //回复别人
                            commentBox.toggleCommentBox(mData.getFormID(), null, false);
                        }
                    }
                });

        commentAdatper.addAdapter(VCadatper);
        mRecyc.setAdapter(commentAdatper);

    }

    private void initLikeAdapter() {
        if (bean.getLikeList() != null) {
            likeList.addAll(bean.getLikeList());
        }
        likeAdapter = new DelegateAdapter(getVirtuaLayout(), false);
        VLadapter = new VBaseAdapter(mContext)
                .setData(likeList)
                .setLayoutHelper(getLinearLayouut())
                .setLayout(R.layout.recyc_like_square)
                .setHolder(SquareLikeHolder.class)
                .setListener(new ItemListener<SquareLikeBean>() {
                    @Override
                    public void onItemClick(View view, int position, SquareLikeBean mData) {

                    }
                });
        likeAdapter.addAdapter(VLadapter);

    }

    public void showMenuDialog(final int pos, final long reviewID) {
        new IOSDialog(mContext).builder()
                .setOtherViewVisiable()
                .setAlbum("删除评论", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        ((SquarePresenter) mPresenter).getDeleteComment(pos, reviewID);
                    }
                }).setCancle("取消", null)
                .show();
    }

    private LayoutHelper getLinearLayouut() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        helper.setMargin(0, 30, 0, 0);
        helper.setPadding(0, 0, 0, 0);
        helper.setDividerHeight(1);
        return helper;
    }

    //设置数据
    private void setBaseData() {

        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("广场详情");
        ImageLoadMnanger.INSTANCE.loadImage(mHead, bean.getAvatar());
        mName.setText(bean.getUserName());
        try {
            mTime.setText(TimeUtil.formatDate(bean.getDateStr(), TimeUtil.DETAILS, TimeUtil.MMDDHHMM_CHINESE_TWO));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mComment.setText("评论 " + getSize(bean.getCommentList()));
        mThumbUp.setText("赞 " + getSize(bean.getLikeList()));
        if (!TextUtils.isEmpty(bean.getContent())) {
            mContent.setText(bean.getContent());
            mContent.setVisibility(View.VISIBLE);
        }
    }

    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox
            .OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, Long statusID, CommentBean commentInfo, String commentContent) {
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
            ((SquarePresenter) mPresenter).addComent(commentContent, statusID, bean.getSquarePublishID());
        }
    };

    private String getSize(List<?> datas) {
        if (isListEmpty(datas))
            return "";
        else
            return datas.size() + "";
    }

    //初始化播放器
    private void initVideoPlayer() {
        mPlayer.setUp(bean.getVideo(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        ImageLoadMnanger.INSTANCE.loadImage(mPlayer.thumbImageView, bean.getVideoPicture());
    }

    //初始化banner
    private void initBanner() {
        for (String url : bean.getThumbnail()) {
            medias.add(new LocalMedia(url, 0, 0, ""));
        }
        //mBanner.isAutoPlay(false);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        mBanner.setImages(bean.getThumbnail());
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageLoadMnanger.INSTANCE.loadImage(imageView, (String) path);
            }

        });

        //设置点击事件监听
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                PictureSelector.create((SquareDetailsActivity) mContext).externalPicturePreview(position, medias);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    //初始化tab
    private void initTabUI() {
        mTabLayout.addTab(mTabLayout.newTab().setText("评论"));
        mTabLayout.addTab(mTabLayout.newTab().setText("赞"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mRecyc.setAdapter(commentAdatper);
                        break;
                    case 1:
                        commentBox.dismissCommentBox(false);
                        mRecyc.setAdapter(likeAdapter);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        commentBox.toggleCommentBox(0, null, false);
                        break;
                }
            }
        });
        TabUtils.setIndicator(mTabLayout, 50, 50);
    }


    public boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void getSquareListSuccess(BaseResponseBean mData) {

    }

    @Override
    public void showMenuDialog(int itemPosition, boolean isMyRelase, SquareBaseBean squareInfo) {

    }

    @Override
    public void deleteReleaseSuccess() {

    }

    @Override
    public void reportSuccess() {

    }

    @Override
    public void likeSquareSuccess(int p, SquareLikeBean mData) {

    }

    @Override
    public void cancleLikeSuccess(int itmePosition, int likePos) {

    }

    @Override
    public void addCommentSuccess(SquareCommentBean mData) {
        mData.setFormAvatar(UserHelper.getInstance().getLogUser().getUserIcon());
        VCadatper.addItem(mData);
        VCadatper.notifyDataSetChanged();
        commentAdatper.notifyDataSetChanged();
        bean.setCommentList((ArrayList<SquareCommentBean>) VCadatper.getDatas());
        mComment.setText("评论 " + getSize(bean.getCommentList()));
        isChage = true;
    }

    @Override
    public void deleteCommentSuccess(int pos) {
        VCadatper.removeItem(pos);
        bean.setCommentList((ArrayList<SquareCommentBean>) VCadatper.getDatas());
        mComment.setText("评论 " + getSize(bean.getCommentList()));
        isChage = true;

    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager
                .OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                if (!isVisible) {
                    commentBox.dismissCommentBox(false);
                }
            }
        });
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0) {
                outRect.top = 15;
            }
        }

    }

    @Override
    public boolean onPreTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        if (isChage) {
            Intent itt = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("SQUARE_BEAN", bean);
            bundle.putInt("POSITION", this.getIntent().getExtras().getInt("POSITION"));
            itt.putExtras(bundle);
            setResult(RESULT_OK, itt);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    /**
     * @param mContext 上下文
     * @param from     从哪儿跳来的
     */
    public static void startAction(Context mContext, SquareBaseBean info, int type, String from) {
        Intent itt = new Intent(mContext, SquareDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        bundle.putSerializable("SQUARE_BEAN", info);
        bundle.putInt("VIEW_TYPE", type);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }

}
