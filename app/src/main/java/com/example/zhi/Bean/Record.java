package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class Record extends BmobObject {

    private String groupNumber;
    private String testName;
    private String account;
    private String studentName;
    private Number record;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
