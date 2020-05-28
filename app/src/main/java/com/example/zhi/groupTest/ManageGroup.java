package com.example.zhi.groupTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zhi.utils.UserName;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import com.example.zhi.adapter.UserNameAdapter;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ManageGroup extends AppCompatActivity implements View.OnClickListener{
    private List<UserName> userNameList = new ArrayList<>();
    private UserState mUser = new UserState();
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_group);
        //初始化控件
        initView();
        //设置点击响应事件
        setOnClick();
        //获得当前用户
        getUser();
        //获取查询用户的用户名,并对界面赋值渲染
        getUserName();
    }

    private void setOnClick() {
        refresh.setOnClickListener(this);
    }

    private void initView() {
        refresh = findViewById(R.id.refresh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                Intent intent = new Intent(ManageGroup.this, ManageGroup.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void getUser() {
        //给列表赋值
        //查询到state=0，且群组号与当前用户的群组号相同的人的列表，查询到的是对象列表
        BmobUser bmobUser = BmobUser.getCurrentUser(BmobUser.class);
        String username = bmobUser.getUsername();

        BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username",username);
        categoryBmobQuery.findObjects(new FindListener<UserState>() {
            @Override
            public void done(List<UserState> object, BmobException e) {
                if (e == null) {
                    mUser = object.get(0);
                } else {
                    Toast.makeText(ManageGroup.this,"查询用户失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserName(){
        String groupNumber = mUser.getGroupNumber();
        //根据当前对象的群组号和state进行查询
        //查询到state=0，且群组号与当前用户的群组号相同的人的列表，查询到的是对象列表，
        //每一个查询条件都需要New一个BmobQuery对象
        //--and条件1
        BmobQuery<UserState> eq1 = new BmobQuery<UserState>();
        eq1.addWhereEqualTo("groupNumber", groupNumber);
        //--and条件2
        BmobQuery<UserState> eq2 = new BmobQuery<UserState>();
        eq2.addWhereEqualTo("state", 0);
        //最后组装完整的and条件
        List<BmobQuery<UserState>> andQuerys = new ArrayList<BmobQuery<UserState>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        //查询符合整个and条件的人
        BmobQuery<UserState> query = new BmobQuery<UserState>();
        query.and(andQuerys);
        query.findObjects(new FindListener<UserState>() {
            @Override
            public void done(List<UserState> object, BmobException e) {
                if (e == null) {
                    //将查询到的列表对xml进行渲染
                    int count = object.size();
                    for (int i = 0; i < count; i++) {
                        String userName = object.get(i).getUsername();
                        String nickName = object.get(i).getNickname();

                        UserName username = new UserName(userName,nickName);
                        userNameList.add(username);

                        //在此处加载RecycleView
                        RecyclerView recyclerView = findViewById(R.id.manage_group_recycle_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ManageGroup.this);
                        recyclerView.setLayoutManager(layoutManager);
                        UserNameAdapter adapter = new UserNameAdapter(userNameList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(ManageGroup.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
