package com.example.zhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Bean.User;
import com.example.zhi.MainActivity;
import com.example.zhi.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {

    private EditText username,password,nickname;

    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //获取输入的用户名,密码和昵称
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        nickname = findViewById(R.id.nickname);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();

                //赋值到user
                user.setUsername(username.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());
                user.setNickname(nickname.getText().toString().trim());

                if(username.getText().toString().equals("")){
                    Toast.makeText(Register.this,"用户名没有输入",Toast.LENGTH_SHORT).show();

                }else if(password.getText().toString().equals("")){
                    Toast.makeText(Register.this,"密码没有输入",Toast.LENGTH_SHORT).show();
                }else{
                    if(nickname.getText().toString().equals("")){
                        user.setNickname("未设置昵称");
                    }
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                //注册成功，跳转到登录界面
                                Toast.makeText(Register.this,"注册成功，跳转登录",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this,"注册失败，请重新输入"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }
}
