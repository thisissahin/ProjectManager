package com.projectmanage.Activities.Projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Adapters.ProjectAdapter;
import com.projectmanage.Models.ProjectObject;
import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class FragmentProjectActivity extends Fragment {

    DatabaseReference mDatabaseProject;
    ProjectAdapter adapter;
    private String currentUserId;
    private String projectKey;
    private String projectName;
    FloatingActionButton actionButton;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;

    public FragmentProjectActivity() {
    }

    private static final String TAG = "MyProjectTag";




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_projects,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onAcCreated");
        View v = getView();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        actionButton = v.findViewById(R.id.projectFab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectAdd.class);
                intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        recyclerView = v.findViewById(R.id.rvUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ProjectAdapter(getDataSetChat(), getActivity());
        recyclerView.setAdapter(adapter);
        mLayoutManager =  new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),((LinearLayoutManager) mLayoutManager).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        updateUi();

    }


    private void getProjectList(){

        mDatabaseProject = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects");
        mDatabaseProject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectNames.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        projectKey = data.getKey();
                        if(projectKey !=null){
                            projectName = data.child("projectName").getValue().toString();
                            ProjectObject newMessage = new ProjectObject(projectKey,projectName);
                            projectNames.add(newMessage);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<ProjectObject> projectNames = new ArrayList<>();

    private List<ProjectObject> getDataSetChat() {
        return projectNames;
    }

    public void updateUi(){
        projectNames.clear();
        getProjectList();
        adapter.notifyDataSetChanged();
    }

}
