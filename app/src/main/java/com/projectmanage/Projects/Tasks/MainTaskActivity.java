package com.projectmanage.Projects.Tasks;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Projects.ProjectAddUser;
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
    private String projectName;

    DatabaseReference mDatabaseTask;
    private String currentUserId;

    @Override
    protected void onStart() {
        super.onStart();
        resualtsTask.clear();
        getNoteList();
        checkProjectExist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                projectKey= null;
                projectName= null;
            } else {
                projectKey= extras.getString("projectKey");
                projectName= extras.getString("projectName");
            }
        } else {
            projectKey= (String) savedInstanceState.getSerializable("projectKey");
            projectName= (String) savedInstanceState.getSerializable("projectName");
        }
        if(projectKey==null){



        }
        else {
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks");
        }

        mDatabaseTask.keepSynced(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(projectName);

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
            mTaskLayoutManager = new GridLayoutManager(MainTaskActivity.this,1);

        }
        if(orientation==2) {
            mTaskLayoutManager = new GridLayoutManager(MainTaskActivity.this,4);

        }
        mRecyclerView.setLayoutManager(mTaskLayoutManager);
        mTaskAdapter = new TaskAdapter(getDataSetChat(), MainTaskActivity.this);
        mRecyclerView.setAdapter(mTaskAdapter);


    }
    private void getNoteList() {
        mDatabaseTask.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    String title = null;
                    String date = null;
                    String taskKey = dataSnapshot.getKey();

                    if (dataSnapshot.child("title").getValue() != null) {
                        title = dataSnapshot.child("title").getValue().toString();
                    }

                    if (dataSnapshot.child("date").getValue() != null) {
                        date = dataSnapshot.child("date").getValue().toString();
                    }



                    if (title != null) {

                        TaskObject newMessage = new TaskObject(title, taskKey, date,projectKey,projectName);
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




    private void checkProjectExist(){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(MainTaskActivity.this,"This project is deleted!",Toast.LENGTH_LONG).show();
                    DatabaseReference mDatabaseDelete = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child(projectKey);
                    mDatabaseDelete.removeValue();
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);
                deletedNote.removeValue();
                deletedNote = FirebaseDatabase.getInstance().getReference().child("Users").
                        child(currentUserId).child("Projects").child(projectKey);
                deletedNote.removeValue();
                finish();
                return true;
            case R.id.memberAdd:
                Intent i = new Intent(MainTaskActivity.this, ProjectAddUser.class);
                i.putExtra("projectKey",projectKey);
                i.putExtra("projectName",projectName);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_menu, menu);
        return true;
    }



    private ArrayList<TaskObject> resualtsTask = new ArrayList<>();

    private List<TaskObject> getDataSetChat() {
        return resualtsTask;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

