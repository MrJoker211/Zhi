package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class Record extends BmobObject {

    private String testName;
    private String username;
    private String studentName;
    private Number record;
    private String groupNumber;

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Number getRecord() {
        return record;
    }

    public void setRecord(Number record) {
        this.record = record;
    }

}
