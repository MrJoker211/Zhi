package com.example.zhi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zhi.R;
import com.example.zhi.forChapter.FirstChapter;
import com.example.zhi.forChapter.FourthChapter;
import com.example.zhi.forChapter.SecondChapter;
import com.example.zhi.forChapter.ThirdChapter;
import com.example.zhi.test.ChoiceTest;
import com.example.zhi.test.CompetitionTest;
import com.example.zhi.test.GroupTest;
import com.example.zhi.test.JudgeTest;

public class FragmentTest extends Fragment {

    private Button choiceTest;
    private Button judgeTest;
    private Button competitionTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenttest,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //在此方法内做逻辑处理

        //初始化View
        initView();
        //点击按钮进行界面跳转
        onClick();

    }

    private void onClick() {
        //跳转到选择题测试
        choiceTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChoiceTest.class));
            }
        });
        //跳转到判断题测试
        judgeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JudgeTest.class));
            }
        });
        //跳转到竞赛问答
        competitionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CompetitionTest.class));
            }
        });
    }

    private void initView() {
        choiceTest = getActivity().findViewById(R.id.choice_test);
        judgeTest = getActivity().findViewById(R.id.judge_test);
        competitionTest = getActivity().findViewById(R.id.competition_test);
    }
}
