package com.example.zhi.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Fragment.FragmentTest;
import com.example.zhi.R;

public class ReportCard extends AppCompatActivity implements View.OnClickListener {

    private int right;
    private int wrong;
    private int notResponse;
    private int rightPercent;
    private String data;

    private TextView rightReport;
    private TextView wrongReport;
    private TextView notDoneReport;
    private TextView percent;

    private TextView judge1;
    private TextView judge2;
    private TextView judge3;
    private TextView judge4;

    private Button backToHome;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_card);

        //接收值
        Intent intent = getIntent();
        right = intent.getIntExtra("right",10);
        wrong = intent.getIntExtra("wrong",10);
        notResponse = intent.getIntExtra("notResponse",10);
        rightPercent = right*10;

        initView();

        setClick();

        setText();

    }

    private void setText() {
        //渲染界面
        rightReport.setText(String.valueOf(right));
        wrongReport.setText(String.valueOf(wrong));
        notDoneReport.setText(String.valueOf(notResponse));
        percent.setText(String.valueOf(rightPercent));
        if(rightPercent<60){
            //小于六十的正确率
            judge4.setVisibility(View.VISIBLE);
        }else if(rightPercent<80){
            //小于八十的正确率
            judge3.setVisibility(View.VISIBLE);
        }else if(rightPercent<100){
            //小于一百的正确率
            judge2.setVisibility(View.VISIBLE);
        }else {
            //等于一百的正确率
            judge1.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        rightReport = findViewById(R.id.right_report);
        wrongReport = findViewById(R.id.wrong_report);
        notDoneReport = findViewById(R.id.not_done_report);
        percent = findViewById(R.id.percent_report);

        judge1 = findViewById(R.id.judge1);
        judge2 = findViewById(R.id.judge2);
        judge3 = findViewById(R.id.judge3);
        judge4 = findViewById(R.id.judge4);

        backToHome = findViewById(R.id.back_to_home);
    }

    //设置监听
    private void setClick() {
        backToHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_home:
                //点击以后销毁当前的活动，返回到主界面
                finish();
        }
    }
}
