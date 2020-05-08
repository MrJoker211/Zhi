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
import com.example.zhi.activity.Login;
import com.example.zhi.forChapter.FifthChapter;
import com.example.zhi.forChapter.FirstChapter;
import com.example.zhi.forChapter.FourthChapter;
import com.example.zhi.forChapter.SecondChapter;
import com.example.zhi.forChapter.ThirdChapter;
import com.example.zhi.forQuestionType.Choice;
import com.example.zhi.forQuestionType.Judge;

import cn.bmob.v3.BmobUser;

//此处可以直接继承基类BaseFragment
//
public class FragmentHome extends Fragment {

    private Button firstChapter;
    private Button secondChapter;
    private Button thirdChapter;
    private Button fourthChapter;
    private Button fifChapter;

    private Button judgePractice;
    private Button choicePractice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenthome,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //逻辑处理
        initView();

        //按钮响应事件
        onClick();

    }

    private void onClick() {
        //跳转到第一章
        firstChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FirstChapter.class));
            }
        });
        //跳转到第二章
        secondChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SecondChapter.class));
            }
        });
        //跳转到第三章
        thirdChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThirdChapter.class));
            }
        });
        //跳转到第四章
        fourthChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FourthChapter.class));
            }
        });
        //跳转到第五章
        fifChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FifthChapter.class));
            }
        });


        //跳转到判断题练习
        judgePractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Judge.class));
            }
        });
        //跳转到选择题练习
        choicePractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Choice.class));
            }
        });
    }


    private void initView() {
        firstChapter = getActivity().findViewById(R.id.first_chapter);
        secondChapter = getActivity().findViewById(R.id.second_chapter);
        thirdChapter = getActivity().findViewById(R.id.third_chapter);
        fourthChapter = getActivity().findViewById(R.id.fourth_chapter);
        fifChapter = getActivity().findViewById(R.id.fifth_chapter);

        judgePractice = getActivity().findViewById(R.id.judge_practice);
        choicePractice = getActivity().findViewById(R.id.choice_practice);
    }
}
