package com.projectmanage.Projects.Tasks;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

public class MainTaskActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FloatingActionButton actionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTaskAdapter;
    private RecyclerView.LayoutManager mTaskLayoutManager;
    private String projectKey;

    DatabaseReference mDatabaseTask;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                projectKey= null;
            } else {
                projectKey= extras.getString("projectKey");
            }
        } else {
            projectKey= (String) savedInstanceState.getSerializable("projectKey");
        }
        if(projectKey==null){



        }
        else {
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Notes");
        }

        mDatabaseTask.keepSynced(true);

        actionButton = findViewById(R.id.projectTaskFab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTaskActivity.this, TaskAdd.class);
                intent.putExtra("projectKey",projectKey);
                startActivity(intent);

            }
        });

        mRecyclerView = findViewById(R.id.projectTaskRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        int orientation = MainTaskActivity.this.getResources().getConfiguration().orientation;

        if(orientation==1){
            mTaskLayoutManager = new GridLayoutManager(MainTaskActivity.this,2);

        }
        if(orientation==2) {
            mTaskLayoutManager = new GridLayoutManager(MainTaskActivity.this,4);

        }
        mRecyclerView.setLayoutManager(mTaskLayoutManager);
        mTaskAdapter = new TaskAdapter(getDataSetChat(), MainTaskActivity.this);
        mRecyclerView.setAdapter(mTaskAdapter);

        getNoteList();


    }
    private void getNoteList() {
        mDatabaseTask.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String task = null;
                    String date = null;
                    String taskKey = dataSnapshot.getKey();

                    if (dataSnapshot.child("text").getValue() != null) {
                        task = dataSnapshot.child("text").getValue().toString();
                    }

                    if (dataSnapshot.child("date").getValue() != null) {
                        date = dataSnapshot.child("date").getValue().toString();
                    }
                    if (task != null) {

                        TaskObject newMessage = new TaskObject(task, taskKey, date,projectKey);
                        resualtsTask.add(newMessage);
                        mTaskAdapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mTaskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mTaskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private ArrayList<TaskObject> resualtsTask = new ArrayList<>();

    private List<TaskObject> getDataSetChat() {
        return resualtsTask;
    }
}

