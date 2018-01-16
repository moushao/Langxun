package com.langbai.tdhd.circle.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;

import java.util.List;


/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView {

    void onLikeChange(int itemPos, List<ThumbsUp> likeUserList);

    void onCommentChange(int itemPos, List<CommentBean> commentInfoList);

    /**
     * 因为recyclerview通过位置找到itemview有可能会找不到对应的View，所以这次直接传值
     *
     * @param viewHolderRootView
     * @param itemPos
     * @param momentid
     * @param commentWidget
     */
    void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid, CommentWidget commentWidget);

}
