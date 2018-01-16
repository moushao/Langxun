package com.hyphenate.easeui;

import java.io.Serializable;

/**
 * 类名: {@link EventMessage}
 * <br/> 功能描述:用于EventBus传值
 * <br/> 作者: MouShao
 * <br/> 时间: 2017/9/20
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class EventMessage implements Serializable {
    public final static String GROUP_CHANGED = "GROUP_CHANGED";
    public final static String USER_CONTRACT = "USER_CONTRACT";
    public final static String DELETE_USER = "DELETE_USER";//删除好友
    //类型
    private String code;
    //消息,值
    private String message;

    public EventMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
