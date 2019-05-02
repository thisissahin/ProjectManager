package com.projectmanage.Projects.Requests;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.projectmanage.R;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    RequestAdapter adapter;
    ArrayList<RequestObject> requests = new ArrayList<>();
    private String currentUserId;
    private String requestFrom;
    private String projectKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child("Requests");



        RecyclerView recyclerView = findViewById(R.id.rvRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAdapter(requests, RequestActivity.this);
        recyclerView.setAdapter(adapter);
        getProjectList();

    }
    private void getProjectList() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    projectKey = dataSnapshot.getKey();
                    requestFrom = dataSnapshot.child("requestFrom").getValue().toString();
                    RequestObject newMessage = new RequestObject(projectKey,requestFrom);

                    requests.add(newMessage);
                    adapter.notifyDataSetChanged();


                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }


}
