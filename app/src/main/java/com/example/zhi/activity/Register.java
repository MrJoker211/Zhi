package com.example.zhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zhi.Bean.User;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import java.util.Random;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText username,password,nickname;

    private CheckBox yes,no;

    private Button register;

    private int mIsCommittee = 0;
    private int mState = 2;
    private String mGroupNumber = "未加入组织";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        setClick();

    }

    private void setClick() {
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        register.setOnClickListener(this);

    }

    //初始化控件
    private void initView() {
        //获取输入的用户名,密码和昵称
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        nickname = findViewById(R.id.nickname);

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        register = findViewById(R.id.register);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                yes.setChecked(true);
                no.setChecked(false);
                break;
            case R.id.no:
                yes.setChecked(false);
                no.setChecked(true);
                break;
            case R.id.register:

                User user = new User();
                UserState userState = new UserState();

                if(yes.isChecked()){
                    mIsCommittee = 1;
                    mState = 1;
                    //随机生成8位群组号
                    mGroupNumber = getRandomString(8);
                }

                //赋值到User
                user.setUsername(username.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());
                user.setNickname(nickname.getText().toString().trim());

                //赋值到UserState
                userState.setUsername(username.getText().toString().trim());
                userState.setNickname(nickname.getText().toString().trim());
                userState.setGroupNumber(mGroupNumber);
                userState.setIsCommittee(mIsCommittee);
                userState.setState(mState);


                if(username.getText().toString().equals("")){
                    Toast.makeText(Register.this,"用户名没有输入！",Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equals("")){
                    Toast.makeText(Register.this,"密码没有输入！",Toast.LENGTH_SHORT).show();
                }else if(!(yes.isChecked()||no.isChecked())){
                    Toast.makeText(Register.this,"请选择身份！",Toast.LENGTH_SHORT).show();
                }else{
                    if(nickname.getText().toString().equals("")){
                        user.setNickname("未设置昵称");
                        userState.setNickname("未设置昵称");
                    }

                    userState.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId,BmobException e) {
                            if(e!=null){
                                Toast.makeText(Register.this,"存储数据失败！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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
                break;
            default:
                break;
        }
    }

    //生成指定length的随机字符串（A-Z，a-z，0-9),用于生成默认的群组号
    public static String getRandomString(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
