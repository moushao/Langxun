package com.langbai.tdhd.mvp.view;

import com.langbai.tdhd.bean.BaseResponseBean;
import com.langbai.tdhd.bean.SquareBaseBean;
import com.langbai.tdhd.bean.SquareCommentBean;
import com.langbai.tdhd.bean.SquareLikeBean;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/11/1.
 */

public interface SquareView extends BaseView {

    void getSquareListSuccess(BaseResponseBean mData);

    void showMenuDialog(int itemPosition, boolean isMyRelase, SquareBaseBean squareInfo);

    void deleteReleaseSuccess();

    void reportSuccess();

    void likeSquareSuccess(int p,SquareLikeBean mData);

    void cancleLikeSuccess(int itmePosition, int likePos);

    void addCommentSuccess(SquareCommentBean mData);

    void deleteCommentSuccess(int pos);
}
