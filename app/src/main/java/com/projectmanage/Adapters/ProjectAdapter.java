package com.projectmanage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectmanage.Models.ProjectObject;
import com.projectmanage.Activities.Projects.Tasks.MainTaskActivity;
import com.projectmanage.R;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectViewHolder> {

    private List<ProjectObject> mData;
    private Context context;

    public ProjectAdapter(List<ProjectObject> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projects, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProjectViewHolder holder, final int position) {
        String projectKey = mData.get(position).getProjectKey();
        String projectName = mData.get(position).getProjectName();
        holder.mProjectName.setText(projectName);


        holder.mProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectName = mData.get(position).getProjectName();
                String projectKey = mData.get(position).getProjectKey();
                Intent i = new Intent(context, MainTaskActivity.class);
                i.putExtra("projectKey", projectKey);
                i.putExtra("projectName", projectName);
                context.startActivity(i);



            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}



