package com.langbai.tdhd.mvp.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.CommentBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.circle.commentwidget.CommentWidget;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.CircleModel;
import com.langbai.tdhd.mvp.view.CircleView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CirclePresenter extends BasePresenter<CircleView> {
    CircleModel mModel = new CircleModel();

    //获取自己的朋友圈
    public void getMyOwnCircleList(int page, long ownID) {
        mModel.getMyOwnCircleList(page, ownID, new MVPCallBack<BaseResponseBean>() {
            @Override
            public void succeed(BaseResponseBean mData) {
                if (mView != null) {
                    mView.getMyCircleListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    public void getCircleList(int page) {
        mModel.getCircleList(page, new MVPCallBack<BaseResponseBean>() {
            @Override
            public void succeed(BaseResponseBean mData) {
                if (mView != null) {
                    mView.getMyCircleListSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }

    /**
     * 点赞一个朋友圈
     */
    public void addLike(final int itemPosition, long userPublishID) {
        CircleModel.addLike(userPublishID, new MVPCallBack<Long>() {
            @Override
            public void succeed(Long mData) {
                if (mView != null) {
                    mView.addLike(itemPosition, mData);
                }
            }

            @Override
            public void failed(String message) {

            }
        });


    }

    /**
     * 取消点赞一个朋友圈
     */
    public void unLike(final int itemPosition, Long userPublishID, Long friendPraiseID) {
        CircleModel.cancleLike(userPublishID, friendPraiseID, new MVPCallBack<Long>() {
            @Override
            public void succeed(Long mData) {
                if (mView != null) {
                    mView.cancleLike(itemPosition);
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, long statusID, @Nullable
            CommentWidget commentWidget) {
        if (mView != null) {
            mView.showCommentBox(viewHolderRootView, itemPos, statusID, commentWidget);
        }
    }

    //回复朋友圈 true 回复某个人 false 单独评论
    public void addComment(boolean isReParson, final int itemPos, long statusID, final CommentBean commentInfo,
                           final String commentContent) {
        if (isReParson) {
            mModel.addCommentToPerson(statusID, commentContent, commentInfo, new MVPCallBack<Long>() {
                @Override
                public void succeed(Long friendReviewID) {
                    mView.addComment(itemPos, friendReviewID, commentContent, commentInfo);
                }

                @Override
                public void failed(String message) {

                }
            });
        } else {
            mModel.addComment(statusID, commentContent, new MVPCallBack<Long>() {
                @Override
                public void succeed(Long friendReviewID) {
                    mView.addComment(itemPos, friendReviewID, commentContent, commentInfo);
                }

                @Override
                public void failed(String message) {

                }
            });
        }

    }

    //删除评论
    public void deleteComment(final int contentPosition, final int commentPosition, CommentBean commentInfo) {
        mModel.deleteComment(commentInfo, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null)
                    mView.deleteComment(contentPosition, commentPosition);
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    //删除自己发布的朋友圈
    public void deleteRelease(final int itemPosition, long statusID) {
        mModel.deleteRelease(statusID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null)
                    mView.deleteReleaseSuccess(itemPosition);
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    //更改朋友圈相册封面
    public void changeWallPicture(Context context, Map<String, RequestBody> params) {
        mModel.changeWallPicture(context, params, new MVPCallBack<LoginResponseBean>() {
            @Override
            public void succeed(LoginResponseBean mData) {
                if (mView != null) {
                    mView.changeWappPictureSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {
                requestFailed(message);
            }
        });
    }


}
