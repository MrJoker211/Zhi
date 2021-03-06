package com.example.zhi.test;

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

import com.example.zhi.Bean.ChoiceTable;
import com.example.zhi.Bean.CompetitionRecord;
import com.example.zhi.Bean.Record;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.groupTest.StartExam;
import com.example.zhi.utils.CompetitionInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class CompetitionTest extends AppCompatActivity implements View.OnClickListener {

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
    private int mCount = 100;
    //表长
    private int mTableLength;
    //数据的现在位置
    private int mCurrentIndex = 0;

    //记录正确与错误和未做答的数量 以及题目的总数量
    private int mRight = 0;
    private int mWrong = 0;
    private int mNotResponse = 10;

    private String mChoiceAnswer;

    private Timer timer;

    private int[] result;

    //排名
    private int mRanking;

    private int mRecord;

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
                    saveRecord();
                    getRanking();
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_test);
        //初始化控件
        initView();
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

        //设置控件点击监听
        setClick();
        //获取随机数字
        getRandomNumber();
    }

    private void getRandomNumber() {
        BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<ChoiceTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<ChoiceTable>() {
            @Override
            public void done(List<ChoiceTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //不然每经历一次查询顺序都会不一样
                    mTableLength = object.size();
                    result = randomCommon(0, mTableLength, 10);
                    getQuestion();
                } else {
                    Toast.makeText(CompetitionTest.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    private void getRanking() {
        //对record进行查询，所有大于它的
        BmobQuery<CompetitionRecord> competitionRecordBmobQuery = new BmobQuery<>();
        //可能没有执行此处内容
        competitionRecordBmobQuery.addWhereGreaterThan("record", mCount*2 + mRight*10);

        competitionRecordBmobQuery.count(CompetitionRecord.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    //前面有count个比自己大的成绩，加一以后就是自己的排名
                    mRanking = count + 1;
                    Intent intent = new Intent(CompetitionTest.this, CompetitionReportCard.class);
                    intent.putExtra("right", mRight);
                    intent.putExtra("wrong", mWrong);
                    intent.putExtra("record", mRecord);
                    intent.putExtra("ranking", mRanking);
                    startActivity(intent);
                    timer.cancel();
                    finish();
                }else{
                    Toast.makeText(CompetitionTest.this, "查询数量失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveRecord() {
        /*
         * 存数据到CompetitionRecord表中，username，usedTime，rightNumber，record
         * */
        //查询mGroupNumber
        BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
        String username = bmobUser.getUsername();
        //用时的获取  总时间减去剩余时间
        int usedTime = 100 - mCount;
        //正确量mRight
        //成绩
        mRecord = mCount*2 + mRight*10 ;

        CompetitionRecord competitionRecord = new CompetitionRecord();
        competitionRecord.setUsername(username);
        competitionRecord.setUsedTime(usedTime);
        competitionRecord.setRightNumber(mRight);
        competitionRecord.setRecord(mRecord);
        competitionRecord.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e != null) {
                    Toast.makeText(CompetitionTest.this, "存储成绩失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //加载问题
    private void getQuestion() {
        BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<ChoiceTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<ChoiceTable>() {
            @Override
            public void done(List<ChoiceTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //不然每经历一次查询顺序都会不一样
                    //mTableLength = object.size();
                    BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject(object.get(result[mCurrentIndex]).getObjectId(), new QueryListener<ChoiceTable>() {
                        @Override
                        public void done(ChoiceTable choiceTable, BmobException e) {
                            if (e == null) {
                                titleNumber.setText(String.valueOf(mCurrentIndex + 1));
                                title.setText(choiceTable.getTitle());
                                choiceA.setText(choiceTable.getChoiceA());
                                choiceB.setText(choiceTable.getChoiceB());
                                choiceC.setText(choiceTable.getChoiceC());
                                choiceD.setText(choiceTable.getChoiceD());
                                mChoiceAnswer = choiceTable.getAnswer();
                            } else {
                                Toast.makeText(CompetitionTest.this, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CompetitionTest.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    if (mCurrentIndex == (10 - 1)) {
                        //在到达最后一题还按下一题就会提示这个
                        Toast.makeText(CompetitionTest.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                        //保存成绩
                        saveRecord();
                        //获取排名,执行带着数据进行跳转
                        getRanking();
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
                    Toast.makeText(CompetitionTest.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //点击确定后核对选择的答案
    private void checkAnswer(final String checkAnswer) {
        //正确答案与输入相比较
        if (mChoiceAnswer.equals(checkAnswer)) {
            mRight = mRight + 1;
            mNotResponse = mNotResponse - 1;
            Toast.makeText(CompetitionTest.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
        } else {
            mWrong = mWrong + 1;
            mNotResponse = mNotResponse - 1;
            Toast.makeText(CompetitionTest.this, "做错了，再接再厉！正确答案是" + mChoiceAnswer, Toast.LENGTH_SHORT).show();
        }
    }

    //初始化控件
    private void initView() {
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
