package com.example.zhi.Bean;

import cn.bmob.v3.BmobObject;

public class JudgeTable extends BmobObject {

    private String title;
    private String answer;

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
