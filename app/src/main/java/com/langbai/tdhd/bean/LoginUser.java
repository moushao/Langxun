package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/9/21.
 */

public class LoginUser {
    private String sex;
    private String  phone;
    private String area;
    private String signName;
    private int age;
    private String userIcon;
    private String realName;
    private Long userInfoID;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(Long userInfoID) {
        this.userInfoID = userInfoID;
    }
}
