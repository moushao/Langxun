package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/11/23.
 */

public class FriendsSettingBean {
    private boolean friendBan;//不让好友看我
    private boolean ownBan; // 我不看好友

    public boolean isFriendBan() {
        return friendBan;
    }

    public void setFriendBan(boolean friendBan) {
        this.friendBan = friendBan;
    }

    public boolean isOwnBan() {
        return ownBan;
    }

    public void setOwnBan(boolean ownBan) {
        this.ownBan = ownBan;
    }
}
