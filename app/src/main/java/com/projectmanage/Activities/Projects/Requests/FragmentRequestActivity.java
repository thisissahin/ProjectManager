package com.projectmanage.Activities.Projects.Requests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.projectmanage.Adapters.RequestAdapter;
import com.projectmanage.Models.RequestObject;
import com.projectmanage.R;

import java.util.ArrayList;

public class FragmentRequestActivity extends Fragment {
    DatabaseReference mDatabase;
    RequestAdapter adapter;
    ArrayList<RequestObject> requests = new ArrayList<>();
    private String currentUserId;
    private String requestFrom;
    private String projectKey;
    private String projectName;

    public FragmentRequestActivity() {
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_request,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Requests");

        RecyclerView recyclerView = v.findViewById(R.id.rvRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RequestAdapter(requests, getActivity());
        recyclerView.setAdapter(adapter);
        requests.clear();
        getRequestList();
        adapter.notifyDataSetChanged();


    }

    private void getRequestList(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {

                        projectKey = data.getKey();
                        requestFrom = data.child("requestFrom").getValue().toString();
                        projectName = data.child("projectName").getValue().toString();
                        RequestObject newMessage = new RequestObject(projectKey,requestFrom,projectName);

                        requests.add(newMessage);
                        adapter.notifyDataSetChanged();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






}
