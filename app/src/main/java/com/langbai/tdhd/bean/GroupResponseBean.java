package com.langbai.tdhd.bean;

import java.io.Serializable;

/**
 * Created by Moushao on 2017/10/12.
 */

public class GroupResponseBean implements Serializable{
    private String personnickname;  //用户群昵称
    private boolean membersonly;
    private int groupRoleID;//群角色（1—群主，2—监管员，3—普通群成员）
    private int maxusers;
    private long[] data_group_list_member;
    private int type;
    private String groupicon;//群头像，地址
    private String descriptions;//群描述，即群公告
    private boolean public_group;
    private int id;//后台群ID，即groupChatID
    private String created;
    private String name;//群名称
    private String owner;
    private boolean allowinvites;
    private String groupid;//环信群id，建群的时候由环信返回的群标志
    private String countMember;
    private int affiliations_count;
    private String custom;

    public String getPersonnickname() {
        return personnickname;
    }

    public void setPersonnickname(String personnickname) {
        this.personnickname = personnickname;
    }

    public boolean isMembersonly() {
        return membersonly;
    }

    public void setMembersonly(boolean membersonly) {
        this.membersonly = membersonly;
    }

    public int getGroupRoleID() {
        return groupRoleID;
    }

    public void setGroupRoleID(int groupRoleID) {
        this.groupRoleID = groupRoleID;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public long[] getData_group_list_member() {
        return data_group_list_member;
    }

    public void setData_group_list_member(long[] data_group_list_member) {
        this.data_group_list_member = data_group_list_member;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupicon() {
        return groupicon;
    }

    public void setGroupicon(String groupicon) {
        this.groupicon = groupicon;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public boolean isPublic_group() {
        return public_group;
    }

    public void setPublic_group(boolean public_group) {
        this.public_group = public_group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getCountMember() {
        return countMember;
    }

    public void setCountMember(String countMember) {
        this.countMember = countMember;
    }

    public int getAffiliations_count() {
        return affiliations_count;
    }

    public void setAffiliations_count(int affiliations_count) {
        this.affiliations_count = affiliations_count;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
