package com.langbai.tdhd.utils;

import android.content.Context;
import android.util.Log;

import com.langbai.tdhd.bean.GroupResponseBean;
import com.langbai.tdhd.cache.UserCacheManager;
import com.langbai.tdhd.event.MVPCallBack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moushao on 2017/10/13.
 */

public class GroupUtils {

    private Context mContext;
    private DiskCacheManager manager;

    //TODO 新建群的时候,需要保存到缓存

    public GroupUtils(Context context) {
        mContext = context;
        manager = new DiskCacheManager(context, "GROUP_LIST");
    }

    /**
     * 保存群列表
     */
    public void saveGroupList(ArrayList<GroupResponseBean> list) {
        HashMap<String, GroupResponseBean> map = new HashMap<>();
        for (GroupResponseBean bean : list) {
            map.put(bean.getGroupid(), bean);
            UserCacheManager.save(bean.getGroupid(), bean.getName(), bean.getGroupicon());
        }
        manager.put("GROUP_LIST", map);
    }

    /**
     * 保存单个群
     */
    public void saveGroupItem(GroupResponseBean group) {
        HashMap<String, GroupResponseBean> map = manager.getSerializable("GROUP_LIST");
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(group.getGroupid(), group);
        UserCacheManager.save(group.getGroupid(), group.getName(), group.getGroupicon());
        manager.put("GROUP_LIST", map);
    }


    public long getGroupBackgroundID(String EMID) {
        HashMap<String, GroupResponseBean> map = manager.getSerializable("GROUP_LIST");
        if (map == null) {
            return -1;
        } else {
            try {
                GroupResponseBean bean = map.get(EMID);
                return bean.getId();
            } catch (Exception e) {
                return -1;
            }
        }
    }
}
