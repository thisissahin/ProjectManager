package com.projectmanage.Projects.Tasks;





import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.R;
import com.projectmanage.Settings.SharedPreferencesManager;


public class TaskActivity extends AppCompatActivity {
    DatabaseReference mDatabaseTask;
    private String currentUserId;
    private EditText taskUpdateEdit;
    private Boolean editFocusable;
    private String taskKey;
    private String projectKey;
    SharedPreferencesManager mSharedPrefManager;

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

        taskUpdateEdit = findViewById(R.id.taskEditText);
        editFocusable = false;
        taskUpdateEdit.setFocusable(false);
        taskUpdateEdit.setFocusableInTouchMode(false);
        taskUpdateEdit.setClickable(false);
        taskUpdateEdit.setTextSize(mSharedPrefManager.getVariable());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks");

        mDatabaseTask.child(taskKey).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.child("note").getValue().toString();
                taskUpdateEdit.setText(data);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_edit && editFocusable == false) {
            editFocusable = true;


            taskUpdateEdit.setFocusable(true);
            taskUpdateEdit.setFocusableInTouchMode(true);
            taskUpdateEdit.setClickable(true);
            taskUpdateEdit.setShowSoftInputOnFocus(true);
            item.setIcon(R.drawable.baseline_done_white_18dp);
            taskUpdateEdit.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);






        }
        else if(id == R.id.action_edit && editFocusable == true){
            String text = taskUpdateEdit.getText().toString();

            mDatabaseTask.child(taskKey).child("note").setValue(text);

            item.setIcon(R.drawable.baseline_edit_white_18dp);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            editFocusable = false;
            taskUpdateEdit.setFocusable(false);
            taskUpdateEdit.setFocusableInTouchMode(false);
            taskUpdateEdit.setClickable(false);

        }

        else if(id == R.id.action_delete){
            delete(taskKey);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete(String taskKey){
        DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks").child(taskKey);
        deletedNote.removeValue();

    }





}


