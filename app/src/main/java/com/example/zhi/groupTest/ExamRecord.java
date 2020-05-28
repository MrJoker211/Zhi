package com.example.zhi.groupTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Bean.Record;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class ExamRecord extends AppCompatActivity implements View.OnClickListener{
    private EditText testName;
    private Button showRecord;

    private String mGroupNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_record);
        //初始化控件
        initView();
        //设置点击响应事件
        setOnClick();
    }

    private void setOnClick() {
        showRecord.setOnClickListener(this);
    }

    private void initView() {
        testName = findViewById(R.id.test_name);
        showRecord = findViewById(R.id.show_record);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_record:
                //执行携带数据跳转
                //首先判断非空
                if(!(testName.getText().toString().trim().equals(""))){
                    //如果内容非空
                    //然后判断输入内容可以查找的到且在对应的群组号相同的情况下
                    /*BmobQuery<Record> query = new BmobQuery<Record>();
                    query.addWhereEqualTo("testName", testName.getText().toString().trim());
                    query.count(Record.class, new CountListener() {
                        @Override
                        public void done(Integer count, BmobException e) {
                            if(e==null){
                                if(count==0){
                                    //查询数据为零证明不存在该测试
                                }
                            }else{

                            }
                        }
                    });*/

                    //此处执行查询得到当前用户的群组号
                    BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
                    String username = bmobUser.getUsername();
                    BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                    categoryBmobQuery.addWhereEqualTo("username",username);
                    categoryBmobQuery.findObjects(new FindListener<UserState>() {
                        @Override
                        public void done(List<UserState> object, BmobException e) {
                            if (e == null) {
                                //获取到当前用户的群组号
                                mGroupNumber = object.get(0).getGroupNumber();
                            } else {
                                Toast.makeText(ExamRecord.this,"查询用户失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //此处执行复合查询
                    //查询测试名为给出测试名的内容
                    BmobQuery<Record> eq1 = new BmobQuery<Record>();
                    eq1.addWhereEqualTo("testName", testName.getText().toString().trim());
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
                    /*query.findObjects(new FindListener<Record>() {
                        @Override
                        public void done(List<Record> object, BmobException e) {
                            if(e==null){

                            }else{

                            }
                        }
                    });*/
                    query.count(Record.class, new CountListener() {
                        @Override
                        public void done(Integer count, BmobException e) {
                            if(e==null){
                                if(count!=0){
                                    //查询数据不为零证明存在该测试，执行携带数据跳转
                                    toShowRecord();
                                }else{
                                    Toast.makeText(ExamRecord.this, "您所在群组不存在该测试！", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ExamRecord.this, "查询测试是否存在时出现错误！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ExamRecord.this, "请输入测试名！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //该方法执行携带数据跳转
    private void toShowRecord() {
        //执行跳转
        Intent intent = new Intent(ExamRecord.this, ShowRecord.class);
        intent.putExtra("mTestName",testName.getText().toString().trim());
        intent.putExtra("mGroupNumber",mGroupNumber);
        startActivity(intent);
        finish();
    }
}