package com.example.zhi.groupTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Bean.User;
import com.example.zhi.Bean.UserState;
import com.example.zhi.Fragment.FragmentMine;
import com.example.zhi.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class JoinGroup extends AppCompatActivity implements View.OnClickListener{

    private EditText groupNumber;
    private Button joinGroup;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);

        //初始化控件
        initView();

        //设置点击事件
        setClick();

    }

    private void setClick() {
        joinGroup.setOnClickListener(this);
    }

    private void initView() {
        joinGroup = findViewById(R.id.join_group);
        groupNumber = findViewById(R.id.group_number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.join_group:
                if(groupNumber.getText().toString().equals("")){
                    Toast.makeText(JoinGroup.this,"群组号不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }
                //获取当前用户
                //加载个人信息,通过父类的函数获取当前的对象
                BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
                String username = bmobUser.getUsername();
                //先将群组号改为输入号
                //再将当前用户state改为0
                BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("username",username);
                categoryBmobQuery.findObjects(new FindListener<UserState>() {
                    @Override
                    public void done(List<UserState> object, BmobException e) {
                        if (e == null) {
                            UserState userState = object.get(0);
                            userState.setState(0);
                            userState.setGroupNumber(groupNumber.getText().toString().trim());
                            userState.update(userState.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(JoinGroup.this,"加入请求发送成功",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(JoinGroup.this,"加入请求发送失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(JoinGroup.this,"查询用户失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                //然后退出到个人中心界面并销毁当前界面
                finish();
                break;
        }
    }
}
