package com.example.zhi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zhi.R;
import com.example.zhi.test.ShowCompetitionRecord;
import com.example.zhi.utils.CompetitionInfo;

import java.util.List;

public class ShowCompetitionRecordAdapter extends RecyclerView.Adapter<ShowCompetitionRecordAdapter.ViewHolder> {

    private List<CompetitionInfo> mCompetitionInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View competitionView;

        TextView username;
        TextView usedTime;
        TextView rightNumber;
        TextView record;

        public ViewHolder(View view) {
            super(view);
            competitionView = view;
            username = view.findViewById(R.id.username);
            usedTime = view.findViewById(R.id.usedTime);
            rightNumber = view.findViewById(R.id.rightNumber);
            record = view.findViewById(R.id.record);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_competition_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompetitionInfo competitionInfo = mCompetitionInfoList.get(position);

        holder.username.setText(competitionInfo.getUsername());
        holder.usedTime.setText(String.format("%d", competitionInfo.getUsedTime()));
        holder.rightNumber.setText(String.format("%d", competitionInfo.getRightNumber()));
        holder.record.setText(String.format("%d", competitionInfo.getRecord()));
    }

    @Override
    public int getItemCount() {
        return mCompetitionInfoList.size();
    }

    public ShowCompetitionRecordAdapter(List<CompetitionInfo> competitionInfoList){
        mCompetitionInfoList = competitionInfoList;
    }
}
