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
import com.example.zhi.Bean.JudgeForGroup;
import com.example.zhi.Bean.Record;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.test.ReportCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class StartExam extends AppCompatActivity implements View.OnClickListener {

    //测试名
    private TextView testName;
    //题目序号
    private TextView titleNumber;

    private TextView di;
    private TextView ti;
    private TextView shengyu;
    private TextView fen;
    private TextView miao;

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

    private CheckBox right;
    private CheckBox wrong;


    //确定
    private Button start;
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

    private String mChoiceAnswer;
    private String mJudgeAnswer;

    //设置为0时,代表默认0道题，5时为五道题，10时为为十道题
    private int mChoiceTableLength = 0;
    private int mJudgeTableLength = 0;

    private List<ChoiceForGroup> mChoiceForGroups = new ArrayList<>();
    private List<JudgeForGroup> mJudgeForGroups = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    // (2) 使用handler处理接收到的消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (mCount != 0) {
                    mCount--;
                    lastMinute.setText(String.valueOf(mCount / 60));
                    lastSecond.setText(String.valueOf(mCount % 60));
                } else {
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
        //设置控件点击监听
        setClick();
        //获取问题列表
        getQuestionList();
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
        categoryBmobQuery.addWhereEqualTo("username", username);
        categoryBmobQuery.findObjects(new FindListener<UserState>() {
            @Override
            public void done(List<UserState> object, BmobException e) {
                if (e == null) {
                    mGroupNumber = object.get(0).getGroupNumber();
                    //将成绩写入到成绩表中
                    Record record = new Record();
                    record.setTestName(mTestName);
                    record.setStudentName(mStudentName);
                    record.setUsername(mUsername);
                    record.setRecord(mRight * 10);
                    record.setGroupNumber(mGroupNumber);
                    record.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e != null) {
                                Toast.makeText(StartExam.this, "存储成绩失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(StartExam.this, "查询用户失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = new Intent(StartExam.this, ReportCard.class);
        intent.putExtra("right", mRight);
        intent.putExtra("wrong", mWrong);
        intent.putExtra("notResponse", mNotResponse);
        startActivity(intent);
        timer.cancel();
        finish();
    }

    //加载问题列表
    private void getQuestionList() {
        BmobQuery<ChoiceForGroup> choiceForGroupBmobQuery = new BmobQuery<>();
        choiceForGroupBmobQuery.addWhereEqualTo("testName", mTestName);
        choiceForGroupBmobQuery.findObjects(new FindListener<ChoiceForGroup>() {
            @Override
            public void done(List<ChoiceForGroup> object, BmobException e) {
                if (e == null) {
                    //获取到行数据对象的列表
                    mChoiceTableLength = object.size();
                    if(mChoiceTableLength!=0){
                        mChoiceForGroups = object;
                    }
                } else {
                    Toast.makeText(StartExam.this, "查询数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BmobQuery<JudgeForGroup> judgeForGroupBmobQuery = new BmobQuery<>();
        judgeForGroupBmobQuery.addWhereEqualTo("testName", mTestName);
        judgeForGroupBmobQuery.findObjects(new FindListener<JudgeForGroup>() {
            @Override
            public void done(List<JudgeForGroup> object, BmobException e) {
                if (e == null) {
                    //获取到行数据对象的列表
                    mJudgeTableLength = object.size();
                    if(mJudgeTableLength!=0){
                        //不等于零进行赋值
                        mJudgeForGroups = object;
                    }
                } else {
                    Toast.makeText(StartExam.this, "查询数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getQuestion(){
        if(mChoiceTableLength == 10){
            //只有十道选择题
            testName.setText(mTestName);
            titleNumber.setText(String.valueOf(mCurrentIndex + 1));
            title.setText(mChoiceForGroups.get(mCurrentIndex).getTitle());
            choiceA.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceA());
            choiceB.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceB());
            choiceC.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceC());
            choiceD.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceD());
            //存储答案，作为比较正确与否用
            mChoiceAnswer = mChoiceForGroups.get(mCurrentIndex).getAnswer();
        }else if((mChoiceTableLength == 5)&&(mJudgeTableLength == 5)){
            //各五道题
            if(mCurrentIndex<5){
                testName.setText(mTestName);
                titleNumber.setText(String.valueOf(mCurrentIndex + 1));
                title.setText(mChoiceForGroups.get(mCurrentIndex).getTitle());
                choiceA.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceA());
                choiceB.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceB());
                choiceC.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceC());
                choiceD.setText(mChoiceForGroups.get(mCurrentIndex).getChoiceD());
                //存储答案，作为比较正确与否用
                mChoiceAnswer = mChoiceForGroups.get(mCurrentIndex).getAnswer();
            }else {
                //将选择题选项设置为隐藏，再将判断题选项设置为可见，
                //刚开始默认选择题为可见，判断为不可见
                checkBoxA.setVisibility(View.INVISIBLE);
                checkBoxB.setVisibility(View.INVISIBLE);
                checkBoxC.setVisibility(View.INVISIBLE);
                checkBoxD.setVisibility(View.INVISIBLE);
                choiceA.setVisibility(View.INVISIBLE);
                choiceB.setVisibility(View.INVISIBLE);
                choiceC.setVisibility(View.INVISIBLE);
                choiceD.setVisibility(View.INVISIBLE);

                right.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);

                testName.setText(mTestName);
                titleNumber.setText(String.valueOf(mCurrentIndex + 1));
                title.setText(mJudgeForGroups.get(mCurrentIndex-5).getTitle());
                //存储答案，作为比较正确与否用
                mJudgeAnswer = mJudgeForGroups.get(mCurrentIndex-5).getAnswer();
            }
        }else{
            //十道判断题
            checkBoxA.setVisibility(View.INVISIBLE);
            checkBoxB.setVisibility(View.INVISIBLE);
            checkBoxC.setVisibility(View.INVISIBLE);
            checkBoxD.setVisibility(View.INVISIBLE);
            choiceA.setVisibility(View.INVISIBLE);
            choiceB.setVisibility(View.INVISIBLE);
            choiceC.setVisibility(View.INVISIBLE);
            choiceD.setVisibility(View.INVISIBLE);

            right.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.VISIBLE);

            testName.setText(mTestName);
            titleNumber.setText(String.valueOf(mCurrentIndex + 1));
            //此处判断出索引为零
            title.setText(mJudgeForGroups.get(mCurrentIndex).getTitle());
            //存储答案，作为比较正确与否用
            mJudgeAnswer = mJudgeForGroups.get(mCurrentIndex).getAnswer();
        }
    }

    //设置监听
    private void setClick() {
        checkBoxA.setOnClickListener(this);
        checkBoxB.setOnClickListener(this);
        checkBoxC.setOnClickListener(this);
        checkBoxD.setOnClickListener(this);
        right.setOnClickListener(this);
        wrong.setOnClickListener(this);
        sure.setOnClickListener(this);

        start.setOnClickListener(this);
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
            case R.id.right:
                right.setChecked(true);
                wrong.setChecked(false);
                break;
            case R.id.wrong:
                right.setChecked(false);
                wrong.setChecked(true);
                break;
            case R.id.start:
                //将所有对应不可见的内容设置为可见
                di.setVisibility(View.VISIBLE);
                ti.setVisibility(View.VISIBLE);
                checkBoxA.setVisibility(View.VISIBLE);
                checkBoxB.setVisibility(View.VISIBLE);
                checkBoxC.setVisibility(View.VISIBLE);
                checkBoxD.setVisibility(View.VISIBLE);
                sure.setVisibility(View.VISIBLE);

                shengyu.setVisibility(View.VISIBLE);
                fen.setVisibility(View.VISIBLE);
                miao.setVisibility(View.VISIBLE);
                lastMinute.setVisibility(View.VISIBLE);
                lastSecond.setVisibility(View.VISIBLE);

                start.setVisibility(View.INVISIBLE);
                //将第一题进行显示
                getQuestion();
                //设置为在此处开始计时
                //设置计时器
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // (1) 使用handler发送消息
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }
                }, 0, 1000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

                break;
            case R.id.sure:
                if(mChoiceTableLength == 10){
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
                        if (mCurrentIndex == (mAll - 1)) {
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
                    } else {
                        Toast.makeText(StartExam.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                    }
                }else if(mJudgeTableLength == 10){
                    //判断题表长为十
                    //如果已经选定了答案就显示出答案,如果都未被选中就弹出请选择答案
                    if (!((!right.isChecked()) && (!wrong.isChecked()))) {
                        //如果有一个被选中，对应到错,对然后与正确答案比较
                        if (right.isChecked()) {
                            checkAnswer("对");
                        } else if (wrong.isChecked()) {
                            checkAnswer("错");
                        }
                        //判定完答案就跳转到下一题
                        if (mCurrentIndex == (mAll - 1)) {
                            //在到达最后一题还按下一题就会提示这个
                            Toast.makeText(StartExam.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                            //如果判定为最后一题就把它提交
                            //执行带着数据进行跳转
                            toReport();
                        } else {
                            //将答案栏隐藏,并清除选项框的选中状态
                            right.setChecked(false);
                            wrong.setChecked(false);
                            mCurrentIndex = mCurrentIndex + 1;
                            getQuestion();
                        }
                    } else {
                        Toast.makeText(StartExam.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(mCurrentIndex<5){
                        //小于5时用选择题
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
                            if (mCurrentIndex == (mAll - 1)) {
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
                        } else {
                            Toast.makeText(StartExam.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //大于5用判断题
                        if (!((!right.isChecked()) && (!wrong.isChecked()))) {
                            if (right.isChecked()) {
                                checkAnswer("对");
                            } else if (wrong.isChecked()) {
                                checkAnswer("错");
                            }
                            //判定完答案就跳转到下一题
                            if (mCurrentIndex == (mAll - 1)) {
                                //在到达最后一题还按下一题就会提示这个
                                Toast.makeText(StartExam.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                                toReport();
                            } else {
                                //将答案栏隐藏,并清除选项框的选中状态
                                right.setChecked(false);
                                wrong.setChecked(false);
                                mCurrentIndex = mCurrentIndex + 1;
                                getQuestion();
                            }
                        } else {
                            Toast.makeText(StartExam.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void checkAnswer(final String checkAnswer) {
        //首先判断对应的题型
        if(mChoiceTableLength == 10){
            //选择题十个，只用选择题的答案
            //再根据题型选择对应需要判断的答案
            if (mChoiceAnswer.equals(checkAnswer)) {
                //做对
                mRight = mRight + 1;
                mNotResponse = mNotResponse - 1;
                Toast.makeText(StartExam.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
            } else {
                //做错
                mWrong = mWrong + 1;
                mNotResponse = mNotResponse - 1;
                Toast.makeText(StartExam.this, "做错了，再接再厉！正确答案是" + mChoiceAnswer, Toast.LENGTH_SHORT).show();
            }
        }else if(mChoiceTableLength == 5){
            //选择题五个
            if(mCurrentIndex<5){
                //先进行选择题
                if (mChoiceAnswer.equals(checkAnswer)) {
                    //做对
                    mRight = mRight + 1;
                    mNotResponse = mNotResponse - 1;
                    Toast.makeText(StartExam.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
                } else {
                    //做错
                    mWrong = mWrong + 1;
                    mNotResponse = mNotResponse - 1;
                    Toast.makeText(StartExam.this, "做错了，再接再厉！正确答案是" + mChoiceAnswer, Toast.LENGTH_SHORT).show();
                }
            }else{
                //再进行判断题
                if (mJudgeAnswer.equals(checkAnswer)) {
                    //做对
                    mRight = mRight + 1;
                    mNotResponse = mNotResponse - 1;
                    Toast.makeText(StartExam.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
                } else {
                    //做错
                    mWrong = mWrong + 1;
                    mNotResponse = mNotResponse - 1;
                    Toast.makeText(StartExam.this, "做错了，再接再厉！正确答案是" + mJudgeAnswer, Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            //判断题十个
            //再根据题型选择对应需要判断的答案
            if (mJudgeAnswer.equals(checkAnswer)) {
                //做对
                mRight = mRight + 1;
                mNotResponse = mNotResponse - 1;
                Toast.makeText(StartExam.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
            } else {
                //做错
                mWrong = mWrong + 1;
                mNotResponse = mNotResponse - 1;
                Toast.makeText(StartExam.this, "做错了，再接再厉！正确答案是" + mJudgeAnswer, Toast.LENGTH_SHORT).show();
            }
        }
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

        right = findViewById(R.id.right);
        wrong = findViewById(R.id.wrong);
        sure = findViewById(R.id.sure);

        di = findViewById(R.id.di);
        ti = findViewById(R.id.ti);
        shengyu = findViewById(R.id.shengyu);
        fen = findViewById(R.id.fen);
        miao = findViewById(R.id.miao);
        start = findViewById(R.id.start);


        lastMinute = findViewById(R.id.last_minute);
        lastSecond = findViewById(R.id.last_second);

    }
}
