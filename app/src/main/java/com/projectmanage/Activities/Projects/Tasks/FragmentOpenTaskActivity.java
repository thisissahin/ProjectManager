package com.projectmanage.Activities.Projects.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Adapters.TaskAdapter;
import com.projectmanage.Models.TaskObject;
import com.projectmanage.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentOpenTaskActivity extends Fragment implements TaskAdapter.OnTaskListener {

    public FragmentOpenTaskActivity(){}
    private FirebaseAuth mAuth;
    private FloatingActionButton actionButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mTaskAdapter;
    private RecyclerView.LayoutManager mTaskLayoutManager;
    private String projectKey;
    private String projectName;

    DatabaseReference mDatabaseTask;
    private String currentUserId;
    private static final String TAG = "MyTag";

    //TODO On Activity created call every time fix this


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_task,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        Log.d(TAG,"onAcCreated");


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
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
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child("Open");
        }

        mDatabaseTask.keepSynced(true);


        actionButton = v.findViewById(R.id.projectTaskFab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskAdd.class);
                intent.putExtra("projectKey",projectKey);
                intent.putExtra("taskState","Open");
                startActivity(intent);


            }
        });

        mRecyclerView = v.findViewById(R.id.projectTaskRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);


            mTaskLayoutManager = new GridLayoutManager(getActivity(),1);


        mRecyclerView.setLayoutManager(mTaskLayoutManager);
        mTaskAdapter = new TaskAdapter(getDataSetChat(), getActivity(),this);
        mRecyclerView.setAdapter(mTaskAdapter);
        resualtsTask.clear();
        getNoteList();
        mTaskAdapter.notifyDataSetChanged();


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
                    Toast.makeText(getActivity(),"This project is deleted!",Toast.LENGTH_LONG).show();
                    DatabaseReference mDatabaseDelete = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child(projectKey);
                    mDatabaseDelete.removeValue();
                    //finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void moveTaskTo(String taskKey, String toPath, final int position) {
        final DatabaseReference databaseFromPath = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child("Open").child(taskKey);
        final DatabaseReference databaseToPath = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child(toPath).child(taskKey);

        databaseFromPath.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                databaseToPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            databaseFromPath.removeValue();
                            resualtsTask.remove(position);
                            mTaskAdapter.notifyDataSetChanged();

                    }


                });
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



    @Override
    public void onTaskClick(int position) {
        String taskKey = resualtsTask.get(position).getTaskKey();
        String projectKey = resualtsTask.get(position).getProjectKey();
        Intent i = new Intent(getActivity(), TaskInfoActivity.class);
        i.putExtra("taskKey",taskKey);
        i.putExtra("projectKey",projectKey);
        i.putExtra("taskState","Open");
        startActivity(i);
    }

    @Override
    public void onTaskLongClick(final int position, View v) {
        final PopupMenu popupMenu = new PopupMenu(getActivity(),v);
        popupMenu.getMenuInflater().inflate(R.menu.task_popup_menu,popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.moveToOpen).setVisible(false);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String key = resualtsTask.get(position).getTaskKey();
                String projectKey = resualtsTask.get(position).getProjectKey();

                if (item.getItemId() == (R.id.delete)) {

                    DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child("Open").child(key);
                    deletedNote.removeValue();
                    resualtsTask.remove(position);
                }

                if(item.getItemId() == (R.id.moveToActive)){
                    moveTaskTo(key,"Active",position);
                }

                if(item.getItemId() == (R.id.moveToCompleted)){
                    moveTaskTo(key,"Completed",position);
                }


                return true;
            }


        });
    }

}