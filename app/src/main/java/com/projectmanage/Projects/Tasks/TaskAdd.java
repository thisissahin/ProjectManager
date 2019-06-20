package com.projectmanage.Projects.Tasks;



import android.os.Handler;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class TaskAdd extends AppCompatActivity {

    private EditText taskNoteEdit;
    private EditText taskTitleEdit;
    private EditText taskCheckListEdit;
    private ImageView checkListButton;
    DatabaseReference mDatabaseTask;
    private String projectKey;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    CheckListAdapter adapter;
    private ArrayList<CheckListObject> checkListTexts = new ArrayList<>();






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


        taskTitleEdit = findViewById(R.id.taskAddTitleEditText);
        taskCheckListEdit = findViewById(R.id.taskAddCheckListEdit);
        taskNoteEdit = findViewById(R.id.taskAddNoteEditText);
        checkListButton = findViewById(R.id.taskAddCheckImage);
        recyclerView = findViewById(R.id.taskAddCheckListRcy);
        mLayoutManager =  new LinearLayoutManager(TaskAdd.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CheckListAdapter(checkListTexts, TaskAdd.this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),((LinearLayoutManager) mLayoutManager).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);


        checkListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String checkListText = taskCheckListEdit.getText().toString();
                taskCheckListEdit.setText("");
                final Boolean checkBoxBool = false;

                if(!checkListText.isEmpty()) {

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            CheckListObject newMessage = new CheckListObject(checkListText, checkBoxBool, null);
                            checkListTexts.add(0, newMessage);
                            adapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(0);

                        }
                    });
                }
            }
        });


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

            adapter.notifyDataSetChanged();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    taskAdd();
                }
            }, 500);

        }
        return super.onOptionsItemSelected(item);

    }

    public void taskAdd(){



            String note = taskNoteEdit.getText().toString();
            String title = taskTitleEdit.getText().toString();

            if(!title.isEmpty()){
                DatabaseReference newMessageDb = mDatabaseTask.push();
                String pushKey = newMessageDb.getKey().toString();

                Map newMessage = new HashMap();
                String date = new SimpleDateFormat("MMM dd HH:mm").format(Calendar.getInstance().getTime());
                newMessage.put("note",note);
                newMessage.put("date",date);
                newMessage.put("title",title);
                newMessageDb.setValue(newMessage);

                DatabaseReference mDatabaseCheckList = FirebaseDatabase.getInstance().getReference().child("Projects")
                        .child(projectKey).child("Tasks").child(pushKey).child("checkList");

                if(!checkListTexts.isEmpty()) {
                    for (int i = 0; i< checkListTexts.size(); i++) {
                        DatabaseReference pushdata = mDatabaseCheckList.push();
                        Map newCheck = new HashMap();
                        Collections.reverse(checkListTexts);
                        newCheck.put("text",checkListTexts.get(i).getCheckListText());
                        newCheck.put("checkBox",checkListTexts.get(i).getCheckBoxBool());
                        pushdata.setValue(newCheck);
                        newCheck.clear();
                    }
                }
                finish();




            }

            taskNoteEdit.setText(null);

    }

}


