package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquareCommentBean implements Serializable {
    private String content;//评论的文字
    private String toName;//给谁评论
    private int formType; //评论人的平台
    private String formAvatar;//评论人的头像
    private long squareReviewID; //这条评论的id
    private long toID;//
    private String formName;//评论人的姓名
    private long formID; //评论人的id
    private int toType; //被评论人的平台类型
    private long squarePublishID; //这条广场的id

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public String getFormAvatar() {
        return formAvatar;
    }

    public void setFormAvatar(String formAvatar) {
        this.formAvatar = formAvatar;
    }

    public long getSquareReviewID() {
        return squareReviewID;
    }

    public void setSquareReviewID(long squareReviewID) {
        this.squareReviewID = squareReviewID;
    }

    public long getToID() {
        return toID;
    }

    public void setToID(long toID) {
        this.toID = toID;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public long getFormID() {
        return formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    public int getToType() {
        return toType;
    }

    public void setToType(int toType) {
        this.toType = toType;
    }

    public long getSquarePublishID() {
        return squarePublishID;
    }

    public void setSquarePublishID(long squarePublishID) {
        this.squarePublishID = squarePublishID;
    }
}
