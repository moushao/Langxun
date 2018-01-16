package com.langbai.tdhd.mvp.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CircleBaseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.bean.ThumbsUp;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moushao on 2017/10/20.
 */

public interface CircleView extends BaseView {
    //获取朋友圈
    void getMyCircleListSuccess(BaseResponseBean mData);

    //喜欢
    void addLike(int itemPos, Long mData);

    //取消喜欢
    void cancleLike(int itemPos);

    /**
     * 因为recyclerview通过位置找到itemview有可能会找不到对应的View，所以这次直接传值
     *
     * @param viewHolderRootView
     * @param itemPos
     * @param momentid
     * @param commentWidget
     */
    void showCommentBox(@Nullable View viewHolderRootView, int itemPos, long momentid, CommentWidget commentWidget);

    //删除评论
    void deleteComment(int itemPos, int commentContent);

    //评论成功
    void addComment(int itemPos, Long mData, String commentContent, CommentBean commentInfo);

    //删除朋友圈成功
    void deleteReleaseSuccess(int itemPosition);

    void changeWappPictureSuccess(LoginResponseBean mData);
}
