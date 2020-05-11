package com.example.zhi.Fragment;

import android.annotation.SuppressLint;
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
import com.example.zhi.R;
import com.example.zhi.activity.Login;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FragmentMine extends Fragment {


    private TextView username,nickname,groupNumber;
    private Button loginOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentmine,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //逻辑处理


        initView();
        //加载我的信息
        getMyinfo();

        //退出登录
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //并不是真正意义的退出，让用户信息不再记录到App中
                //然后用finish结束活动  才算退出
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });

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
                    username.setText(user.getUsername());
                    nickname.setText(user.getNickname());
                    groupNumber.setText(user.getGroupNumber());
                }else {
                    Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        username = getActivity().findViewById(R.id.username);
        nickname = getActivity().findViewById(R.id.nickname);
        groupNumber = getActivity().findViewById(R.id.group_number);

        loginOut = getActivity().findViewById(R.id.login_out);



    }
}
