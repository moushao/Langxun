package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CommentBean {
    private int formType;//评论人的平台类型
    private String toName;//被回复人的名字 张三 回复了 李四 我是李四
    private String formName;//回复人的名字 张三 回复了 李四 我是张三
    private long toID;//消息评论被回复人名称，
    private long commentID;//这条评论的id，
    private long state;//我也不知道这是个啥
    private long toType;//我也不知道这是个啥
    private String content;//评论的内容
    private String reviewDateStr;//评论的时间
    private long formID;//此评论的评论者userID
    private long userPublishID;//这条朋友圈内容本身的id

    public CommentBean(int formType,
                       String toName, 
                       String formName, 
                       long toID, long commentID, long state, long 
            toType, String content, String reviewDateStr, long formID, long userPublishID) {
        this.formType = formType;
        this.toName = toName;
        this.formName = formName;
        this.toID = toID;
        this.commentID = commentID;
        this.state = state;
        this.toType = toType;
        this.content = content;
        this.reviewDateStr = reviewDateStr;
        this.formID = formID;
        this.userPublishID = userPublishID;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public long getToID() {
        return toID;
    }

    public void setToID(long toID) {
        this.toID = toID;
    }

    public long getCommentID() {
        return commentID;
    }

    public void setCommentID(long commentID) {
        this.commentID = commentID;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public long getToType() {
        return toType;
    }

    public void setToType(long toType) {
        this.toType = toType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewDateStr() {
        return reviewDateStr;
    }

    public void setReviewDateStr(String reviewDateStr) {
        this.reviewDateStr = reviewDateStr;
    }

    public long getFormID() {
        return formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    public long getUserPublishID() {
        return userPublishID;
    }

    public void setUserPublishID(long userPublishID) {
        this.userPublishID = userPublishID;
    }
}
