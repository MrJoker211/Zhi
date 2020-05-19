package com.example.zhi.utils;


//作为中间类，用于传值
public class UserName {
    private String username;
    private String nickname;

    public UserName(String username,String nickname){
        this.username = username;
        this.nickname = nickname;
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
}
