package com.langbai.tdhd.bean;

import java.util.ArrayList;

/**
 * Created by Moushao on 2017/10/17.
 */

public class MeetingBase {
    private String message;
    private String status;
    private ArrayList<String> meetingGroup;
    private ArrayList<Meeting> meetDetail;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getMeetingGroup() {
        return meetingGroup;
    }

    public void setMeetingGroup(ArrayList<String> meetingGroup) {
        this.meetingGroup = meetingGroup;
    }

    public ArrayList<Meeting> getMeetDetail() {
        return meetDetail;
    }

    public void setMeetDetail(ArrayList<Meeting> meetDetail) {
        this.meetDetail = meetDetail;
    }
}
