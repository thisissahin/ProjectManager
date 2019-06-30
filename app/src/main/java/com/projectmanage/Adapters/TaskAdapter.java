package com.projectmanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Models.TaskObject;
import com.projectmanage.R;

import java.util.List;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskObject> taskList;
    private Context context;
    private String currentUserId;
    private OnTaskListener onTaskListener;


    public TaskAdapter(List<TaskObject> taskList, Context context,OnTaskListener onTaskListener) {
        this.taskList = taskList;
        this.context = context;
        this.onTaskListener = onTaskListener;
    }



    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_task,null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        TaskViewHolder rcv = new TaskViewHolder(layoutView,onTaskListener);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {

        holder.mTaskTitle.setText(taskList.get(position).getTitle());


    }
    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mTaskTitle;
        OnTaskListener onTaskListener;

        public TaskViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);

            mTaskTitle = itemView.findViewById(R.id.projectTaskTitle);
            this.onTaskListener = onTaskListener;
            mTaskTitle.setOnClickListener(this);
            mTaskTitle.setOnLongClickListener(this);


        }



        @Override
        public void onClick(View view) {
            onTaskListener.onTaskClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onTaskListener.onTaskLongClick(getAdapterPosition(),view);
            return true;
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public interface OnTaskListener{
        void onTaskClick(int position);
        void onTaskLongClick(int position,View v);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }




}

