package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/10/26.
 */

public class PraiseBean {
    private Long friendPraiseID;//点赞id
    private long friendReviewID;//评论回复id

    public long getFriendReviewID() {
        return friendReviewID;
    }

    public void setFriendReviewID(long friendReviewID) {
        this.friendReviewID = friendReviewID;
    }

    public Long getFriendPraiseID() {
        return friendPraiseID;
    }

    public void setFriendPraiseID(Long friendPraiseID) {
        this.friendPraiseID = friendPraiseID;
    }
}
