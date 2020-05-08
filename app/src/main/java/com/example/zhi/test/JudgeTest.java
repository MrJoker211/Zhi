package com.example.zhi.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Bean.JudgeTable;
import com.example.zhi.R;
import com.example.zhi.forQuestionType.Judge;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class JudgeTest extends AppCompatActivity implements View.OnClickListener {

    //题目
    private TextView title;

    //选择框
    private CheckBox checkBoxRight;
    private CheckBox checkBoxWrong;

    //答案
    private TextView answerTitle;
    private TextView answer;

    //确定，上一题，下一题
    private Button sure;
    private Button lastQuestion;
    private Button nextQuestion;

    //定义全局的表长和数据的当前位置
    private int mTableLength;
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judge_test);
        //初始化控件
        initView();
        //设置控件点击监听
        setClick();
        //获取问题，初始化界面
        getQuestion();
    }
    //加载问题
    private void getQuestion() {
        BmobQuery<JudgeTable> bmobQuery = new BmobQuery<JudgeTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<JudgeTable>() {
            @Override
            public void done(List<JudgeTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //不然每经历一次查询顺序都会不一样
                    mTableLength = object.size();
                    BmobQuery<JudgeTable> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject(object.get(mCurrentIndex).getObjectId(), new QueryListener<JudgeTable>() {
                        @Override
                        public void done(JudgeTable judgeTable, BmobException e) {
                            if (e == null) {
                                title.setText(judgeTable.getTitle());
                                answer.setText(judgeTable.getAnswer());
                            } else {
                                Toast.makeText(JudgeTest.this, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(JudgeTest.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //设置监听
    private void setClick() {
        checkBoxRight.setOnClickListener(this);
        checkBoxWrong.setOnClickListener(this);

        sure.setOnClickListener(this);
        lastQuestion.setOnClickListener(this);
        nextQuestion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                checkBoxRight.setChecked(true);
                checkBoxWrong.setChecked(false);
                break;
            case R.id.wrong:
                checkBoxRight.setChecked(false);
                checkBoxWrong.setChecked(true);
                break;
            case R.id.sure:
                //点击确定按钮
                //显示出答案
                answerTitle.setVisibility(View.VISIBLE);
                answer.setVisibility(View.VISIBLE);
                //进行比较弹出对错
                //核查哪个checkbox被选中
                //如果“对”被选中，用对与正确答案比较
                if(checkBoxRight.isChecked()){
                    checkAnswer("对");
                }
                //如果“错”被选中，用错与正确答案比较
                if(checkBoxWrong.isChecked()){
                    checkAnswer("错");
                }
                //如果都未被选中先弹出请选择
                if((!checkBoxWrong.isChecked())&&(!checkBoxRight.isChecked())){
                    Toast.makeText(JudgeTest.this,"请您选择一个答案！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.last_question:
                //查询上一题，并对界面进行渲染
                //首先判断上一题是不是空
                if (mCurrentIndex == 0) {
                    Toast.makeText(JudgeTest.this, "当前内容为第一题！", Toast.LENGTH_SHORT).show();
                } else {
                    //将答案栏隐藏,并清除选项框的选中状态
                    checkBoxRight.setChecked(false);
                    checkBoxWrong.setChecked(false);
                    answerTitle.setVisibility(View.INVISIBLE);
                    answer.setVisibility(View.INVISIBLE);
                    mCurrentIndex = mCurrentIndex - 1;
                    getQuestion();
                }
                break;
            case R.id.next_question:
                //查询下一题，并对界面进行渲染
                //首先判断下一题是不是空
                if (mCurrentIndex == (mTableLength-1)) {
                    Toast.makeText(JudgeTest.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                } else {
                    //将答案栏隐藏,并清除选项框的选中状态
                    checkBoxRight.setChecked(false);
                    checkBoxWrong.setChecked(false);
                    answerTitle.setVisibility(View.INVISIBLE);
                    answer.setVisibility(View.INVISIBLE);
                    mCurrentIndex = mCurrentIndex + 1;
                    getQuestion();
                }
                break;
            default:
                break;
        }
    }
    //核对答案
    private void checkAnswer(final String checkAnswer) {
        BmobQuery<JudgeTable> bmobQuery = new BmobQuery<JudgeTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<JudgeTable>() {
            @Override
            public void done(List<JudgeTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //不然每经历一次查询顺序都会不一样
                    BmobQuery<JudgeTable> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject(object.get(mCurrentIndex).getObjectId(), new QueryListener<JudgeTable>() {
                        @Override
                        public void done(JudgeTable judgeTable, BmobException e) {
                            if (e == null) {
                                //正确答案与输入相比较
                                if(judgeTable.getAnswer().equals(checkAnswer)){
                                    Toast.makeText(JudgeTest.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(JudgeTest.this, "做错了，再接再厉！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(JudgeTest.this, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(JudgeTest.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //初始化
    private void initView() {
        title = findViewById(R.id.choice_title);

        checkBoxRight = findViewById(R.id.right);
        checkBoxWrong = findViewById(R.id.wrong);

        answerTitle = findViewById(R.id.answer_title);
        answer = findViewById(R.id.answer);


        sure = findViewById(R.id.sure);
        lastQuestion = findViewById(R.id.last_question);
        nextQuestion = findViewById(R.id.next_question);

    }
}
