package com.projectmanage.Projects.Tasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projectmanage.R;

class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTaskText;
    public TextView mTaskDate;

    public TaskViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mTaskText = itemView.findViewById(R.id.projectTaskText);
        mTaskDate = itemView.findViewById(R.id.projectTaskDate);


    }

    @Override
    public void onClick(View v) {

    }
}

