package com.projectmanage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Models.ProjectObject;
import com.projectmanage.Activities.Projects.Tasks.MainTaskActivity;
import com.projectmanage.R;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectViewHolder> {

    private List<ProjectObject> mData;
    private Context context;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseDelete;

    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        final String projectKey = mData.get(position).getProjectKey();
        final String projectName = mData.get(position).getProjectName();
        holder.mProjectName.setText(projectName);
        holder.mProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MainTaskActivity.class);
                i.putExtra("projectKey", projectKey);
                i.putExtra("projectName", projectName);
                i.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(i);
            }

        });

    }






    @Override
    public int getItemCount() {
        return mData.size();
    }


}



