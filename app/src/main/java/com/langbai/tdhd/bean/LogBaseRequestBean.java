package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/9/25.
 */

/**
 * 包含登录\找回密码\注册模块的请求字段
 */
public class LogBaseRequestBean {
    public String realName;             //用户真实姓名	String	必填	        用户真实姓名 
    public String phone;                //用户电话号码	String	必填	        电话号码，同时也是登陆账号和环信账号 
    public String password;             //密码	        String	必填	        密码，md5加密后发过来 
    public String code;                 //验证码	        String	必填	        短信验证码 
    public String serviceMobile;        //服务中心账号	String	非必填	    服务中心账号 
    public String account;              //登录账号	    String	必填	        用户登录账号，即电话号码
    public int type;                    //用户类型       int     非必填      如果为空则默认为通兑2          平台1 通兑2 小牛3


    public LogBaseRequestBean() {
    }

    public String getRealName() {
        return realName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }

    public String getAccount() {
        return account;
    }

    public int getType() {
        return type;
    }
}    
