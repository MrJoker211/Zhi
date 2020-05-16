package com.example.zhi.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zhi.Bean.User;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.activity.Login;
import com.example.zhi.groupTest.ExamRecord;
import com.example.zhi.groupTest.JoinExam;
import com.example.zhi.groupTest.JoinGroup;
import com.example.zhi.groupTest.ManageGroup;
import com.example.zhi.groupTest.ReleaseExam;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FragmentMine extends Fragment implements View.OnClickListener {


    private TextView username;
    private TextView nickname;
    private TextView groupNumber;

    private Button loginOut;
    private Button manageGroup;
    private Button releaseExam;
    private Button examRecord;
    private Button joinGroup;
    private Button joinExam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentmine,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件
        initView();
        //加载我的信息
        getMyinfo();

        //设置监听点击事件
        setClick();

    }

    private void setClick() {
        loginOut.setOnClickListener(this);
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
                if(e == null){
                    BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                    categoryBmobQuery.addWhereEqualTo("username", user.getUsername());
                    categoryBmobQuery.findObjects(new FindListener<UserState>() {
                        @Override
                        public void done(List<UserState> object, BmobException e) {
                            if (e == null) {
                                username.setText(object.get(0).getUsername());
                                nickname.setText(object.get(0).getNickname());
                                //只有申请加入群组且被同意以后才有群组号
                                if(object.get(0).getState().intValue() == 1){
                                    groupNumber.setText(object.get(0).getGroupNumber());
                                }
                            } else {
                                Toast.makeText(getActivity(),"加载userState失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"加载user失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        username = getActivity().findViewById(R.id.username);
        nickname = getActivity().findViewById(R.id.nickname);
        groupNumber = getActivity().findViewById(R.id.group_number);

        loginOut = getActivity().findViewById(R.id.login_out);
        manageGroup = getActivity().findViewById(R.id.manage_group);
        releaseExam = getActivity().findViewById(R.id.release_exam);
        examRecord = getActivity().findViewById(R.id.exam_record);
        joinGroup = getActivity().findViewById(R.id.join_group);
        joinExam = getActivity().findViewById(R.id.join_exam);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_out:
                //并不是真正意义的退出，让用户信息不再记录到App中
                //然后用finish结束活动  才算退出
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
                break;
            case R.id.manage_group:
                startActivity(new Intent(getActivity(), ManageGroup.class));
                break;
            case R.id.release_exam:
                startActivity(new Intent(getActivity(), ReleaseExam.class));
                break;
            case R.id.exam_record:
                startActivity(new Intent(getActivity(), ExamRecord.class));
                break;
            case R.id.join_group:
                startActivity(new Intent(getActivity(), JoinGroup.class));
                break;
            case R.id.join_exam:
                startActivity(new Intent(getActivity(), JoinExam.class));
                break;
        }
    }
}
