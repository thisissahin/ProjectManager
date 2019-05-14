package com.projectmanage.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentNoteActivity extends Fragment {
    private FirebaseAuth mAuth;
    private FloatingActionButton actionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mNoteAdapter;
    private RecyclerView.LayoutManager mNoteLayoutManager;
    private String key;

    DatabaseReference mDatabaseNote;
    private String currentUserId;

    public FragmentNoteActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_note,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes");



        mDatabaseNote.keepSynced(true);

        actionButton = v.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteAdd.class);
                startActivity(intent);

            }
        });

        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        int orientation = getActivity().getResources().getConfiguration().orientation;

        if(orientation==1){
            mNoteLayoutManager = new GridLayoutManager(getActivity(),2);

        }
        if(orientation==2) {
            mNoteLayoutManager = new GridLayoutManager(getActivity(),4);

        }
        mRecyclerView.setLayoutManager(mNoteLayoutManager);
        mNoteAdapter = new NoteAdapter(getDataSetChat(), getActivity());
        mRecyclerView.setAdapter(mNoteAdapter);

        resualtsNote.clear();
        getNoteList();
        mNoteAdapter.notifyDataSetChanged();


    }
    private void getNoteList() {
        mDatabaseNote.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String note = null;
                    String date = null;
                    String key = dataSnapshot.getKey();

                    if (dataSnapshot.child("text").getValue() != null) {
                        note = dataSnapshot.child("text").getValue().toString();
                    }

                    if (dataSnapshot.child("date").getValue() != null) {
                        date = dataSnapshot.child("date").getValue().toString();
                    }
                    if (note != null) {

                        NoteObject newMessage = new NoteObject(note, key, date);
                        resualtsNote.add(newMessage);
                        mNoteAdapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mNoteAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mNoteAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private ArrayList<NoteObject> resualtsNote = new ArrayList<>();

    private List<NoteObject> getDataSetChat() {
        return resualtsNote;
    }
}
