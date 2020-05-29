package com.example.zhi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zhi.R;
import com.example.zhi.utils.RecordInfo;
import java.util.List;

public class ShowRecordAdapter extends RecyclerView.Adapter<ShowRecordAdapter.ViewHolder> {

    private List<RecordInfo> mRecordInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View recordView;
        TextView userName;
        TextView studentName;
        TextView record;

        public ViewHolder(View view) {
            super(view);
            recordView = view;

            userName = view.findViewById(R.id.username);
            studentName = view.findViewById(R.id.student_name);
            record = view.findViewById(R.id.record);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordInfo recordInfo = mRecordInfoList.get(position);

        holder.userName.setText(recordInfo.getUsername());
        holder.studentName.setText(recordInfo.getStudentName());
        holder.record.setText(String.format("%d", recordInfo.getRecord()));
    }


    @Override
    public int getItemCount() {
        return mRecordInfoList.size();
    }

    public ShowRecordAdapter(List<RecordInfo> recordInfoList) {
        mRecordInfoList = recordInfoList;
    }

}