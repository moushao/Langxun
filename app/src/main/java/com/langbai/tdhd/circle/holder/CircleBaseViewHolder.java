package com.langbai.tdhd.circle.holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.langbai.tdhd.Constant;
import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.CircleActivity;
import com.langbai.tdhd.activity.FriendsInfoActivity;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.comment.ClickShowMoreLayout;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;
import com.langbai.tdhd.circle.popup.CommentPopup;
import com.langbai.tdhd.circle.popup.DeleteCommentPopup;
import com.langbai.tdhd.circle.praisewidget.PraiseWidget;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.circle.ui.photo.SimpleObjectPool;
import com.langbai.tdhd.common.MyApplication;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.CirclePresenter;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.TimeUtil;
import com.langbai.tdhd.utils.ToastUtils;
import com.langbai.tdhd.utils.UIHelper;

import java.util.List;


import static com.langbai.tdhd.adapter.CircleContentHodler.isListEmpty;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈基本item
 */
public abstract class CircleBaseViewHolder extends BaseRecyclerViewHolder<CircleBaseBean> implements
        BaseMomentVH<CircleBaseBean>, ViewGroup.OnHierarchyChangeListener {
    protected Context mContext;

    //头部
    protected ImageView avatar;
    protected TextView nick;
    protected ClickShowMoreLayout userText;

    //底部
    protected TextView createTime;
    protected TextView deleteRelease;
    protected ImageView commentImage;
    protected FrameLayout menuButton;
    protected LinearLayout commentAndPraiseLayout;
    protected PraiseWidget praiseWidget;
    protected View line;
    protected LinearLayout commentLayout;

    //内容区
    protected RelativeLayout contentLayout;
    private long userID = UserHelper.getUserId();

    //评论区的view对象池
    private static final SimpleObjectPool<CommentWidget> COMMENT_TEXT_POOL = new SimpleObjectPool<CommentWidget>(35);

    private CommentPopup commentPopup;
    private DeleteCommentPopup deleteCommentPopup;

    private CirclePresenter momentPresenter;
    private int itemPosition; //当条朋友圈的位置
    private CircleBaseBean momentsInfo;


    public CircleBaseViewHolder(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
        mContext = context;
        onFindView(itemView);

        //header
        avatar = (ImageView) findView(avatar, R.id.avatar);
        nick = (TextView) findView(nick, R.id.nick);
        userText = (ClickShowMoreLayout) findView(userText, R.id.item_text_field);

        //bottom
        createTime = (TextView) findView(createTime, R.id.create_time);
        deleteRelease = (TextView) itemView.findViewById(R.id.delete_release);

        // deleteRelease = (TextView) findView(deleteRelease, R.id.delete_release);
        commentImage = (ImageView) findView(commentImage, R.id.menu_img);
        menuButton = (FrameLayout) findView(menuButton, R.id.menu_button);
        commentAndPraiseLayout = (LinearLayout) findView(commentAndPraiseLayout, R.id.comment_praise_layout);
        praiseWidget = (PraiseWidget) findView(praiseWidget, R.id.praise);
        line = findView(line, R.id.divider);
        commentLayout = (LinearLayout) findView(commentLayout, R.id.comment_layout);
        //content
        contentLayout = (RelativeLayout) findView(contentLayout, R.id.content);

        if (commentPopup == null) {
            commentPopup = new CommentPopup((Activity) getContext());
            commentPopup.setOnCommentPopupClickListener(onCommentPopupClickListener);
        }

        if (deleteCommentPopup == null) {
            deleteCommentPopup = new DeleteCommentPopup((Activity) getContext());
            deleteCommentPopup.setOnDeleteCommentClickListener(onDeleteCommentClickListener);
        }
        deleteRelease.setText("删除");


    }

    public void setPresenter(CirclePresenter momentPresenter) {
        this.momentPresenter = momentPresenter;
    }

    public BasePresenter getPresenter() {
        return momentPresenter;
    }

    @Override
    public void onBindData(CircleBaseBean data, int position) {
        if (data == null) {
            LogUtil.e("数据是空的！！！！");
            findView(userText, R.id.item_text_field);
            userText.setText("这个动态的数据是空的。。。。OMG");
            return;
        }
        this.momentsInfo = data;
        this.itemPosition = position;
        //通用数据绑定
        onBindMutualDataToViews(data);
        //点击事件
        menuButton.setOnClickListener(onMenuButtonClickListener);
        menuButton.setTag(R.id.momentinfo_data_tag_id, data);
        if (userID == momentsInfo.getUserID()) {
            deleteRelease.setOnClickListener(deleteClick);
            deleteRelease.setVisibility(View.VISIBLE);
        } else {
            deleteRelease.setVisibility(View.INVISIBLE);
        }
        //传递到子类
        onBindDataToView(data, position, getViewType());
    }

    private void onBindMutualDataToViews(CircleBaseBean data) {
        //header
        ImageLoadMnanger.INSTANCE.loadImage(avatar, data.getAvatar());

        nick.setText(UserHelper.getNickNAme(data.getUserName(), data.getType()));
        if (TextUtils.isEmpty(data.getContent())) {
            userText.setVisibility(View.GONE);
        } else {
            userText.setText(data.getContent());
        }
        //bottom
        createTime.setText(TimeUtil.getTimeString(Long.valueOf(data.getSdate())));
        boolean needPraiseData = addLikes(data.getLikeList());
        boolean needCommentData = addComment(data.getCommentList());
        praiseWidget.setVisibility(needPraiseData ? View.VISIBLE : View.GONE);
        commentLayout.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
        line.setVisibility(needPraiseData && needCommentData ? View.VISIBLE : View.GONE);
        commentAndPraiseLayout.setVisibility(needCommentData || needPraiseData ? View.VISIBLE : View.GONE);
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
        praiseWidget.setDatas(likesList);
        return true;
    }


    private int commentLeftAndPaddintRight = UIHelper.dipToPx(8f);
    private int commentTopAndPaddintBottom = UIHelper.dipToPx(3f);

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
        final int childCount = commentLayout.getChildCount();
        if (childCount < commentList.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = COMMENT_TEXT_POOL.get();
                if (commentWidget == null) {
                    commentWidget = new CommentWidget(getContext(), i);
                    commentWidget.setPadding(commentLeftAndPaddintRight, commentTopAndPaddintBottom,
                            commentLeftAndPaddintRight, commentTopAndPaddintBottom);
                    commentWidget.setLineSpacing(4, 1);
                }
                commentWidget.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable
                        .common_selector));
                commentWidget.setOnClickListener(onCommentWidgetClickListener);
                commentWidget.setOnLongClickListener(onCommentLongClickListener);
                commentLayout.addView(commentWidget);
            }
        } else if (childCount > commentList.size()) {
            //当前的view的数目比list的长度大，则减去对应的view
            commentLayout.removeViews(commentList.size(), childCount - commentList.size());
        }
        //绑定数据
        for (int n = 0; n < commentList.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) commentLayout.getChildAt(n);
            if (commentWidget != null)
                commentWidget.setCommentText(commentList.get(n));
        }
        return true;
    }


    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof CommentWidget)
            COMMENT_TEXT_POOL.put((CommentWidget) child);
    }

    public void clearCommentPool() {
        COMMENT_TEXT_POOL.clearPool();
    }

    /**
     * ==================  click listener block
     */

    private View.OnClickListener onCommentWidgetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof CommentWidget))
                return;
            CommentBean commentInfo = ((CommentWidget) v).getData();
            if (commentInfo == null)
                return;

            if (commentInfo.getFormID() == UserHelper.getInstance().getLogUser().getUserInfoID()) {
                deleteCommentPopup.showPopupWindow(commentInfo, ((CommentWidget) v).getCommentPositon());
            } else {
                momentPresenter.showCommentBox(null, itemPosition, momentsInfo.getStatusID(), (CommentWidget) v);
            }
        }
    };

    private DeleteCommentPopup.OnDeleteCommentClickListener onDeleteCommentClickListener = new DeleteCommentPopup
            .OnDeleteCommentClickListener() {
        @Override
        public void onDelClick(CommentBean commentInfo, int commentPosition) {

            //ToastUtils.showToast(getContext(), itemPosition + ":" + commentPosition);
            momentPresenter.deleteComment(itemPosition, commentPosition, commentInfo);
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
            CircleBaseBean info = (CircleBaseBean) v.getTag(R.id.momentinfo_data_tag_id);
            if (info != null) {
                commentPopup.updateMomentInfo(info);
                commentPopup.showPopupWindow(commentImage);
            }
        }
    };
    private View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            momentPresenter.deleteRelease(itemPosition, momentsInfo.getStatusID());
        }
    };


    private CommentPopup.OnCommentPopupClickListener onCommentPopupClickListener = new CommentPopup
            .OnCommentPopupClickListener() {
        @Override
        public void onLikeClick(View v, @NonNull CircleBaseBean info, boolean hasLiked, Long friendPraiseID) {
            if (hasLiked) {
                momentPresenter.unLike(itemPosition, info.getStatusID(), friendPraiseID);
            } else {
                momentPresenter.addLike(itemPosition, info.getStatusID());

            }

        }

        @Override
        public void onCommentClick(View v, @NonNull CircleBaseBean info) {
            momentPresenter.showCommentBox(itemView, itemPosition, info.getStatusID(), null);
        }
    };

    /**
     * ============  tools method block
     */


    protected final View findView(View view, int resid) {
        if (resid > 0 && itemView != null && view == null) {
            return itemView.findViewById(resid);
        }
        return view;
    }


}
