package com.projectmanage.Projects;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Projects.Tasks.MainTaskActivity;
import com.projectmanage.R;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private ArrayList<ProjectObject> mData;
    private Context context;

    public ProjectAdapter(ArrayList<ProjectObject> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projects, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String projectKey = mData.get(position).getProjectKey();

        DatabaseReference mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("name");
        mDatabaseNote.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = null;
                if (dataSnapshot.exists()){
                    name = dataSnapshot.getValue().toString();
                    holder.myTextView.setText(name);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectKey = mData.get(position).getProjectKey();
                Toast.makeText(context,projectKey,Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MainTaskActivity.class);
                i.putExtra("projectKey",projectKey);
                context.startActivity(i);

            }
        });
        holder.myTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String projectKey = mData.get(position).getProjectKey();
                Intent i = new Intent(context,ProjectAddUser.class);
                i.putExtra("projectKey",projectKey);
                context.startActivity(i);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);


            myTextView = itemView.findViewById(R.id.userText);

        }


    }




}
