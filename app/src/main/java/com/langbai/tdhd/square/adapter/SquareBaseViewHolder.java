package com.langbai.tdhd.square.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.langbai.tdhd.R;
import com.langbai.tdhd.UserHelper;
import com.langbai.tdhd.activity.SquareActivity;
import com.langbai.tdhd.activity.SquareDetailsActivity;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.comment.ClickShowMoreLayout;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;
import com.langbai.tdhd.circle.holder.BaseMomentVH;
import com.langbai.tdhd.circle.holder.BaseRecyclerViewHolder;
import com.langbai.tdhd.circle.popup.CommentPopup;
import com.langbai.tdhd.circle.popup.DeleteCommentPopup;
import com.langbai.tdhd.circle.praisewidget.PraiseWidget;
import com.langbai.tdhd.circle.ui.photo.ImageLoadMnanger;
import com.langbai.tdhd.circle.ui.photo.SimpleObjectPool;
import com.langbai.tdhd.common.Constants;
import com.langbai.tdhd.mvp.presenter.BasePresenter;
import com.langbai.tdhd.mvp.presenter.SquarePresenter;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.TimeUtil;
import com.langbai.tdhd.utils.UIHelper;

import java.text.ParseException;
import java.util.List;

import static com.langbai.tdhd.adapter.CircleContentHodler.isListEmpty;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈基本item
 */
public abstract class SquareBaseViewHolder extends BaseRecyclerViewHolder<SquareBaseBean> implements
        BaseMomentVH<SquareBaseBean>, ViewGroup.OnHierarchyChangeListener {
    protected Context mContext;

    //头部
    protected ImageView avatar;
    protected ImageView comment_ic;
    protected ImageView like_ic;
    protected TextView nick;
    protected TextView menu;
    protected TextView time;
    protected TextView comment_tv;
    protected TextView like_tv;
    protected ClickShowMoreLayout userText;

    //底部
    protected View line;
    protected LinearLayout commentLayout;
    protected LinearLayout item_layout;

    //内容区
    protected RelativeLayout contentLayout;
    private long userID = UserHelper.getUserId();

    private SquarePresenter squarePresenter;
    private int itemPosition; //当条朋友圈的位置
    private SquareBaseBean squareInfo;
    private boolean isMyRelase;
    private int viewType;
    private boolean hasComment = false;
    private boolean hasLiked = false;
    private long hasLikeID;
    private int likePos;

    public SquareBaseViewHolder(Context context, ViewGroup viewGroup, int layoutResId, int viewType) {
        super(context, viewGroup, layoutResId);
        mContext = context;
        this.viewType = viewType;
        onFindView(itemView);

        //header
        avatar = (ImageView) findView(avatar, R.id.avatar);
        nick = (TextView) findView(nick, R.id.nick);
        menu = (TextView) findView(menu, R.id.menu);
        time = (TextView) findView(time, R.id.time);
        comment_ic = (ImageView) findView(comment_ic, R.id.comment_ic);
        comment_tv = (TextView) findView(comment_tv, R.id.comment_tv);
        like_ic = (ImageView) findView(like_ic, R.id.like_ic);
        like_tv = (TextView) findView(like_tv, R.id.like_tv);
        item_layout = (LinearLayout) findView(item_layout, R.id.item_layout);
        userText = (ClickShowMoreLayout) findView(userText, R.id.item_text_field);
    }

    public void setPresenter(SquarePresenter squarePresenter) {
        this.squarePresenter = squarePresenter;
    }

    public BasePresenter getPresenter() {
        return squarePresenter;
    }

    @Override
    public void onBindData(SquareBaseBean data, int position) {
        if (data == null) {
            LogUtil.e("数据是空的！！！！");
            findView(userText, R.id.item_text_field);
            userText.setText("这个动态的数据是空的。。。。OMG");
            return;
        }
        this.squareInfo = data;
        this.itemPosition = position;
        if (squareInfo.getUserID() == UserHelper.getUserId())
            isMyRelase = true;
        //通用数据绑定
        onBindMutualDataToViews(data);
        //传递到子类
        onBindDataToView(data, position, getViewType());
    }

    private void onBindMutualDataToViews(SquareBaseBean data) {
        //header
        ImageLoadMnanger.INSTANCE.loadImage(avatar, data.getAvatar());
        nick.setText(UserHelper.getNickNAme(data.getUserName(), data.getType()));

        switch (data.getType()) {
            case 1:
                nick.setText(data.getUserName() + "（KF）");
                break;
            case 2:
                nick.setText(data.getUserName() + "（TD）");
                break;
            case 3:
                nick.setText(data.getUserName() + "（XN）");
                break;
            default:
                nick.setText(data.getUserName());
                break;
        }
        if (TextUtils.isEmpty(data.getContent())) {
            userText.setVisibility(View.GONE);
        } else {
            userText.setText(data.getContent());
        }
        //bottom
        try {
            time.setText(TimeUtil.formatDate(data.getDateStr(), TimeUtil.DETAILS, TimeUtil.MMDDHHMM_CHINESE_TWO));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment_ic.setOnClickListener(mClickListener);
        like_ic.setOnClickListener(mClickListener);
        menu.setOnClickListener(mClickListener);
        item_layout.setOnClickListener(mClickListener);
        long id = UserHelper.getUserId();
        if (!isListEmpty(data.getLikeList())) {
            like_tv.setText(data.getLikeList().size() + "");
            for (int i = 0; i < data.getLikeList().size(); i++) {
                if (data.getLikeList().get(i).getLikeID() == id) {
                    hasLiked = true;
                    hasLikeID = data.getLikeList().get(i).getSquarePraiseID();
                    likePos = i;
                    like_ic.setImageResource(R.drawable.icon_like_has);
                    break;
                }
            }
        }
        if (!isListEmpty(data.getCommentList())) {
            comment_tv.setText(data.getCommentList().size() + "");
            for (SquareCommentBean bean : data.getCommentList()) {
                if (bean.getFormID() == id) {
                    hasComment = true;
                    comment_ic.setImageResource(R.drawable.icon_comment);
                    break;
                }
            }
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.item_layout:
                case R.id.comment_ic:
                    //SquareDetailsActivity.startAction(mContext, squareInfo, viewType, SquareActivity.TAG);

                    Intent itt = new Intent(mContext, SquareDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM, SquareActivity.TAG);
                    bundle.putSerializable("SQUARE_BEAN", squareInfo);
                    bundle.putInt("VIEW_TYPE", viewType);
                    bundle.putInt("POSITION", itemPosition);
                    itt.putExtras(bundle);
                    ((Activity) mContext).startActivityForResult(itt, SquareDetailsActivity.REQUEST_CODE);
                    break;
                case R.id.menu:
                    squarePresenter.showMenuDialog(itemPosition, isMyRelase, squareInfo);
                    break;
                case R.id.like_ic:
                    if (hasLiked) {
                        //                        like_ic.setImageResource(R.drawable.icon_like);
                        squarePresenter.cancleLike(itemPosition, likePos, squareInfo.getSquarePublishID(), hasLikeID);
                    } else {
                        //                        like_ic.setImageResource(R.drawable.icon_like_has);
                        squarePresenter.Like(itemPosition, squareInfo.getSquarePublishID());
                    }
                    break;
            }
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

    @Override
    public void onChildViewAdded(View view, View view1) {

    }

    @Override
    public void onChildViewRemoved(View view, View view1) {

    }
}
