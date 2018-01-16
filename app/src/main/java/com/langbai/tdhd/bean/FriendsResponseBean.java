package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/9/28.
 */

public class FriendsResponseBean implements Serializable {


    private String friendType;
    private long goodFriendID;
    private long friendID;
    private String friendSex;
    private int type;
    private String friendName;
    private int userType;
    private String friendIcon;
    private long userID;
    private String friendPhone;
    private String friendComment;
    private boolean friendState;
    private String friendAge;
    private String userName;
    private String friendArea;
    private String userPhone;
    private int friendUserType;
    private String friendSignName;

    public FriendsResponseBean(int type, String friendName, int userType, String friendIcon) {
        this.type = type;
        this.friendName = friendName;
        this.userType = userType;
        this.friendIcon = friendIcon;
    }

    public Long getFriendID() {
        return friendID;
    }

    public void setFriendID(Long friendID) {
        this.friendID = friendID;
    }

    public String getFriendSex() {
        return friendSex;
    }

    public void setFriendSex(String friendSex) {
        this.friendSex = friendSex;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendIcon() {
        return friendIcon;
    }

    public void setFriendIcon(String friendIcon) {
        this.friendIcon = friendIcon;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getFriendComment() {
        return friendComment;
    }

    public void setFriendComment(String friendComment) {
        this.friendComment = friendComment;
    }


    public String getFriendArea() {
        return friendArea;
    }

    public void setFriendArea(String friendArea) {
        this.friendArea = friendArea;
    }

    public String getFriendSignName() {
        return friendSignName;
    }

    public void setFriendSignName(String friendSignName) {
        this.friendSignName = friendSignName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFriendType() {
        return friendType;
    }

    public void setFriendType(String friendType) {
        this.friendType = friendType;
    }

    public long getGoodFriendID() {
        return goodFriendID;
    }

    public void setGoodFriendID(long goodFriendID) {
        this.goodFriendID = goodFriendID;
    }

    public void setFriendID(long friendID) {
        this.friendID = friendID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isFriendState() {
        return friendState;
    }

    public void setFriendState(boolean friendState) {
        this.friendState = friendState;
    }

    public String getFriendAge() {
        return friendAge;
    }

    public void setFriendAge(String friendAge) {
        this.friendAge = friendAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getFriendUserType() {
        return friendUserType;
    }

    public void setFriendUserType(int friendUserType) {
        this.friendUserType = friendUserType;
    }
}
