package com.example.zhi.utils;

public class CompetitionInfo {
    private String username;
    private int usedTime;
    private int rightNumber;
    private int record;

    public CompetitionInfo(String username,int usedTime,int rightNumber,int record){
        this.username = username;
        this.usedTime = usedTime;
        this.rightNumber = rightNumber;
        this.record = record;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Number getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public Number getRightNumber() {
        return rightNumber;
    }

    public void setRightNumber(int rightNumber) {
        this.rightNumber = rightNumber;
    }

    public Number getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

}
