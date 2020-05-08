package com.example.zhi.Bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String nickname;

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

}
