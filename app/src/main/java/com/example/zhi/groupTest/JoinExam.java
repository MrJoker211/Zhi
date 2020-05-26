package com.example.zhi.groupTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zhi.R;

import cn.bmob.v3.BmobUser;

public class JoinExam extends AppCompatActivity implements View.OnClickListener{

    private EditText testName;
    private EditText studentName;
    private Button sure;

    //获取当前用户名，获取测试名
    private String mStudentName;
    private String mUsername;
    private String mTestName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_exam);
        //初始化控件
        initView();
        //设置响应事件
        setOnClick();
    }


    private void setOnClick() {
        sure.setOnClickListener(this);
    }

    private void initView() {
        testName = findViewById(R.id.test_name);
        studentName = findViewById(R.id.student_name);
        sure = findViewById(R.id.sure);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                //先进行判空操作
                if(studentName.getText().toString().trim().equals("")){
                    Toast.makeText(JoinExam.this,"请输入您的姓名！",Toast.LENGTH_SHORT).show();
                }else if(testName.getText().toString().trim().equals("")){
                    Toast.makeText(JoinExam.this,"请输入考试名称！",Toast.LENGTH_SHORT).show();
                }else{
                    //获取数据
                    mTestName = testName.getText().toString().trim();
                    mStudentName = studentName.getText().toString().trim();
                    BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);//查询当前用户，获取用户名
                    mUsername = bmobUser.getUsername();
                    //进行带数据跳转
                    toJoinExam();
                }
                break;
            default:
                break;
        }
    }

    //执行带着数据进行跳转操作
    private void toJoinExam() {
        Intent intent = new Intent(JoinExam.this, StartExam.class);
        intent.putExtra("mTestName",mTestName);
        intent.putExtra("mStudentName",mStudentName);
        intent.putExtra("mUsername",mUsername);
        startActivity(intent);
        finish();
    }
}
