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
public class FragmentHome extends Fragment implements View.OnClickListener{

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

        //控件初始化
        initView();

        //设置点击事件
        setClick();

    }

    private void setClick() {
        firstChapter.setOnClickListener(this);
        secondChapter.setOnClickListener(this);
        thirdChapter.setOnClickListener(this);
        fourthChapter.setOnClickListener(this);
        fifChapter.setOnClickListener(this);

        judgePractice.setOnClickListener(this);
        choicePractice.setOnClickListener(this);
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

    //点击事件的响应
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first_chapter:
                startActivity(new Intent(getActivity(), FirstChapter.class));
                break;
            case R.id.second_chapter:
                startActivity(new Intent(getActivity(), SecondChapter.class));
                break;
            case R.id.third_chapter:
                startActivity(new Intent(getActivity(), ThirdChapter.class));
                break;
            case R.id.fourth_chapter:
                startActivity(new Intent(getActivity(), FourthChapter.class));
                break;
            case R.id.fifth_chapter:
                startActivity(new Intent(getActivity(), FifthChapter.class));
                break;
            case R.id.judge_practice:
                startActivity(new Intent(getActivity(), Judge.class));
                break;
            case R.id.choice_practice:
                startActivity(new Intent(getActivity(), Choice.class));
                break;
        }
    }
}
