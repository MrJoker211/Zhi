package com.example.zhi.utils;

public class RecordInfo {
    private String username;
    private String studentName;
    private int record;

    public RecordInfo(String username,String studentName,int record){
        this.username = username;
        this.studentName = studentName;
        this.record = record;
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

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }
}
