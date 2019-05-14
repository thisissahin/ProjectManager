package com.projectmanage.Projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Notes.MainNoteActivity;
import com.projectmanage.Notes.NoteAdd;
import com.projectmanage.R;

import java.util.ArrayList;

public class FragmentProjectActivity extends Fragment {
    DatabaseReference mDatabaseProject;
    DatabaseReference mDatabaseProjectName;
    ProjectAdapter adapter;
    ArrayList<ProjectObject> userNames = new ArrayList<>();
    private String currentUserId;
    private String projectName;
    String projectKey;
    FloatingActionButton actionButton;

    public FragmentProjectActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_projects,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseProject = FirebaseDatabase.getInstance().getReference();
        mDatabaseProjectName =  mDatabaseProject.child("Users").child(currentUserId).child("Projects");

        actionButton = v.findViewById(R.id.projectFab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectAdd.class);
                startActivity(intent);

            }
        });


        RecyclerView recyclerView = v.findViewById(R.id.rvUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProjectAdapter(userNames, getActivity());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_create){

            Intent i = new Intent(getActivity(),ProjectAdd.class);

            startActivity(i);

        }

        return super.onOptionsItemSelected(item);

    }
}
