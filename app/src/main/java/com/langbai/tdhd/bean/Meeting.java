package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/10/17.
 */

public class Meeting implements Serializable {

    private String meetingName;
    private int meetingMaxusers;
    private boolean state;
    private String owner;
    private long meetingID;
    private String groupid;
    private String meetingLock;
    private String meetingHXID;
    private String meetingDescript;

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public int getMeetingMaxusers() {
        return meetingMaxusers;
    }

    public void setMeetingMaxusers(int meetingMaxusers) {
        this.meetingMaxusers = meetingMaxusers;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(long meetingID) {
        this.meetingID = meetingID;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getMeetingLock() {
        return meetingLock;
    }

    public void setMeetingLock(String meetingLock) {
        this.meetingLock = meetingLock;
    }

    public String getMeetingHXID() {
        return meetingHXID;
    }

    public void setMeetingHXID(String meetingHXID) {
        this.meetingHXID = meetingHXID;
    }

    public String getMeetingDescript() {
        return meetingDescript;
    }

    public void setMeetingDescript(String meetingDescript) {
        this.meetingDescript = meetingDescript;
    }
}
