package com.langbai.tdhd.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.base.BaseActivity;
import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.CircleViewHelper;
import com.langbai.tdhd.circle.commentwidget.CommentBox;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;
import com.langbai.tdhd.circle.commentwidget.KeyboardControlMnanager;
import com.langbai.tdhd.circle.holder.CirccleVideoMomentsVH;
import com.langbai.tdhd.circle.holder.CircleMomentsAdapter;
import com.langbai.tdhd.circle.holder.EmptyMomentsVH;
import com.langbai.tdhd.circle.holder.HostViewHolder;
import com.langbai.tdhd.circle.holder.MultiImageMomentsVH;
import com.langbai.tdhd.circle.holder.TextOnlyMomentsVH;
import com.langbai.tdhd.circle.holder.WebMomentsVH;
import com.langbai.tdhd.circle.pullrecyclerview.CircleRecyclerView;
import com.langbai.tdhd.circle.pullrecyclerview.interfaces.OnRefreshListener2;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.CirclePresenter;
import com.langbai.tdhd.mvp.view.CircleView;
import com.langbai.tdhd.utils.TimeUtil;
import com.langbai.tdhd.utils.VideoPlayer;
import com.langbai.tdhd.utils.permission.CheckPermListener;
import com.langbai.tdhd.widget.Dialog.IOSDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class CircleActivity extends BaseActivity implements CircleRecyclerView.
        OnPreDispatchTouchListener, /*IMomentView,*/ CircleView {
    public static final String TAG = "CircleActivity";
    @BindView(R.id.title_back) ImageView mTitleBack;
    @BindView(R.id.title_tv) TextView mTitleTv;
    @BindView(R.id.image_more) ImageView mImageMore;
    @BindView(R.id.recycler) CircleRecyclerView circleRecyclerView;
    @BindView(R.id.widget_comment) CommentBox commentBox;
    private Context mContext;
    private HostViewHolder hostViewHolder;
    private CircleMomentsAdapter adapter;
    private List<CircleBaseBean> momentsInfoList;

    private CircleViewHelper mViewHelper;
    private boolean isLoadMore;
    private int page;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle;
    }

    @Override
    public BasePresenter getPresenter() {
        return new CirclePresenter();
    }

    @Override
    protected void initInjector() {
        mContext = this;
    }

    @Override
    protected void initEventAndData() {
        onTintStatusBar();
        initWidget();

    }

    private void initWidget() {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(this);
        }
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleTv.setText("朋友圈");
        mImageMore.setImageResource(R.drawable.icon_add);
        mImageMore.setVisibility(View.VISIBLE);
        hostViewHolder = new HostViewHolder(this, new ItemListener() {
            @Override
            public void onItemClick(View view, int position, Object mData) {
                showPictureWallDialog();
            }
        });

        circleRecyclerView.setOnRefreshListener(refreshListener);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());
        hostViewHolder.loadHostData(UserHelper.getInstance().getLogUser());

        commentBox = (CommentBox) findViewById(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        CircleMomentsAdapter.Builder<CircleBaseBean> builder = new CircleMomentsAdapter.Builder<>(this);
        builder.addType(EmptyMomentsVH.class, 0, R.layout.moments_empty_content)
                .addType(TextOnlyMomentsVH.class, 1, R.layout.moments_only_text)
                .addType(MultiImageMomentsVH.class, 2, R.layout.moments_multi_image)
                .addType(CirccleVideoMomentsVH.class, 3, R.layout.moments_video_image)
                .addType(WebMomentsVH.class, 4, R.layout.moments_web)
                .setData(momentsInfoList)
                .setPresenter((CirclePresenter) mPresenter);

        adapter = builder.build();

        circleRecyclerView.setAdapter(adapter);
        circleRecyclerView.autoRefresh();
        initKeyboardHeightObserver();
    }

    @OnClick({R.id.title_back, R.id.image_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.image_more:
                showIOSDialog();
        }
    }


    private OnRefreshListener2 refreshListener = new OnRefreshListener2() {
        @Override
        public void onRefresh() {
            isLoadMore = false;
            ((CirclePresenter) mPresenter).getCircleList(0);
        }

        @Override
        public void onLoadMore() {
            isLoadMore = true;
            if (page < 10) {
                circleRecyclerView.compelete();
                return;
            }
            ((CirclePresenter) mPresenter).getCircleList(page);
        }
    };

    /**
     * 当初选择图片来源的dialog
     */
    private void showPictureWallDialog() {
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new IOSDialog(mContext).builder()
                                        .setTitle("更换相册封面")
                                        .setCareme("照相机", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                gotoTakePhote();
                                            }
                                        })
                                        .setAlbum("相册", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                initPhotoSelector();
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

    /**
     * 当初选择图片来源的dialog
     */
    private void showIOSDialog() {
        new IOSDialog(mContext).builder()
                .setTitle("发布朋友圈")
                .setCareme("文字", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoReleaseCircle(CircleReleaseActivity.TEXT);
                    }
                })
                .setAlbum("图片", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoReleaseCircle(CircleReleaseActivity.PICTURE);
                    }
                })
                .setWord("视频", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoReleaseCircle(CircleReleaseActivity.VIDEO);
                    }
                })
                .setCancle("取消", null)
                .show();
    }

    public void gotoReleaseCircle(int type) {
        Intent itt = new Intent(mContext, CircleReleaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CircleReleaseActivity.SELECTE_TYPE, type);
        itt.putExtras(bundle);
        startActivityForResult(itt, CircleReleaseActivity.REQUEST_CODE);
    }


    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox
            .OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, Long statusID, CommentBean commentInfo, String commentContent) {
            if (TextUtils.isEmpty(commentContent))
                return;
            int itemPos = mViewHelper.getCommentItemDataPosition();
            if (itemPos < 0 || itemPos > adapter.getItemCount())
                return;
            //            List<CommentBean> commentInfos = adapter.findData(itemPos).getCommentList();
            if (commentInfo != null) {
                //回复某个人的评论
                ((CirclePresenter) mPresenter).addComment(true, itemPos, statusID, commentInfo,
                        commentContent);
            } else {
                //评论朋友圈
                ((CirclePresenter) mPresenter).addComment(false, itemPos, statusID, commentInfo,
                        commentContent);
            }
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };


    @Override
    public boolean onPreTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void addLike(int itemPos, Long friendPraiseID) {
        CircleBaseBean bean = adapter.getDatas().get(itemPos);
        ThumbsUp up = new ThumbsUp(TimeUtil.getCurrentTime() + "",
                UserHelper.getInstance().getLogUser().getUserInfoID(),
                UserHelper.getInstance().getLogUser().getRealName(),
                adapter.getDatas().get(itemPos).getStatusID(),
                friendPraiseID,
                UserHelper.getInstance().getLogUser().getType()
        );
        bean.getLikeList().add(up);
        adapter.notifyItemChanged(itemPos);
    }

    @Override
    public void cancleLike(int itemPos) {
        long userID = UserHelper.getInstance().getLogUser().getUserInfoID();
        for (ThumbsUp thumbsUp : adapter.getDatas().get(itemPos).getLikeList()) {
            if (thumbsUp.getLikeID() == userID) {
                adapter.getDatas().get(itemPos).getLikeList().remove(thumbsUp);
            }
        }
        adapter.notifyItemChanged(itemPos);
    }


    @Override
    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, long statusID, CommentWidget
            commentWidget) {
        if (viewHolderRootView != null) {
            mViewHelper.setCommentAnchorView(viewHolderRootView);
        } else if (commentWidget != null) {
            mViewHelper.setCommentAnchorView(commentWidget);
        }
        mViewHelper.setCommentItemDataPosition(itemPos);
        commentBox.toggleCommentBox(statusID, commentWidget == null ? null : commentWidget.getData(), false);
    }


    @Override
    public void addComment(int itemPos, Long commentID, String commentContent, CommentBean commentInfo) {
        CommentBean bean = new CommentBean(
                UserHelper.getUserType(),
                commentInfo == null ? "" : commentInfo.getFormName(),
                UserHelper.getUserRealName(),
                commentInfo == null ? 0 : commentInfo.getFormID(),
                commentID,
                0,
                0,
                commentContent,
                TimeUtil.getCurrentTime() + "",
                UserHelper.getUserId(),
                adapter.getDatas().get(itemPos).getStatusID()
        );
        adapter.getDatas().get(itemPos).getCommentList().add(bean);
        adapter.notifyItemChanged(itemPos);

    }

    @Override
    public void deleteComment(int itemPos, int commentPosition) {
        adapter.getDatas().get(itemPos).getCommentList().remove(commentPosition);
        adapter.notifyItemChanged(itemPos);
    }

    @Override
    public void deleteReleaseSuccess(int itemPosition) {
        adapter.getDatas().remove(itemPosition);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void changeWappPictureSuccess(LoginResponseBean mData) {
        hostViewHolder.loadHostData(mData);
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void onFailed(String message) {
        circleRecyclerView.compelete();
    }

    @Override
    public void getMyCircleListSuccess(BaseResponseBean mData) {
        circleRecyclerView.compelete();
        page = mData.getPage();
        if (isLoadMore) {
            adapter.addMore((ArrayList<CircleBaseBean>) mData.getData());
        } else {
            adapter.updateData((ArrayList<CircleBaseBean>) mData.getData());
        }
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager
                .OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = commentBox.getCommentType();
                if (isVisible) {
                    //定位评论框到view
                    anchorView = mViewHelper.alignCommentBoxToView(circleRecyclerView, commentBox, commentType);
                } else {
                    //定位到底部
                    commentBox.dismissCommentBox(false);
                    mViewHelper.alignCommentBoxToViewWhenDismiss(circleRecyclerView, commentBox, commentType,
                            anchorView);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CircleReleaseActivity.REQUEST_CODE:
                    refreshListener.onRefresh();
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                case PictureConfig.REQUEST_CAMERA:
                    // 图片选择结果回调
                    final List<LocalMedia> selectMedia = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Map<String, RequestBody> params = new HashMap<>();
                    File file = new File(selectMedia.get(0).getCompressPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/" + selectMedia.get
                            (0).getPictureType().replace("image/", "")), file);
                    String name = UserHelper.getUserId() + TimeUtil.getCurrentTime() + "";
                    params.put("file\"; name =" + name + ";filename=" + name + "\"", requestBody);

                    params.put("userInfoID", RequestBody.create(MediaType.parse("text/plain"), UserHelper.getUserId() +
                            ""));
                    ((CirclePresenter) mPresenter).changeWallPicture(mContext, params);
                    break;

            }
        }

    }

    private void initPhotoSelector() {
        PictureSelector.create(CircleActivity.this)//
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll
                // ()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
    }

    private void gotoTakePhote() {
        PictureSelector.create(CircleActivity.this).openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
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
        Intent itt = new Intent(mContext, CircleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, from);
        itt.putExtras(bundle);
        mContext.startActivity(itt);
    }
}
