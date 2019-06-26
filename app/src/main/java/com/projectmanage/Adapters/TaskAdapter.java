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

        //holder.mTaskDate.setText(taskList.get(position).getDate());
        holder.mTaskTitle.setText(taskList.get(position).getTitle());
        /*

        holder.mTaskTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == (R.id.delete)){
                            String key = taskList.get(position).getTaskKey();
                            String projectKey = taskList.get(position).getProjectKey();
                            delete(key,projectKey);
                            taskList.remove(position);
                        }


                        return true;
                    }

                });

                return true;
            }

        });
*/

    }
    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTaskTitle;
        OnTaskListener onTaskListener;

        public TaskViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);

            mTaskTitle = itemView.findViewById(R.id.projectTaskTitle);
            this.onTaskListener = onTaskListener;
            mTaskTitle.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            onTaskListener.onTaskClick(getAdapterPosition());
        }
    }

    public interface OnTaskListener{
        void onTaskClick(int position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void delete(String key,String projectKey){
        DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child(key);
        deletedNote.removeValue();

    }


}

