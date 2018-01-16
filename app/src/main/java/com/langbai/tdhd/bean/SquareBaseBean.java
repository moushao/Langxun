package com.langbai.tdhd.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Moushao on 2017/11/1.
 */

public class SquareBaseBean implements Serializable{
    private String phone;   
    private boolean status;
    private int type;
    private String avatar;
    private String dateStr;                 //日期
    private String content;
    private long userID;
    private ArrayList<String> thumbnail; //图片列表
    private String userName;
    private String videoPicture;
    private long squarePublishID;
    private String video;
    private String serviceMobile;
    private ArrayList<SquareLikeBean> likeList;
    private ArrayList<SquareCommentBean> commentList;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
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

    public ArrayList<String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ArrayList<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(String videoPicture) {
        this.videoPicture = videoPicture;
    }

    public long getSquarePublishID() {
        return squarePublishID;
    }

    public void setSquarePublishID(long squarePublishID) {
        this.squarePublishID = squarePublishID;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }

    public void setServiceMobile(String serviceMobile) {
        this.serviceMobile = serviceMobile;
    }

    public ArrayList<SquareLikeBean> getLikeList() {
        return likeList;
    }

    public void setLikeList(ArrayList<SquareLikeBean> likeList) {
        this.likeList = likeList;
    }

    public ArrayList<SquareCommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<SquareCommentBean> commentList) {
        this.commentList = commentList;
    }
}
