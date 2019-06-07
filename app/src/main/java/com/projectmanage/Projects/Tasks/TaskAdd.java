package com.projectmanage.Projects.Tasks;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class TaskAdd extends AppCompatActivity {
    private EditText taskNoteEdit;
    private EditText taskTitleEdit;
    private EditText taskCheckListEdit;
    DatabaseReference mDatabaseTask;
    private String projectKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskadd);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                projectKey= null;
            } else {
                projectKey= extras.getString("projectKey");
            }
        } else {
            projectKey= (String) savedInstanceState.getSerializable("projectKey");
        }
        if(projectKey==null){



        }
        else {
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks");
        }






        taskTitleEdit = findViewById(R.id.taskTitleEditText);
        taskCheckListEdit = findViewById(R.id.taskCheckListEdit);
        taskNoteEdit = findViewById(R.id.taskNoteEditText);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_done ) {
            String note = taskNoteEdit.getText().toString();
            String title = taskTitleEdit.getText().toString();

            if(!title.isEmpty()){
                DatabaseReference newMessageDb = mDatabaseTask.push();

                Map newMessage = new HashMap();
                String date = new SimpleDateFormat("MMM dd HH:mm").format(Calendar.getInstance().getTime());
                newMessage.put("note",note);
                newMessage.put("date",date);
                newMessage.put("title",title);

                newMessageDb.setValue(newMessage);


                finish();




            }

            taskNoteEdit.setText(null);
        }
        return super.onOptionsItemSelected(item);

    }
}


