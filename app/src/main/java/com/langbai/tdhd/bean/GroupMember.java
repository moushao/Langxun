package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/10/13.
 */

public class GroupMember implements Serializable {

    private boolean friendNo;
    private long groupChatID;
    private long groupPersonID;
    private int groupRoleID;//角色
    private boolean state;//状态
    private String personNickName;//昵称
    private String personIcon;//;头像
    private String type;//平台类型
    private long userID; //此人的id,不是自己的
    private String personAge;//年龄
    private String personPhone;//电话
    private String personRealName;//姓名
    private String personSex;//些别
    private String personSignName;//签名
    private String personArea; //地址

    public boolean isFriendNo() {
        return friendNo;
    }

    public void setFriendNo(boolean friendNo) {
        this.friendNo = friendNo;
    }

    public long getGroupChatID() {
        return groupChatID;
    }

    public void setGroupChatID(long groupChatID) {
        this.groupChatID = groupChatID;
    }

    public long getGroupPersonID() {
        return groupPersonID;
    }

    public void setGroupPersonID(long groupPersonID) {
        this.groupPersonID = groupPersonID;
    }

    public int getGroupRoleID() {
        return groupRoleID;
    }

    public void setGroupRoleID(int groupRoleID) {
        this.groupRoleID = groupRoleID;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getPersonNickName() {
        return personNickName;
    }

    public void setPersonNickName(String personNickName) {
        this.personNickName = personNickName;
    }

    public String getPersonIcon() {
        return personIcon;
    }

    public void setPersonIcon(String personIcon) {
        this.personIcon = personIcon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getPersonAge() {
        return personAge;
    }

    public void setPersonAge(String personAge) {
        this.personAge = personAge;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getPersonRealName() {
        return personRealName;
    }

    public void setPersonRealName(String personRealName) {
        this.personRealName = personRealName;
    }

    public String getPersonSex() {
        return personSex;
    }

    public void setPersonSex(String personSex) {
        this.personSex = personSex;
    }

    public String getPersonSignName() {
        return personSignName;
    }

    public void setPersonSignName(String personSignName) {
        this.personSignName = personSignName;
    }

    public String getPersonArea() {
        return personArea;
    }

    public void setPersonArea(String personArea) {
        this.personArea = personArea;
    }
}
