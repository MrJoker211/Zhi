package com.example.zhi.forChapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zhi.Bean.ChoiceTable;
import com.example.zhi.R;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FifthChapter extends AppCompatActivity implements View.OnClickListener {

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

    //答案
    private TextView answerTitle;
    private TextView answer;

    //确定，上一题，下一题
    private Button sure;
    private Button lastQuestion;
    private Button nextQuestion;

    //表长
    private int mTableLength;
    //数据的现在位置
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifth_chapter);
        //初始化控件
        initView();
        //设置控件点击监听
        setClick();
        //获取问题，初始化界面,获取初始位置为0的内容，保证每次启动这个界面都从零开始
        getQuestion();


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
                    mTableLength = object.size();
                    BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject(object.get(mCurrentIndex).getObjectId(), new QueryListener<ChoiceTable>() {
                        @Override
                        public void done(ChoiceTable choiceTable, BmobException e) {
                            if (e == null) {
                                title.setText(choiceTable.getTitle());
                                choiceA.setText(choiceTable.getChoiceA());
                                choiceB.setText(choiceTable.getChoiceB());
                                choiceC.setText(choiceTable.getChoiceC());
                                choiceD.setText(choiceTable.getChoiceD());
                                answer.setText(choiceTable.getAnswer());
                            } else {
                                Toast.makeText(FifthChapter.this, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(FifthChapter.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        lastQuestion.setOnClickListener(this);
        nextQuestion.setOnClickListener(this);

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
                //点击确定按钮
                //显示出答案
                if (!((!checkBoxA.isChecked()) && (!checkBoxB.isChecked()) && (!checkBoxC.isChecked()) && (!checkBoxD.isChecked()))) {
                    answerTitle.setVisibility(View.VISIBLE);
                    answer.setVisibility(View.VISIBLE);
                }
                //进行比较弹出对错
                //核查哪个checkbox被选中
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
                //如果都未被选中先弹出请选择
                if ((!checkBoxA.isChecked()) && (!checkBoxB.isChecked()) && (!checkBoxC.isChecked()) && (!checkBoxD.isChecked())) {
                    Toast.makeText(FifthChapter.this, "请您选择一个答案！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.last_question:
                //查询上一题，并对界面进行渲染
                //首先判断上一题是不是空
                if (mCurrentIndex == 0) {
                    Toast.makeText(FifthChapter.this, "当前内容为第一题！", Toast.LENGTH_SHORT).show();
                } else {
                    //将答案栏隐藏,并清除选项框的选中状态
                    checkBoxA.setChecked(false);
                    checkBoxB.setChecked(false);
                    checkBoxC.setChecked(false);
                    checkBoxD.setChecked(false);
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
                    Toast.makeText(FifthChapter.this, "当前内容为最后一题！", Toast.LENGTH_SHORT).show();
                } else {
                    //将答案栏隐藏,并清除选项框的选中状态
                    checkBoxA.setChecked(false);
                    checkBoxB.setChecked(false);
                    checkBoxC.setChecked(false);
                    checkBoxD.setChecked(false);
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

    //点击确定后核对选择的答案
    private void checkAnswer(final String checkAnswer) {
        //此处可以添加寻找Id的内容，方便切换题目
        //加载问题
        BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<ChoiceTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
        bmobQuery.findObjects(new FindListener<ChoiceTable>() {
            @Override
            public void done(List<ChoiceTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //不然每经历一次查询顺序都会不一样
                    BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<>();
                    //根据当前的题目id，加载对应的答案
                    bmobQuery.getObject(object.get(mCurrentIndex).getObjectId(), new QueryListener<ChoiceTable>() {
                        @Override
                        public void done(ChoiceTable choiceTable, BmobException e) {
                            if (e == null) {
                                //正确答案与输入相比较
                                if (choiceTable.getAnswer().equals(checkAnswer)) {
                                    Toast.makeText(FifthChapter.this, "回答正确，干得漂亮！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FifthChapter.this, "做错了，再接再厉！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(FifthChapter.this, "加载答案失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(FifthChapter.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //初始化控件
    private void initView() {
        title = findViewById(R.id.choice_title);

        choiceA = findViewById(R.id.choice_a);
        choiceB = findViewById(R.id.choice_b);
        choiceC = findViewById(R.id.choice_c);
        choiceD = findViewById(R.id.choice_d);

        checkBoxA = findViewById(R.id.checkbox_a);
        checkBoxB = findViewById(R.id.checkbox_b);
        checkBoxC = findViewById(R.id.checkbox_c);
        checkBoxD = findViewById(R.id.checkbox_d);

        answerTitle = findViewById(R.id.answer_title);
        answer = findViewById(R.id.answer);


        sure = findViewById(R.id.sure);
        lastQuestion = findViewById(R.id.last_question);
        nextQuestion = findViewById(R.id.next_question);

    }
}
