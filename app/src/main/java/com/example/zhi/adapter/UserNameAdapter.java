package com.example.zhi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zhi.utils.UserName;
import com.example.zhi.Bean.UserState;
import com.example.zhi.R;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserNameAdapter extends RecyclerView.Adapter<UserNameAdapter.ViewHolder> {
    private List<UserName> mUserNameList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View userNameView;
        TextView userName;
        TextView nickName;
        Button agree;
        Button refuse;

        public ViewHolder(View view) {
            super(view);
            userNameView = view;

            userName = view.findViewById(R.id.username);
            nickName = view.findViewById(R.id.nickname);

            agree = view.findViewById(R.id.agree);
            refuse = view.findViewById(R.id.refuse);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int position = holder.getAdapterPosition();
                UserName username = mUserNameList.get(position);
                //在此处处理点击事件
                //获取位置弹出提示
                //首先获取当前textView中的内容，根据其查询出相应用户，然后将其state改为1
                BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("username", username.getUsername());
                categoryBmobQuery.findObjects(new FindListener<UserState>() {
                    @Override
                    public void done(List<UserState> object, BmobException e) {
                        if (e == null) {
                            object.get(0).setState(1);
                            object.get(0).update(object.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(view.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(view.getContext(), "修改出错！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(view.getContext(), "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int position = holder.getAdapterPosition();
                UserName username = mUserNameList.get(position);
                //首先获取当前textView中的内容，根据其查询出相应用户，然后将其state改为2,并清除群组号

                BmobQuery<UserState> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("username", username.getUsername());
                categoryBmobQuery.findObjects(new FindListener<UserState>() {
                    @Override
                    public void done(List<UserState> object, BmobException e) {
                        if (e == null) {
                            object.get(0).setState(2);
                            object.get(0).setGroupNumber("");
                            object.get(0).update(object.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(view.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(view.getContext(), "修改出错！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(view.getContext(), "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserName username = mUserNameList.get(position);
        holder.userName.setText(username.getUsername());
        holder.nickName.setText(username.getNickname());
    }

    @Override
    public int getItemCount() {
        return mUserNameList.size();
    }

    public UserNameAdapter(List<UserName> userNameList) {
        mUserNameList = userNameList;
    }

}
