package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class CompetitionRecord extends BmobObject {
    private String username;
    private Number usedTime;
    private Number rightNumber;
    private Number record;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Number getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Number usedTime) {
        this.usedTime = usedTime;
    }

    public Number getRightNumber() {
        return rightNumber;
    }

    public void setRightNumber(Number rightNumber) {
        this.rightNumber = rightNumber;
    }

    public Number getRecord() {
        return record;
    }

    public void setRecord(Number record) {
        this.record = record;
    }
}
