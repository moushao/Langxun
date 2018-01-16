package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/9/25.
 */

public class RegisterResponseBean implements Serializable {
    private String phone;                   //电话                登录用户电话
    private String realName;                //真实姓名            登录用户真实姓名
    private Long userInfoID;                //用户ID             后台用户ID，即UserID
    private int type;                       //用户类型            用户类型                       平台1 通兑2 小牛3
    private String serviceMobile;           //服务中心账号         服务中心账号

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }

    public void setServiceMobile(String serviceMobile) {
        this.serviceMobile = serviceMobile;
    }
}
