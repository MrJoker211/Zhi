package com.example.zhi.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.MainActivity;
import com.example.zhi.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //延时操作,两秒延时
        Timer timer = new Timer();
        timer.schedule(timetask,2000);

        Bmob.initialize(this,"51a3da36d3985eb259871fb055a8b67f");

    }

    TimerTask timetask = new TimerTask() {
        @Override
        public void run() {
            //如果已登录，跳转到主界面。没登录的话跳转到登录界面
            BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
            if(bmobUser!=null){
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                //关闭此活动
                finish();
            }else {
                //没有登录
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                //关闭此活动
                finish();
            }
        }
    };
}
