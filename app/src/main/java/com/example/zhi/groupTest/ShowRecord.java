package com.example.zhi.groupTest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zhi.Bean.Record;
import com.example.zhi.R;
import com.example.zhi.adapter.ShowRecordAdapter;
import com.example.zhi.utils.RecordInfo;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShowRecord extends AppCompatActivity {
    private List<RecordInfo> recordList = new ArrayList<>();

    private String mGroupNumber;
    private String mTestName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_record);

        //接收值
        Intent intent = getIntent();
        mGroupNumber = intent.getStringExtra("mGroupNumber");
        mTestName = intent.getStringExtra("mTestName");

        //获取对应成绩表的内容
        getRecord();
    }

    private void getRecord() {
        //根据接收到的数据进行复合查询
        //此处执行复合查询
        //查询测试名为给出测试名的内容
        BmobQuery<Record> eq1 = new BmobQuery<Record>();
        eq1.addWhereEqualTo("testName", mTestName);
        //查询群组号为当前用户群组号的内容
        BmobQuery<Record> eq2 = new BmobQuery<Record>();
        eq2.addWhereEqualTo("groupNumber", mGroupNumber);
        //最后组装完整的and条件
        List<BmobQuery<Record>> andQuerys = new ArrayList<BmobQuery<Record>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        //查询符合整个and条件的人
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.and(andQuerys);
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> object, BmobException e) {
                if (e == null) {
                    //处理查询到的数据
                    //将查询到的列表对xml进行渲染
                    int count = object.size();
                    for (int i = 0; i < count; i++) {

                        String userName = object.get(i).getUsername();
                        String studentName = object.get(i).getStudentName();
                        int record = object.get(i).getRecord().intValue();

                        RecordInfo recordInfo = new RecordInfo(userName,studentName,record);
                        recordList.add(recordInfo);

                        //在此处加载RecycleView
                        RecyclerView recyclerView = findViewById(R.id.show_record_recycle_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowRecord.this);
                        recyclerView.setLayoutManager(layoutManager);
                        ShowRecordAdapter adapter = new ShowRecordAdapter(recordList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    //处理异常信息
                    Toast.makeText(ShowRecord.this, "查询成绩列表异常！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
