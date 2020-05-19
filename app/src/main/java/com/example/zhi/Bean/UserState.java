package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class UserState extends BmobObject {
    private String username;
    private String nickname;
    private String groupNumber;
    private Number state;
    private Number isCommittee;

    public Number getIsCommittee() {
        return isCommittee;
    }

    public void setIsCommittee(Number isCommittee) {
        this.isCommittee = isCommittee;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
