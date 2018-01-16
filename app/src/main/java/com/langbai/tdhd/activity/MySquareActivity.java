package com.langbai.tdhd.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.SquarePresenter;
import com.langbai.tdhd.mvp.view.SquareView;
import com.langbai.tdhd.square.adapter.SquareAdapter;
import com.langbai.tdhd.square.adapter.SquarePictureVH;
import com.langbai.tdhd.square.adapter.SquareTextVH;
import com.langbai.tdhd.square.adapter.SquareVideoVH;
import com.langbai.tdhd.utils.permission.CheckPermListener;
import com.langbai.tdhd.widget.Dialog.IOSDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

public class MySquareActivity extends BaseActivity implements SquareView {
    public static final String TAG = "MySquareActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.image_more) ImageView mImageMore;
    @BindView(R.id.square) RecyclerView mSquare;
    @BindView(R.id.refresh) SmartRefreshLayout mRefreshLayout;
    private Context mContext;
    private boolean isLoadMore;
    private SquareAdapter adapter;
    private int page;
    private List<SquareBaseBean> momentsInfoList;
    private int position;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_square;
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
        initWidget();
        ((SquarePresenter) mPresenter).getMyOwnSquareList(0);
    }

    private void initWidget() {
        onTintStatusBar();
        mTitleBack.setVisibility(View.VISIBLE);
        mImageMore.setImageResource(R.drawable.icon_add);
        mImageMore.setVisibility(View.VISIBLE);
        mTitleTv.setText("广场");
        mImageMore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                gotoReleaseCircle(SquareReleaseActivity.TEXT);
                return true;
            }
        });
        initRefresh();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mSquare.addItemDecoration(new SpaceItemDecoration(10));
        mSquare.setLayoutManager(manager);
        SquareAdapter.Builder<SquareBaseBean> builder = new SquareAdapter.Builder<>(this);
        builder.addType(SquareTextVH.class, 0, R.layout.square_empty_content)
                .addType(SquarePictureVH.class, 1, R.layout.square_multi_image)
                .addType(SquareVideoVH.class, 2, R.layout.square_video_image)
                .setData(momentsInfoList)
                .setPresenter((SquarePresenter) mPresenter);

        adapter = builder.build();
        mSquare.setAdapter(adapter);
    }


    @OnClick({R.id.image_more, R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.image_more:
                showIOSDialog();
                break;
        }

    }


    private void initRefresh() {
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                if (page < 10) {
                    mRefreshLayout.finishLoadmore(100);
                    return;
                }
                ((SquarePresenter) mPresenter).getMyOwnSquareList(page);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                ((SquarePresenter) mPresenter).getMyOwnSquareList(0);
            }
        });
    }


    /**
     * 当初选择图片来源的dialog
     */
    private void showIOSDialog() {
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new IOSDialog(mContext).builder()
                                        .setTitle("广场发布")
                                        .setCareme("视频", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                gotoReleaseCircle(SquareReleaseActivity.VIDEO);
                                            }
                                        })
                                        .setAlbum("图片", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                gotoReleaseCircle(SquareReleaseActivity.PICTURE);
                                            }
                                        })
                                        .setCancle("取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        }).show();
                            }
                        }, R.string.need_permision, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO);

    }

    @Override
    public void showMenuDialog(int itemPosition, final boolean isMyRelase, final SquareBaseBean squareInfo) {
        position = itemPosition;
        new IOSDialog(mContext).builder()
                .setOtherViewVisiable()
                .setAlbum(isMyRelase ? "删除" : "举报", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isMyRelase) {
                            //删除
                            ((SquarePresenter) mPresenter).getDeleteRelase(squareInfo.getSquarePublishID());
                        } else {
                            //举报
                            ReportTypeActivity.startAction(mContext, TAG);
                        }
                    }
                }).setCancle("取消", null)
                .show();
    }

    public void gotoReleaseCircle(int type) {
        Intent itt = new Intent(mContext, SquareReleaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SquareReleaseActivity.SELECTE_TYPE, type);
        itt.putExtras(bundle);
        startActivityForResult(itt, SquareReleaseActivity.REQUEST_CODE);
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
    }

    @Override
    public void disDialog() {
        disLoadDialog();
    }

    @Override
    public void onFailed(String message) {
        mRefreshLayout.finishLoadmore(100);
        mRefreshLayout.finishRefresh(100);
    }

    @Override
    public void getSquareListSuccess(BaseResponseBean mData) {
        mRefreshLayout.finishLoadmore(100);
        mRefreshLayout.finishRefresh(100);
        page = mData.getPage();
        if (isLoadMore) {
            adapter.addMore((ArrayList<SquareBaseBean>) mData.getData());
        } else {
            adapter.updateData((ArrayList<SquareBaseBean>) mData.getData());
        }
    }

    @Override
    public void deleteReleaseSuccess() {
        adapter.getDatas().remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void reportSuccess() {
        showToast("举报成功");
    }

    @Override
    public void likeSquareSuccess(int position, SquareLikeBean mData) {
        adapter.getDatas().get(position).getLikeList().add(mData);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void cancleLikeSuccess(int itmePosition, int likePos) {
        adapter.getDatas().get(itmePosition).getLikeList().remove(likePos);
        adapter.notifyItemChanged(itmePosition);
    }

    @Override
    public void addCommentSuccess(SquareCommentBean mData) {

    }

    @Override
    public void deleteCommentSuccess(int pos) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SquareReleaseActivity.REQUEST_CODE:
                    ((SquarePresenter) mPresenter).getMyOwnSquareList(0);
                    break;
                case ReportTypeActivity.REQUEST_CODE:
                    ((SquarePresenter) mPresenter).getReportContent(adapter.getDatas().get(position)
                            .getSquarePublishID(), data.getIntExtra(ReportTypeActivity.REQUEST_RESULT, 0));
                    break;
                case SquareDetailsActivity.REQUEST_CODE:
                    SquareBaseBean bean = (SquareBaseBean) data.getExtras().getSerializable("SQUARE_BEAN");
                    int pos = data.getExtras().getInt("POSITION");
                    adapter.getDatas().set(pos, bean);
                    adapter.notifyItemChanged(pos);
                    break;

            }
        }

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
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
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
    public static void startAction(Context mContext, String from) {
        Intent itt = new Intent(mContext, MySquareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
