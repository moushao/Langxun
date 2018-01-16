package com.langbai.tdhd.bean;

/**
 * Created by Moushao on 2017/8/22.
 */

public class OtherBaseResponseBean<T> {
    public boolean success;
    public String msg;
    public T data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
