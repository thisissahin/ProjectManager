package com.projectmanage.Projects.Tasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projectmanage.R;

class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTaskTitle;

    public TaskViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mTaskTitle = itemView.findViewById(R.id.projectTaskTitle);


    }

    @Override
    public void onClick(View v) {

    }
}

