package com.langbai.tdhd.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;
import com.langbai.tdhd.circle.praisewidget.PraiseWidget;
import com.langbai.tdhd.circle.ui.photo.SimpleObjectPool;
import com.langbai.tdhd.event.ItemListener;
import com.langbai.tdhd.utils.DimenUtil;
import com.langbai.tdhd.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Moushao on 2017/10/23.
 */

public class CircleContentHodler extends VBaseHolder<CircleBaseBean> {

    @BindView(R.id.circle_head) CircleImageView mCircleHead;
    @BindView(R.id.circle_name) TextView mCircleName;
    @BindView(R.id.content) TextView mContent;
    @BindView(R.id.pic_recyc) RecyclerView mContentRecyc;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.delete) TextView mDelete;
    @BindView(R.id.menu) ImageView mMenu;
    @BindView(R.id.pic_vedio) ImageView mVedioPic;
    @BindView(R.id.praise) PraiseWidget mPraise;
    @BindView(R.id.comment_layout) LinearLayout mCommentLayout;
    //    @BindView(R.id.circle_image_container) PhotoContents imageContainer;


    private VBaseAdapter picAdapter;
    private DelegateAdapter delegateAdapter = getDelegeteAdapter();

    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);

    private long userId = UserHelper.getInstance().getLogUser().getUserInfoID();

    public CircleContentHodler(View itemView) {
        super(itemView);
        //        if (imageContainer.getmOnItemClickListener() == null) {
        //            //            imageContainer.setmOnItemClickListener(this);
        //        }
    }


    @Override
    public void setData(int position, CircleBaseBean mData) {
        super.setData(position, mData);
        Glide.with(mContext).load(mData.getAvatar()).apply(options).into(mCircleHead);
        mCircleName.setText(mData.getUserName());
        //mTime.setText(DataUtils.getCircleTime(mData.getSdate()));
        if (mData.getUserID() == userId) {
            mDelete.setVisibility(View.VISIBLE);
        }
        initContentAdapter();
        setContext();
    }

    private void setContext() {
        if (TextUtils.isEmpty(mData.getContent())) {
            mContent.setVisibility(View.GONE);
        } else {
            mContent.setText(mData.getContent());
            mContent.setVisibility(View.VISIBLE);
        }
        //如果视频预览图片不为空,说明发的是视频
        if (!TextUtils.isEmpty(mData.getVideoPicture())) {
            mContentRecyc.setVisibility(View.GONE);
            mVedioPic.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mData.getVideoPicture()).apply(options).into(mVedioPic);
        } else if (mData.getThumbnail().size() != 0) {
            //朋友圈类型是图片,那么,初始化得了个delegateAdater,并且初始化图片列表
            /*mVedioPic.setVisibility(View.GONE);
            mContentRecyc.setVisibility(View.VISIBLE);
            if (picAdapter == null) {
                initPicAdapter();
                picAdapter.setData(mData.getThumbnail());
            } else {
                picAdapter.clear();
                picAdapter.addAllData(mData.getThumbnail());
                picAdapter.setLayoutHelper(getGridLayout());
                picAdapter.notifyDataSetChanged();
            } */

        } else {
            mContentRecyc.setVisibility(View.GONE);
        }

        boolean needPraiseData = addLikes(mData.getLikeList());
        mPraise.setVisibility(needPraiseData ? View.VISIBLE : View.GONE);
        boolean needCommentData = addComment(mData.getCommentList());
        mCommentLayout.setVisibility(needCommentData ? View.VISIBLE : View.GONE);

        //        if (adapter == null) {
        //            adapter = new InnerContainerAdapter(mContext, mData.getThumbnail());
        //            imageContainer.setAdapter(adapter);
        //        } else {
        //            adapter.updateData(mData.getThumbnail());
        //        }
    }

    private void initContentAdapter() {
        if (delegateAdapter == null) {
            delegateAdapter = getDelegeteAdapter();
        }
        initPicAdapter();
        delegateAdapter.addAdapter(picAdapter);
        mContentRecyc.setAdapter(delegateAdapter);
    }

    private void initPicAdapter() {
        if (picAdapter == null) {
            picAdapter = new CircleBaseAdapter(mContext)//
                    .setData(new ArrayList<String>())//
                    .setLayout(R.layout.recyc_circle_pic)//
                    .setLayoutHelper(getGridLayout())//
                    .setHolder(CirclePicHodler.class)//
                    .setListener(new ItemListener() {
                        @Override
                        public void onItemClick(View view, int position, Object mData) {


                        }
                    });
        }

    }

    private DelegateAdapter getDelegeteAdapter() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mContext);
        mContentRecyc.setLayoutManager(virtualLayoutManager);
        //设置缓存view个数(当视图中view的个数很多时，设置合理的缓存大小，防止来回滚动时重新创建 View)
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mContentRecyc.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);
        return new DelegateAdapter(virtualLayoutManager, false);

    }

    private LayoutHelper getGridLayout() {
        //设置Grid布局
        GridLayoutHelper helper = new GridLayoutHelper(3);
        //是否自动扩展
        helper.setAutoExpand(false);
        helper.setMargin(10, 20, 10, 20);
        helper.setBgColor(Color.parseColor("#FFFFFF"));
        //helper.setVGap(20);
        return helper;
    }

    /**
     * 添加点赞
     *
     * @param likesList
     * @return ture=显示点赞，false=不显示点赞
     */
    private boolean addLikes(List<ThumbsUp> likesList) {
        if (isListEmpty(likesList)) {
            return false;
        }
        mPraise.setDatas(likesList);
        return true;
    }

    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    //评论区的view对象池
    private static final SimpleObjectPool<CommentWidget> COMMENT_TEXT_POOL = new SimpleObjectPool<CommentWidget>(35);

    /**
     * 添加评论
     *
     * @param commentList
     * @return ture=显示评论，false=不显示评论
     */
    private boolean addComment(List<CommentBean> commentList) {
        if (isListEmpty(commentList)) {
            return false;
        }
        final int childCount = mCommentLayout.getChildCount();
        if (childCount < commentList.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = COMMENT_TEXT_POOL.get();
                if (commentWidget == null) {
                    commentWidget = new CommentWidget(mContext,i);
                    commentWidget.setPadding((int) DimenUtil.dp2px(8f), (int) DimenUtil.dp2px(3f),
                            (int) DimenUtil.dp2px(8f),
                            (int) DimenUtil.dp2px(3f));
                    commentWidget.setLineSpacing(4, 1);
                }
                commentWidget.setBackgroundDrawable(mContent.getResources().getDrawable(R.drawable.common_selector));
                commentWidget.setOnClickListener(onCommentWidgetClickListener);
                commentWidget.setOnLongClickListener(onCommentLongClickListener);
                mCommentLayout.addView(commentWidget);
            }
        } else if (childCount > commentList.size()) {
            //当前的view的数目比list的长度大，则减去对应的view
            mCommentLayout.removeViews(commentList.size(), childCount - commentList.size());
        }
        //绑定数据
        for (int n = 0; n < commentList.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) mCommentLayout.getChildAt(n);
            if (commentWidget != null)
                commentWidget.setCommentText(commentList.get(n));
        }
        return true;
    }

    private View.OnClickListener onCommentWidgetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof CommentWidget))
                return;
            CommentBean commentInfo = ((CommentWidget) v).getData();
            if (commentInfo == null)
                return;
        }
    };


    private View.OnLongClickListener onCommentLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };

    private View.OnClickListener onMenuButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //            MomentsInfo info = (MomentsInfo) v.getTag(R.id.momentinfo_data_tag_id);
            //            if (info != null) {
            //                commentPopup.updateMomentInfo(info);
            //                commentPopup.showPopupWindow(commentImage);
            //            }
            //        }
        }

    };

    //    private static class InnerContainerAdapter extends PhotoContentsBaseAdapter {
    //
    //
    //        private Context context;
    //        private List<String> datas;
    //        private RequestOptions options = new RequestOptions()
    //                .centerCrop()
    //                .placeholder(R.mipmap.ic_launcher)
    //                .diskCacheStrategy(DiskCacheStrategy.ALL)
    //                .error(R.mipmap.ic_launcher)
    //                .priority(Priority.HIGH);
    //
    //        InnerContainerAdapter(Context context, List<String> datas) {
    //            this.context = context;
    //            this.datas = new ArrayList<>();
    //            this.datas.addAll(datas);
    //        }
    //
    //        @Override
    //        public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
    //            if (convertView == null) {
    //                convertView = new ForceClickImageView(context);
    //                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    //            }
    //            return convertView;
    //        }
    //
    //        @Override
    //        public void onBindData(int position, @NonNull ImageView convertView) {
    //            //            ImageLoadMnanger.INSTANCE.loadImage(convertView, datas.get(position));
    //            Glide.with(context).load(datas.get(position)).apply(options).into(convertView);
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return datas.size();
    //        }
    //
    //        public void updateData(List<String> datas) {
    //            this.datas.clear();
    //            this.datas.addAll(datas);
    //            notifyDataChanged();
    //        }
    //
    //    }
}