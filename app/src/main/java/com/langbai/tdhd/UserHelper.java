package com.langbai.tdhd;


import com.langbai.tdhd.bean.FriendsResponseBean;
import com.langbai.tdhd.bean.LoginResponseBean;
import com.langbai.tdhd.utils.LogUtil;
import com.langbai.tdhd.utils.PhoneFormatCheckUtils;

/**
 * 类名: {@link UserHelper}
 * <br/> 功能描述:管理用户信息基类
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/8/28
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class UserHelper {
    private static UserHelper instance = null;
    private LoginResponseBean logUser = null;

    private UserHelper() {

    }

    public synchronized static UserHelper getInstance() {
        if (instance == null) {
            instance = new UserHelper();
        }
        return instance;
    }

    public static long getUserId() {
        return getInstance().getLogUser().getUserInfoID();
    }

    public static int getUserType() {
        return getInstance().getLogUser().getType();
    }

    public static String getUserRealName() {
        return getInstance().getLogUser().getRealName();
    }

    public LoginResponseBean getLogUser() {
        return logUser;
    }

    public void setLogUser(LoginResponseBean logUser) {
        this.logUser = logUser;
    }

    public static String getEMID() {
        return getEMID(getInstance().getLogUser());
    }

    public static String getEMID(LoginResponseBean user) {
        String EMDI = user.getPhone();
        if (user.getType() != 2) {
            //除了通兑互动类型,环信登录帐号为phone+type,
            EMDI = user.getPhone() + user.getType();
        }
        return EMDI;
    }

    public static String getEMID(FriendsResponseBean user) {
        String EMDI = user.getFriendPhone();
        if (user.getType() != 2) {
            //除了通兑互动类型,环信登录帐号为phone+type,
            EMDI = user.getFriendPhone() + user.getType();
        }
        return EMDI;
    }

    public static String getEMNickName(LoginResponseBean user) {
        return getNickNAme(user.getRealName(), user.getType());
    }

    public static String getEMNickName(FriendsResponseBean user) {
        return getNickNAme(user.getFriendName(), user.getType());
    }

    public static String getEMID(String id, int type) {
        String EMDI = id;
        if (type != 2) {
            //除了通兑互动类型,环信登录帐号为phone+type,
            EMDI = id + type;
        }
        return EMDI;
    }

    public static String getNickNAme(String name, int type) {
        String nickName = "";
        switch (type) {
            case 1:
                nickName = name + "(KF)";
                break;
            case 2:
                nickName = name + "(TD)";
                break;
            case 3:
                nickName = name + "(XN)";
                break;
            default:
                nickName = name;
                break;
        }
        return nickName;
    }

    public static int getUserType(String account) {
        if (account.length() == 11) {
            return 2;
        } else {
            String s = account.substring(account.length() - 1);
            return Integer.valueOf(s);
        }

    }

    public static String getUserPhoneByEMID(String emId) {
        String s = emId;
        if (PhoneFormatCheckUtils.isChinaPhoneLegal(s)) {
            return s;
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }
    
    //判断扫描的二维码是不是合法的内容
    public static boolean isLegalQrContent(String content){
        if (content.length() == 11) {
            return PhoneFormatCheckUtils.isChinaPhoneLegal(content);
        } else if (content.length() == 12) {
            return PhoneFormatCheckUtils.isChinaPhoneLegal(content.substring(0, content.length() - 1));
        } else {
            return false;
        } 
    } 
}
