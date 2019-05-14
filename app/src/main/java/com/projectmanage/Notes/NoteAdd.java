package com.projectmanage.Notes;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class NoteAdd extends AppCompatActivity {
    private EditText noteEdit;
    DatabaseReference mDatabaseNote;
    private String currentUserId;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                key= null;
            } else {
                key= extras.getString("key");
            }
        } else {
            key= (String) savedInstanceState.getSerializable("key");
        }
        if(key==null){
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes");


        }
        else {
            mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(key).child("Notes");
        }



        noteEdit = findViewById(R.id.noteEditText);

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
            String note = noteEdit.getText().toString();

            if(!note.isEmpty()){
                DatabaseReference newMessageDb = mDatabaseNote.push();

                Map newMessage = new HashMap();
                String date = new SimpleDateFormat("MMM dd HH:mm").format(Calendar.getInstance().getTime());
                newMessage.put("text",note);
                newMessage.put("date",date);

                newMessageDb.setValue(newMessage);
                finish();




            }

            noteEdit.setText(null);
        }
        return super.onOptionsItemSelected(item);

    }
}

