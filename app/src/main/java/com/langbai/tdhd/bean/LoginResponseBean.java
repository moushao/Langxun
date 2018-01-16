package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/9/25.
 */

public class LoginResponseBean implements Serializable {

    private String phone = "";               //	电话	     	登录用户电话
    private String sex = "";                 //	性别	    	登录用户性别
    private String signName = "";            //	个性签名	    登录用户个性签名
    private String userIcon = "";            //	头像	    	登录用户头像，图片路径
    private int type;                        //  用户类型     非必填      如果为空则默认为通兑2          平台1 通兑2 小牛3
    private String msg = "";                //	信息
    private String password = "";           //  用户类型     非必填      如果为空则默认为通兑2          平台1 通兑2 小牛3
    private int userType;                    //  用户权限     分1 2 3等级
    private Long userInfoID;                //	用户ID	    后台用户ID，即UserID
    private String area = "";                //	地区	    	登录用户所属地区
    private String nickName = "";                //	昵称
    private int age;                         //	年龄	    	登录用户年龄
    private String spacePicture = "";        // 朋友圈的图
    private String realName = "";            //	真实姓名	    登录用户真实姓名
    private String serviceMobile = "";


    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getArea() {
        return area;
    }

    public String getSignName() {
        return signName;
    }

    public int getAge() {
        return age;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getRealName() {
        return realName;
    }

    public Long getUserInfoID() {
        return userInfoID;
    }

    public int getType() {
        return type;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setUserInfoID(Long userInfoID) {
        this.userInfoID = userInfoID;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpacePicture() {
        return spacePicture;
    }

    public void setSpacePicture(String spacePicture) {
        this.spacePicture = spacePicture;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }

    public void setServiceMobile(String serviceMobile) {
        this.serviceMobile = serviceMobile;
    }
}
