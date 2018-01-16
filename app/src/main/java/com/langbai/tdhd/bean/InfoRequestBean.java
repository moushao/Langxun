package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/9/26.
 */

public class InfoRequestBean {
    public Long userInfoID;             //	用户ID	Long	必填	用户ID
    public String userIcon;           //	用户头像	String		用户头像（base64加密二进制数据）
    public int age;                //	用户年龄	Int		用户年龄
    public String sex;                //	用户性别	String		用户性别
    public String signName;           //	用户个性签名	String	 	用户个性签名
    public String area;               //	用户地区	String		用户地区
}
