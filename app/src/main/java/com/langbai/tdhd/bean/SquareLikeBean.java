package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquareLikeBean implements Serializable{
    private long likeID;
    private String likeName;
    private long squarePraiseID;
    private int type;
    private long squarePublishID;
    private String likeAvatar;

    public SquareLikeBean(long likeID, String likeName, long squarePraiseID, int type, long squarePublishID, String 
            likeAvatar) {
        this.likeID = likeID;
        this.likeName = likeName;
        this.squarePraiseID = squarePraiseID;
        this.type = type;
        this.squarePublishID = squarePublishID;
        this.likeAvatar = likeAvatar;
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

    public long getSquarePraiseID() {
        return squarePraiseID;
    }

    public void setSquarePraiseID(long squarePraiseID) {
        this.squarePraiseID = squarePraiseID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSquarePublishID() {
        return squarePublishID;
    }

    public void setSquarePublishID(long squarePublishID) {
        this.squarePublishID = squarePublishID;
    }

    public String getLikeAvatar() {
        return likeAvatar;
    }

    public void setLikeAvatar(String likeAvatar) {
        this.likeAvatar = likeAvatar;
    }
}
