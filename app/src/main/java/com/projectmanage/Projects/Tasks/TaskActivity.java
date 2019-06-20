package com.projectmanage.Projects.Tasks;





import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.R;
import com.projectmanage.Settings.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TaskActivity extends AppCompatActivity {
    DatabaseReference mDatabaseTask;
    DatabaseReference mDatabaseCheckList;

    private EditText taskNoteEdit;
    private EditText taskTitleEdit;
    private ImageView checkListButton;
    private EditText taskCheckListEdit;
    private String taskKey;
    private String projectKey;
    private String currentUserId;
    SharedPreferencesManager mSharedPrefManager;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    CheckListAdapter adapter;
    private ArrayList<CheckListObject> checkListTexts = new ArrayList<>();
    private ArrayList<String> checkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                taskKey= null;
                projectKey= null;
            } else {
                taskKey= extras.getString("taskKey");
                projectKey= extras.getString("projectKey");
            }
        } else {
            taskKey= (String) savedInstanceState.getSerializable("taskKey");
            projectKey= (String) savedInstanceState.getSerializable("projectKey");
        }

        mSharedPrefManager = new SharedPreferencesManager(this);

        taskNoteEdit = findViewById(R.id.taskNoteEditText);
        taskTitleEdit = findViewById(R.id.taskTitleEditText);
       // taskNoteEdit.setTextSize(mSharedPrefManager.getVariable());
        checkListButton = findViewById(R.id.taskCheckImage);
        taskCheckListEdit = findViewById(R.id.taskCheckListEdit);

        setRecyclerView();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks");
        mDatabaseCheckList = mDatabaseTask.child(taskKey).child("checkList");

        checkListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkListText = taskCheckListEdit.getText().toString();
                taskCheckListEdit.setText("");
                if(!checkListText.isEmpty()) {
                    DatabaseReference pushdata = mDatabaseCheckList.push();
                    Map newCheck = new HashMap();
                    newCheck.put("text",checkListText);
                    newCheck.put("checkBox",false);
                    pushdata.setValue(newCheck);
                    newCheck.clear();
                }
            }
        });



        mDatabaseTask.child(taskKey).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String note = dataSnapshot.child("note").getValue().toString();
                String title = dataSnapshot.child("title").getValue().toString();

                taskTitleEdit.setText(title);
                taskNoteEdit.setText(note);

                getCheckList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void getCheckList(){
        mDatabaseCheckList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    final String text;
                    final String key = dataSnapshot.getKey();
                    final Boolean checkBoxBool;

                    if (dataSnapshot.child("text").getValue() != null) {
                        checkBoxBool = (Boolean) dataSnapshot.child("checkBox").getValue();
                        text = dataSnapshot.child("text").getValue().toString();

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                CheckListObject newMessage = new CheckListObject(text, checkBoxBool, key );
                                checkListTexts.add(0, newMessage);
                                adapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(0);
                            }
                        });
                    }





                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            //adapter.updateList(projectKey,taskKey);
            finish();
            return true;
        }

        if (id == R.id.action_done) {

            String note = taskNoteEdit.getText().toString();
            String title = taskTitleEdit.getText().toString();
            mDatabaseTask.child(taskKey).child("note").setValue(note);
            mDatabaseTask.child(taskKey).child("title").setValue(title);
            adapter.updateList(projectKey,taskKey);
            finish();
        }

        else if(id == R.id.action_delete){
            DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").
                    child(projectKey).child("Tasks").child(taskKey);
            deletedNote.removeValue();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //adapter.updateList(projectKey,taskKey);
        finish();
    }

    public void setRecyclerView(){

        recyclerView = findViewById(R.id.taskCheckListRcy);
        mLayoutManager =  new LinearLayoutManager(TaskActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CheckListAdapter(checkListTexts, TaskActivity.this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),((LinearLayoutManager) mLayoutManager).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

    }




}


