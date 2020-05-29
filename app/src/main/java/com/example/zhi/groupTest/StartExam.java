package com.example.zhi.groupTest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zhi.Bean.ChoiceForGroup;
import com.example.zhi.Bean.Record;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.test.ReportCard;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class StartExam extends AppCompatActivity  implements View.OnClickListener {

    //测试名
    private TextView testName;
    //题目序号
    private TextView titleNumber;
    //题目
    private TextView title;
    //选项
    private TextView choiceA;
    private TextView choiceB;
    private TextView choiceC;
    private TextView choiceD;
    //选择框
    private CheckBox checkBoxA;
    private CheckBox checkBoxB;
    private CheckBox checkBoxC;
    private CheckBox checkBoxD;
    //确定
    private Button sure;
    //剩余的分和秒
    private TextView lastMinute;
    private TextView lastSecond;
    //总时间
    private long mCount = 600;
    //表长
    private int mTableLength;
    //数据的现在位置
    private int mCurrentIndex = 0;
    //记录正确与错误和未做答的数量 以及题目的总数量
    private int mRight = 0;
    private int mWrong = 0;
    private int mNotResponse = 10;
    private int mAll = 10;

    private Timer timer;

    private String mTestName;
    private String mStudentName;
    private String mUsername;

    private String mGroupNumber;

    @SuppressLint("HandlerLeak")
    // (2) 使用handler处理接收到的消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                if(mCount != 0){
                    mCount--;
                    lastMinute.setText(String.valueOf(mCount/60));
                    lastSecond.setText(String.valueOf(mCount%60));
                }else {
                    //时间到，执行带着数据进行跳转
                    timer.cancel();
                    toReport();
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_exam);

        //获取到测试名,学生名，学生账户
        Intent intent = getIntent();
        mTestName = intent.getStringExtra("mTestName");
        mStudentName = intent.getStringExtra("mStudentName");
        mUsername = intent.getStringExtra("mUsername");

        //初始化控件
        initView();

        //设置计时器
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,1000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

        //设置控件点击监听
        setClick();
        //获取问题，初始化界面,获取初始位置为0的内容，保证每次启动这个界面都从零开始
        getQuestion();

    }

    //先将数据存入到Record表中
    //执行带着数据进行跳转操作
    private void toReport() {
        /*
        * 存数据到Record表中，mTestName，mStudentName，mUsername，mGroupNumber
        * */
        //查询mGroupNumber
        BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
        String username = bmobUser.getUsername();
        BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username",username);
        categoryBmobQuery.findObjects(new FindListener<UserState>() {
            @Override
            public void done(List<UserState> object, BmobException e) {
                if (e == null) {
                    mGroupNumber = object.get(0).getGroupNumber();
                } else {
                    Toast.makeText(StartExam.this,"查询用户失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Record record = new Record();
        record.setTestName(mTestName);
        record.setStudentName(mStudentName);
        record.setUsername(mUsername);
        record.setRecord(mRight*10);
        record.setGroupNumber(mGroupNumber);
        record.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e!=null){
                    Toast.makeText(StartExam.this, "存储成绩失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = new Intent(StartExam.this, ReportCard.class);
        intent.putExtra("right",mRight);
        intent.putExtra("wrong",mWrong);
        intent.putExtra("notResponse",mNotResponse);
        startActivity(intent);
        timer.cancel();
        finish();
    }

    //加载问题
    private void getQuestion() {
        BmobQuery<ChoiceForGroup> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("testName", mTestName);
        categoryBmobQuery.findObjects(new FindListener<ChoiceForGroup>() {
            @Override
            public void done(List<ChoiceForGroup> object, BmobException e) {
                if (e == null) {
                    //获取到行数据对象的列表
                    mTableLength = object.size();
                    //根据Id去查询每一条数据，然后用来对界面进行渲染
                    BmobQuery<ChoiceForGroup> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject(object.get(mCurrentIndex).getObjectId(), new QueryListener<ChoiceForGroup>() {
                        @Override
                        public void done(ChoiceForGroup choiceForGroup, BmobException e) {
                            if (e == null) {
                                testName.setText(mTestName);
                                titleNumber.setText(String.valueOf(mCurrentIndex+1));
                                title.setText(choiceForGroup.getTitle());
                                choiceA.setText(choiceForGroup.getChoiceA());
                                choiceB.setText(choiceForGroup.getChoiceB());
                                choiceC.setText(choiceForGroup.getChoiceC());
                                choiceD.setText(choiceForGroup.getChoiceD());
                            } else {
                                Toast.makeText(StartExam.this, "渲染界面失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(StartExam.this, "查询数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //设置监听
    private void setClick() {
        checkBoxA.setOnClickListener(this);
        checkBoxB.setOnClickListener(this);
        checkBoxC.setOnClickListener(this);
        checkBoxD.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    //点击事件的处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkbox_a:
                checkBoxA.setChecked(true);
                checkBoxB.setChecked(false);
                checkBoxC.setChecked(false);
                checkBoxD.setChecked(false);
                break;
            case R.id.checkbox_b:
                checkBoxA.setChecked(false);
                checkBoxB.setChecked(true);
                checkBoxC.setChecked(false);
                checkBoxD.setChecked(false);
                break;
            case R.id.checkbox_c:
                checkBoxA.setChecked(false);
                checkBoxB.setChecked(false);
                checkBoxC.setChecked(true);
                checkBoxD.setChecked(false);
                break;
            case R.id.checkbox_d:
                checkBoxA.setChecked(false);
                checkBoxB.setChecked(false);
                checkBoxC.setChecked(false);
                checkBoxD.setChecked(true);
                break;
            case R.id.sure:
                //如果已经选定了答案就显示出答案,如果都未被选中就弹出请选择答案
                if (!((!checkBoxA.isChecked()) && (!checkBoxB.isChecked()) && (!checkBoxC.isChecked()) && (!checkBoxD.isChecked()))) {
                    //如果有一个被选中，对应到A,B,C,D然后与正确答案比较
                    if (checkBoxA.isChecked()) {
                        checkAnswer("A");
                    } else if (checkBoxB.isChecked()) {
                        checkAnswer("B");
                    } else if (checkBoxC.isChecked()) {
                        checkAnswer("C");
                    } else if (checkBoxD.isChecked()) {
                        checkAnswer("D");
                    }
                    //判定完答案就跳转到下一题
                    if (mCurrentIndex == (mAll-1)) {
                        //在到达最后一题还按下一题就会提示这个
                        Toast.makeText(StartExam.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                        //如果判定为最后一题就把它提交
                        //执行带着数据进行跳转
                        toReport();
                    } else {
                        //将答案栏隐藏,并清除选项框的选中状态
                        checkBoxA.setChecked(false);
                        checkBoxB.setChecked(false);
                        checkBoxC.setChecked(false);
                        checkBoxD.setChecked(false);
                        mCurrentIndex = mCurrentIndex + 1;
                        getQuestion();
                    }
                }else {
                    Toast.makeText(StartExam.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void checkAnswer(final String checkAnswer) {
        //根据当前在界面上渲染的题目进行查询
        BmobQuery<ChoiceForGroup> choiceForGroupBmobQuery = new BmobQuery<>();
        choiceForGroupBmobQuery.addWhereEqualTo("title", title.getText().toString().trim());
        choiceForGroupBmobQuery.findObjects(new FindListener<ChoiceForGroup>() {
            @Override
            public void done(List<ChoiceForGroup> object, BmobException e) {
                if (e == null) {
                    if(object.get(0).getAnswer().equals(checkAnswer)){
                        //做对
                        mRight = mRight + 1;
                        mNotResponse = mNotResponse - 1;
                        Toast.makeText(StartExam.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
                    }else{
                        //做错
                        mWrong = mWrong + 1;
                        mNotResponse = mNotResponse - 1;
                        Toast.makeText(StartExam.this, "做错了，再接再厉！正确答案是"+object.get(0).getAnswer(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StartExam.this, "加载答案失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //初始化控件
    private void initView() {

        testName = findViewById(R.id.test_name);
        titleNumber = findViewById(R.id.title_number);
        title = findViewById(R.id.choice_title);

        choiceA = findViewById(R.id.choice_a);
        choiceB = findViewById(R.id.choice_b);
        choiceC = findViewById(R.id.choice_c);
        choiceD = findViewById(R.id.choice_d);

        checkBoxA = findViewById(R.id.checkbox_a);
        checkBoxB = findViewById(R.id.checkbox_b);
        checkBoxC = findViewById(R.id.checkbox_c);
        checkBoxD = findViewById(R.id.checkbox_d);

        sure = findViewById(R.id.sure);

        lastMinute = findViewById(R.id.last_minute);
        lastSecond = findViewById(R.id.last_second);

    }
}
