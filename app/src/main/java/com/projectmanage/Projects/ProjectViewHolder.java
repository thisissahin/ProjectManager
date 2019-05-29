package com.projectmanage.Projects;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.projectmanage.R;


public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mProjectName;

    public ProjectViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mProjectName = itemView.findViewById(R.id.userText);


    }

    @Override
    public void onClick(View v) {

    }
}
