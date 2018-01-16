package com.langbai.tdhd.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moushao on 2017/10/20.
 */

public class CircleBaseBean {
    private String avatar;//头像
    private int type;//平台类型
    private String content;//文字内容
    private long userID;//谁发的朋友圈
    private long statusID;//发布信息ID 没懂
    private List<String> thumbnail;//朋友圈组图1-9张 
    private List<ThumbsUp> likeList;//点赞列表 
    private String userName;//发布朋友圈这个人的姓名
    private String sdate;//发布时间
    private String videoPicture;//视频封面图
    private String video;//视频链接
    private ArrayList<CommentBean> commentList;//朋友圈的评论

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getStatusID() {
        return statusID;
    }

    public void setStatusID(long statusID) {
        this.statusID = statusID;
    }

    public List<String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ArrayList<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ThumbsUp> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<ThumbsUp> likeList) {
        this.likeList = likeList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(String videoPicture) {
        this.videoPicture = videoPicture;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ArrayList<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<CommentBean> commentList) {
        this.commentList = commentList;
    }
}
