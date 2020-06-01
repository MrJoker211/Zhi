package com.example.zhi.groupTest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhi.Bean.ChoiceForGroup;
import com.example.zhi.Bean.ChoiceTable;
import com.example.zhi.Bean.JudgeForGroup;
import com.example.zhi.Bean.JudgeTable;
import com.example.zhi.Bean.User;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.forQuestionType.Choice;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class ReleaseExam extends AppCompatActivity implements View.OnClickListener {
    private EditText testName;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private Button submit;

    private String mTestName;
    private String mGroupNumber;
    private String mUsername;

    private int mChoice;

    private int mTableLengthForChoice = 0;
    private int mTableLengthForJudge = 0;

    private int mIndex[] = new int[10];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_exam);

        //初始化控件
        intiView();
        //设置响应监听
        setOnClick();
    }

    //初始化控件
    private void intiView() {
        testName = findViewById(R.id.test_name);
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        submit = findViewById(R.id.submit);
    }

    //设置响应监听
    private void setOnClick() {
        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkbox1:
                checkBox1.setChecked(true);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                break;
            case R.id.checkbox2:
                checkBox1.setChecked(false);
                checkBox2.setChecked(true);
                checkBox3.setChecked(false);
                break;
            case R.id.checkbox3:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(true);
                break;
            case R.id.submit:
                toSubmit();
                finish();
                break;
            default:
                break;
        }
    }

    private void toSubmit() {
        //首先判断测试名和checkbox是否非空
        if (!(testName.getText().toString().equals(""))) {
            //输入框不为空，判断checkbox是否为空
            if ((checkBox1.isChecked() || checkBox2.isChecked() || checkBox3.isChecked())) {
                /*
                 * 被选中
                 * 1,将测试名称，发布试题人员账号，出题方式记录下来，
                 */
                //获取测试名称
                mTestName = testName.getText().toString().trim();
                //查询当前用户，并获取其username
                BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
                mUsername = bmobUser.getUsername();
                //根据当前用户名查询UserState表中对应表项
                BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("username", mUsername);
                categoryBmobQuery.findObjects(new FindListener<UserState>() {
                    @Override
                    public void done(List<UserState> object, BmobException e) {
                        if (e == null) {
                            mGroupNumber = object.get(0).getGroupNumber();
                        } else {
                            Toast.makeText(ReleaseExam.this, "查询UserState表失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //获取出题方式
                if (checkBox1.isChecked()) {
                    mChoice = 1;
                } else if (checkBox2.isChecked()) {
                    mChoice = 2;
                } else {
                    mChoice = 3;
                }
                //根据出题方式从选择题表和判断题表中查询出对应的数据
                if (mChoice == 1) {
                    //生成十道选择题
                    getChoice(10);
                } else if (mChoice == 2) {
                    //生成五道选择题五道判断题
                    getChoice(5);
                    getJudge(5);
                } else {
                    //生成十道判断题
                    getJudge(10);
                }
            } else {
                //都未被选中
                Toast.makeText(ReleaseExam.this, "请选择出题方式！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ReleaseExam.this, "请填写测试名称！", Toast.LENGTH_SHORT).show();
        }
    }


    //可以考虑获取试题跟将试题存起来要不要分开
    private void getJudge(final int number) {
        /*
         * 首先查询判断题表
         * 获取表长
         * 从表长数字中随机获取i个数字，生成数组
         * 根据数组取出对应数据依次放到新的判断题表（JudgeForGroup）中
         * */
        BmobQuery<JudgeTable> bmobQuery = new BmobQuery<JudgeTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<JudgeTable>() {
            @Override
            public void done(List<JudgeTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //生成在表长以内的索引值的数组
                    mTableLengthForJudge = object.size();
                    for (int i = 0; i < number ; i++) {
                        mIndex[i]=(int)(Math.random()*mTableLengthForJudge);
                    }
                    for(int i = 0 ; i < number ; i++ ){
                        BmobQuery<JudgeTable> bmobQuery = new BmobQuery<>();
                        bmobQuery.getObject(object.get(mIndex[i]).getObjectId(), new QueryListener<JudgeTable>() {
                            @Override
                            public void done(JudgeTable judgeTable, BmobException e) {
                                if (e == null) {
                                    //往判断题表中写入数据
                                    addToJudgeTable(judgeTable);
                                } else {
                                    Toast.makeText(ReleaseExam.this, "加载失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(ReleaseExam.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToJudgeTable(JudgeTable judgeTable) {
        //新建一个表对象
        JudgeForGroup judgeForGroup = new JudgeForGroup();
        judgeForGroup.setTestName(mTestName);
        judgeForGroup.setBuilderUserName(mUsername);
        judgeForGroup.setGroupNumber(mGroupNumber);
        judgeForGroup.setTitle(judgeTable.getTitle());
        judgeForGroup.setAnswer(judgeTable.getAnswer());
        judgeForGroup.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e!=null){
                    Toast.makeText(ReleaseExam.this,"存储数据失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getChoice(final int number) {
        /*
         * 首先查询选择题表
         * 获取表长
         * 从表长数字中随机获取i个数字，生成数组
         * 根据数组取出对应数据依次放到新的表中
         * */
        BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<ChoiceTable>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<ChoiceTable>() {
            @Override
            public void done(List<ChoiceTable> object, BmobException e) {
                if (e == null) {
                    //此处查询获取到的list列表数据不是按照id进行排序的，需要重新按照id进行排序后再查询
                    //生成在表长以内的索引值的数组
                    mTableLengthForChoice = object.size();
                    for (int i = 0; i < number ; i++) {
                        mIndex[i]=(int)(Math.random()*mTableLengthForChoice);
                    }
                    for(int i = 0 ; i < number ; i++ ){
                        BmobQuery<ChoiceTable> bmobQuery = new BmobQuery<>();
                        bmobQuery.getObject(object.get(mIndex[i]).getObjectId(), new QueryListener<ChoiceTable>() {
                            @Override
                            public void done(ChoiceTable choiceTable, BmobException e) {
                                if (e == null) {
                                    //往选择题表中写入数据
                                    addToChoiceTable(choiceTable);
                                } else {
                                    Toast.makeText(ReleaseExam.this, "加载失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(ReleaseExam.this, "查询ObjectId失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToChoiceTable(ChoiceTable choiceTable){
        //新建一个表对象
        ChoiceForGroup choiceForGroup = new ChoiceForGroup();
        choiceForGroup.setTestName(mTestName);
        choiceForGroup.setBuilderUserName(mUsername);
        choiceForGroup.setGroupNumber(mGroupNumber);
        choiceForGroup.setTitle(choiceTable.getTitle());
        choiceForGroup.setAnswer(choiceTable.getAnswer());
        choiceForGroup.setChoiceA(choiceTable.getChoiceA());
        choiceForGroup.setChoiceB(choiceTable.getChoiceB());
        choiceForGroup.setChoiceC(choiceTable.getChoiceC());
        choiceForGroup.setChoiceD(choiceTable.getChoiceD());
        choiceForGroup.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e!=null){
                    Toast.makeText(ReleaseExam.this,"存储数据失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}