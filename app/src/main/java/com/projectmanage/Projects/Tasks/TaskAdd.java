package com.projectmanage.Projects.Tasks;



import android.content.Intent;
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
    private EditText taskEdit;
    DatabaseReference mDatabaseTask;
    private String currentUserId;
    private String projectKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
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
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Notes");
        }







        taskEdit = findViewById(R.id.taskEditText);

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
            String note = taskEdit.getText().toString();

            if(!note.isEmpty()){
                DatabaseReference newMessageDb = mDatabaseTask.push();

                Map newMessage = new HashMap();
                String date = new SimpleDateFormat("MMM dd HH:mm").format(Calendar.getInstance().getTime());
                newMessage.put("text",note);
                newMessage.put("date",date);

                newMessageDb.setValue(newMessage);
                Intent i = new Intent(TaskAdd.this, MainTaskActivity.class);
                i.putExtra("projectKey",projectKey);
                startActivity(i);
                finish();




            }

            taskEdit.setText(null);
        }
        return super.onOptionsItemSelected(item);

    }
}


