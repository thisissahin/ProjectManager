package com.projectmanage.Notes;

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
import com.projectmanage.MainActivity;
import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

public class MainNoteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FloatingActionButton actionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mNoteAdapter;
    private RecyclerView.LayoutManager mNoteLayoutManager;
    private String key;

    DatabaseReference mDatabaseNote;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes");



        mDatabaseNote.keepSynced(true);

        actionButton = findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNoteActivity.this, NoteAdd.class);
                startActivity(intent);

            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        int orientation = MainNoteActivity.this.getResources().getConfiguration().orientation;

        if(orientation==1){
            mNoteLayoutManager = new GridLayoutManager(MainNoteActivity.this,2);

        }
        if(orientation==2) {
            mNoteLayoutManager = new GridLayoutManager(MainNoteActivity.this,4);

        }
        mRecyclerView.setLayoutManager(mNoteLayoutManager);
        mNoteAdapter = new NoteAdapter(getDataSetChat(), MainNoteActivity.this);
        mRecyclerView.setAdapter(mNoteAdapter);

        getNoteList();


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

                        TaskObject newMessage = new TaskObject(note, key, date);
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



    private ArrayList<TaskObject> resualtsNote = new ArrayList<>();

    private List<TaskObject> getDataSetChat() {
        return resualtsNote;
    }
}
