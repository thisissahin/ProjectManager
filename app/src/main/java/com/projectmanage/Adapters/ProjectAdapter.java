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


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);
        mDatabaseDelete = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child(projectKey);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                holder.mProjectName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!dataSnapshot.exists()){
                            Toast.makeText(context,projectName + " is deleted!",Toast.LENGTH_LONG).show();
                            mDatabaseDelete.removeValue();
                            mData.remove(position);
                            notifyDataSetChanged();
                        }
                        else{
                            Intent i = new Intent(context, MainTaskActivity.class);
                            i.putExtra("projectKey", projectKey);
                            i.putExtra("projectName", projectName);
                            context.startActivity(i);
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    @Override
    public int getItemCount() {
        return mData.size();
    }


}



