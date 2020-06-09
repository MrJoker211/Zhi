package com.example.zhi.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zhi.Bean.CompetitionRecord;
import com.example.zhi.R;
import com.example.zhi.adapter.ShowCompetitionRecordAdapter;
import com.example.zhi.utils.CompetitionInfo;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShowCompetitionRecord extends AppCompatActivity implements View.OnClickListener {
    private List<CompetitionInfo> competitionInfoList = new ArrayList<>();

    private List<CompetitionRecord> competitionRecords = new ArrayList<>();

    private String mUsername;
    private Number mUsedTime;
    private Number mRightNumber;
    private Number mRecord;

    private Button refresh;

    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_competition_record);

        //初始化控件
        initView();
        //设置点击监听
        setClick();

        //获取对应成绩表的内容
        getRecord();
        //展示成绩
        showRecord();
    }



    private void setClick() {
        refresh.setOnClickListener(this);
    }

    private void initView() {
        refresh = findViewById(R.id.refresh);
    }


    private void getRecord() {
        BmobQuery<CompetitionRecord> competitionRecordBmobQuery = new BmobQuery<>();
        competitionRecordBmobQuery.addQueryKeys("objectId");
        competitionRecordBmobQuery.findObjects(new FindListener<CompetitionRecord>() {
            @Override
            public void done(List<CompetitionRecord> list, BmobException e) {
                if (e == null) {
                    //处理查询到的数据
                    //将查询到的列表对xml进行渲染
                    mCount = list.size();
                    Toast.makeText(ShowCompetitionRecord.this, "count的内容为"+mCount, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < mCount; i++) {
                        competitionRecords.set(i, list.get(i));
                        //String username = list.get(i).getUsername();
                        //mUsername = list.get(i).getUsername();
                        //int usedTime = list.get(i).getUsedTime().intValue();
                        //int rightNumber = list.get(i).getRightNumber().intValue();
                        //int record = list.get(i).getRecord().intValue();

                        //CompetitionInfo competitionInfo = new CompetitionInfo(username,usedTime,rightNumber,record);

                        /*CompetitionInfo competitionInfo = new CompetitionInfo(mUsername,10,10,10);
                        competitionInfoList.add(competitionInfo);


                        //在此处加载RecycleView
                        RecyclerView recyclerView = findViewById(R.id.show_competition_record_recycle_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowCompetitionRecord.this);
                        recyclerView.setLayoutManager(layoutManager);
                        ShowCompetitionRecordAdapter showCompetitionRecordAdapter = new ShowCompetitionRecordAdapter(competitionInfoList);
                        recyclerView.setAdapter(showCompetitionRecordAdapter);*/
                    }
                } else {
                    //处理异常信息
                    Toast.makeText(ShowCompetitionRecord.this, "查询成绩列表异常！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void showRecord() {
        for (int i = 0; i < mCount; i++) {
            CompetitionInfo competitionInfo = new CompetitionInfo(competitionRecords.get(i).getUsername(),competitionRecords.get(i).getUsedTime().intValue(),competitionRecords.get(i).getRightNumber().intValue(),competitionRecords.get(i).getRecord().intValue());
            competitionInfoList.add(competitionInfo);
            //在此处加载RecycleView
            RecyclerView recyclerView = findViewById(R.id.show_competition_record_recycle_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ShowCompetitionRecord.this);
            recyclerView.setLayoutManager(layoutManager);
            ShowCompetitionRecordAdapter showCompetitionRecordAdapter = new ShowCompetitionRecordAdapter(competitionInfoList);
            recyclerView.setAdapter(showCompetitionRecordAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                //获取对应成绩表的内容
                getRecord();
                break;
            default:
                break;
        }
    }
}
