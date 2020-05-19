package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class JudgeForGroup extends BmobObject {
    private String testName;
    private String groupNumber;
    private String builderUserName;

    private String title;
    private String answer;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getBuilderUserName() {
        return builderUserName;
    }

    public void setBuilderUserName(String builderUserName) {
        this.builderUserName = builderUserName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
