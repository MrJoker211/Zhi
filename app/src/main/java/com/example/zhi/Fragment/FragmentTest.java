package com.example.zhi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zhi.Bean.User;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.groupTest.ExamRecord;
import com.example.zhi.groupTest.JoinExam;
import com.example.zhi.groupTest.JoinGroup;
import com.example.zhi.groupTest.ManageGroup;
import com.example.zhi.groupTest.ReleaseExam;
import com.example.zhi.test.ChoiceTest;
import com.example.zhi.test.CompetitionTest;
import com.example.zhi.test.JudgeTest;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FragmentTest extends Fragment implements View.OnClickListener {

    private Button choiceTest;
    private Button judgeTest;
    private Button competitionTest;

    private Button manageGroup;
    private Button releaseExam;
    private Button examRecord;
    private Button joinGroup;
    private Button joinExam;

    private int mIsCommittee = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenttest,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化View
        initView();
        //加载我的信息
        getMyinfo();
        //点击按钮进行界面跳转
        setClick();
    }

    private void setClick() {
        choiceTest.setOnClickListener(this);
        judgeTest.setOnClickListener(this);
        competitionTest.setOnClickListener(this);

        manageGroup.setOnClickListener(this);
        releaseExam.setOnClickListener(this);
        examRecord.setOnClickListener(this);
        joinGroup.setOnClickListener(this);
        joinExam.setOnClickListener(this);
    }

    private void getMyinfo() {
        //加载个人信息,通过父类的函数获取当前的对象
        BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
        String Id = bmobUser.getObjectId();
        //通过封装的函数进行查询
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(Id, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    BmobQuery<UserState> userStateBmobQuery = new BmobQuery<>();
                    userStateBmobQuery.addWhereEqualTo("username", user.getUsername());
                    userStateBmobQuery.findObjects(new FindListener<UserState>() {
                        @Override
                        public void done(List<UserState> object, BmobException e) {
                            if (e == null) {
                                mIsCommittee = object.get(0).getIsCommittee().intValue();
                                if(mIsCommittee == 1){
                                    //是委员，显示前三个
                                    manageGroup.setVisibility(View.VISIBLE);
                                    releaseExam.setVisibility(View.VISIBLE);
                                    examRecord.setVisibility(View.VISIBLE);
                                }else{
                                    //不是委员显示后两个
                                    joinGroup.setVisibility(View.VISIBLE);
                                    joinExam.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(getActivity(), "加载userState失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "加载user失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        choiceTest = getActivity().findViewById(R.id.choice_test);
        judgeTest = getActivity().findViewById(R.id.judge_test);
        competitionTest = getActivity().findViewById(R.id.competition_test);

        manageGroup = getActivity().findViewById(R.id.manage_group);
        releaseExam = getActivity().findViewById(R.id.release_exam);
        examRecord = getActivity().findViewById(R.id.exam_record);

        joinGroup = getActivity().findViewById(R.id.join_group);
        joinExam = getActivity().findViewById(R.id.join_exam);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choice_test:
                startActivity(new Intent(getActivity(), ChoiceTest.class));
                break;
            case R.id.judge_test:
                startActivity(new Intent(getActivity(), JudgeTest.class));
                break;
            case R.id.competition_test:
                startActivity(new Intent(getActivity(), CompetitionTest.class));
                break;
            case R.id.manage_group:
                if(mIsCommittee == 1){
                    startActivity(new Intent(getActivity(), ManageGroup.class));
                }else {
                    Toast.makeText(getActivity(), "您不是委员，没有操作权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.release_exam:
                if(mIsCommittee == 1){
                    startActivity(new Intent(getActivity(), ReleaseExam.class));
                }else {
                    Toast.makeText(getActivity(), "您不是委员，没有操作权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exam_record:
                if(mIsCommittee == 1){
                    startActivity(new Intent(getActivity(), ExamRecord.class));
                }else {
                    Toast.makeText(getActivity(), "您不是委员，没有操作权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.join_group:
                if(mIsCommittee == 0){
                    startActivity(new Intent(getActivity(), JoinGroup.class));
                }else {
                    Toast.makeText(getActivity(), "您是委员，不用加入群组！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.join_exam:
                if(mIsCommittee == 0){
                    startActivity(new Intent(getActivity(), JoinExam.class));
                }else {
                    Toast.makeText(getActivity(), "您是委员，不可参加考试！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
