package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/10/20.
 */

import java.io.Serializable;

/**
 * 类名: {@link ThumbsUp}
 * <br/> 功能描述:朋友圈点赞基类
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/10/20
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class ThumbsUp implements Serializable {
    private String praiseDateStr;//点赞的时间戳
    private long likeID;//点赞的人的UserID
    private String likeName;//点赞人的姓名
    private long userPublishID;//这条内容在朋友圈发布的Id,
    private long friendPraiseID;//点赞ID,比如我点了赞,我的这个点赞id 
    private int type;//点赞人的平台类型,

    public ThumbsUp(String praiseDateStr, long likeID, String likeName, long userPublishID, long friendPraiseID, int
            type) {
        this.praiseDateStr = praiseDateStr;
        this.likeID = likeID;
        this.likeName = likeName;
        this.userPublishID = userPublishID;
        this.friendPraiseID = friendPraiseID;
        this.type = type;
    }

    public String getPraiseDateStr() {
        return praiseDateStr;
    }

    public void setPraiseDateStr(String praiseDateStr) {
        this.praiseDateStr = praiseDateStr;
    }

    public long getLikeID() {
        return likeID;
    }

    public void setLikeID(long likeID) {
        this.likeID = likeID;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public long getUserPublishID() {
        return userPublishID;
    }

    public void setUserPublishID(long userPublishID) {
        this.userPublishID = userPublishID;
    }

    public long getFriendPraiseID() {
        return friendPraiseID;
    }

    public void setFriendPraiseID(long friendPraiseID) {
        this.friendPraiseID = friendPraiseID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
