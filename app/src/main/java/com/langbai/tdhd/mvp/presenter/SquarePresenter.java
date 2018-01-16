package com.langbai.tdhd.mvp.presenter;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;
import com.langbai.tdhd.event.MVPCallBack;
import com.langbai.tdhd.mvp.model.SquareModel;
import com.langbai.tdhd.mvp.view.SquareView;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquarePresenter extends BasePresenter<SquareView> {
    SquareModel mModel = new SquareModel();

    //获取自己的朋友圈
    public void getMyOwnSquareList(int page) {
        mModel.getMyOwnSquareList(page, new MVPCallBack<BaseResponseBean>() {
            @Override
            public void succeed(BaseResponseBean mData) {
                if (mView != null)
                    mView.getSquareListSuccess(mData);
            }

            @Override
            public void failed(String message) {
                requestFailed("");
            }
        });
    }

    //获取整个朋友圈
    public void getSquareList(int page) {
        mModel.getSquareList(page, new MVPCallBack<BaseResponseBean>() {
            @Override
            public void succeed(BaseResponseBean mData) {
                if (mView != null)
                    mView.getSquareListSuccess(mData);
            }

            @Override
            public void failed(String message) {
                requestFailed("");
            }
        });
    }

    public void showMenuDialog(int itemPosition, boolean isMyRelase, SquareBaseBean squareInfo) {
        if (mView != null) {
            mView.showMenuDialog(itemPosition, isMyRelase, squareInfo);
        }
    }

    public void getDeleteRelase(long squarePublishID) {
        mModel.getDeleteRelase(squarePublishID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null)
                    mView.deleteReleaseSuccess();
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void getReportContent(long squarePublishID, int reportType) {
        mModel.getReportContent(squarePublishID, reportType, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.reportSuccess();
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    //取消点赞
    public void cancleLike(final int itmePosition, final int likePos, long squarePublishID, long hasLikeID) {
        mModel.cancleLike(squarePublishID, hasLikeID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.cancleLikeSuccess(itmePosition, likePos);
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void Like(final int position, long squarePublishID) {
        mModel.LikeSquare(squarePublishID, new MVPCallBack<SquareLikeBean>() {
            @Override
            public void succeed(SquareLikeBean mData) {
                if (mView != null) {
                    mView.likeSquareSuccess(position, mData);
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void addComent(String commentContent, Long userID, Long squarePublishID) {
        mModel.addSquareComment(commentContent, userID, squarePublishID, new MVPCallBack<SquareCommentBean>() {
            @Override
            public void succeed(SquareCommentBean mData) {
                if (mView != null) {
                    mView.addCommentSuccess(mData);
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }

    public void getDeleteComment(final int pos, long reviewID) {
        mModel.deleteComment(reviewID, new MVPCallBack<String>() {
            @Override
            public void succeed(String mData) {
                if (mView != null) {
                    mView.deleteCommentSuccess(pos);
                }
            }

            @Override
            public void failed(String message) {

            }
        });
    }
}
