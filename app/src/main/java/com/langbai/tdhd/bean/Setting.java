package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/11/17.
 */

public class Setting implements Serializable {
    private boolean isReciveNew = true;
    private boolean isVoice = true;
    private boolean isShock = true;
    private boolean isHeadphone = true;
    private boolean isNotDisturb = false;

    
    public Setting(boolean isReciveNew, boolean isVoice, boolean isShock, boolean isHeadphone, boolean isNotDisturb) {
        this.isReciveNew = isReciveNew;
        this.isVoice = isVoice;
        this.isShock = isShock;
        this.isHeadphone = isHeadphone;
        this.isNotDisturb = isNotDisturb;
    }

    public boolean isReciveNew() {
        return isReciveNew;
    }

    public void setReciveNew(boolean reciveNew) {
        isReciveNew = reciveNew;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }

    public boolean isShock() {
        return isShock;
    }

    public void setShock(boolean shock) {
        isShock = shock;
    }

    public boolean isHeadphone() {
        return isHeadphone;
    }

    public void setHeadphone(boolean headphone) {
        isHeadphone = headphone;
    }

    public boolean isNotDisturb() {
        return isNotDisturb;
    }

    public void setNotDisturb(boolean notDisturb) {
        isNotDisturb = notDisturb;
    }
}
