package com.example.zhi.Bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String nickname;
    private String organization;
    private String groupNumber;
    private Number state;
    private Number isCommittee;


    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Number getState() {
        return state;
    }

    public void setState(Number state) {
        this.state = state;
    }

    public Number getIsCommittee() {
        return isCommittee;
    }

    public void setIsCommittee(Number isCommittee) {
        this.isCommittee = isCommittee;
    }
}
