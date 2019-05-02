package com.projectmanage.Projects;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.MainActivity;
import com.projectmanage.R;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {
    DatabaseReference mDatabaseProject;
    DatabaseReference mDatabaseProjectName;
    ProjectAdapter adapter;
    ArrayList<ProjectObject> userNames = new ArrayList<>();
    private String currentUserId;
    private String projectName;
    String projectKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseProject = FirebaseDatabase.getInstance().getReference();
        mDatabaseProjectName =  mDatabaseProject.child("Users").child(currentUserId).child("Projects");



        RecyclerView recyclerView = findViewById(R.id.rvUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjectAdapter(userNames, ProjectActivity.this);
        recyclerView.setAdapter(adapter);
        getProjectList();

    }
    private void getProjectList() {
        mDatabaseProjectName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    projectKey = dataSnapshot.getKey();

                    ProjectObject newMessage = new ProjectObject(projectKey);

                    userNames.add(newMessage);
                    adapter.notifyDataSetChanged();


                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_create){

            Intent i = new Intent(this,ProjectAdd.class);

            startActivity(i);

        }

            return super.onOptionsItemSelected(item);

    }

}
