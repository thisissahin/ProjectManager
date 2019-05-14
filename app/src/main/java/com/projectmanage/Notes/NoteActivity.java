package com.projectmanage.Notes;




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


public class NoteActivity extends AppCompatActivity {
    DatabaseReference mDatabaseNote;
    private String currentUserId;
    EditText noteUpdateEdit;
    private Boolean editFocusable;
    private String key;
    SharedPreferencesManager mSharedPrefManager;

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
        mSharedPrefManager = new SharedPreferencesManager(this);

        noteUpdateEdit = findViewById(R.id.noteEditText);
        editFocusable = false;
        noteUpdateEdit.setFocusable(false);
        noteUpdateEdit.setFocusableInTouchMode(false);
        noteUpdateEdit.setClickable(false);
        noteUpdateEdit.setTextSize(mSharedPrefManager.getVariable());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes");

        mDatabaseNote.child(key).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.child("text").getValue().toString();
                noteUpdateEdit.setText(data);


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

        if (id == R.id.action_edit && editFocusable == false) {
            editFocusable = true;


            noteUpdateEdit.setFocusable(true);
            noteUpdateEdit.setFocusableInTouchMode(true);
            noteUpdateEdit.setClickable(true);
            noteUpdateEdit.setShowSoftInputOnFocus(true);
            item.setIcon(R.drawable.baseline_done_white_18dp);
            noteUpdateEdit.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);






        }
        else if(id == R.id.action_edit && editFocusable == true){
            String text = noteUpdateEdit.getText().toString();

            mDatabaseNote.child(key).child("text").setValue(text);

            item.setIcon(R.drawable.baseline_edit_white_18dp);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            editFocusable = false;
            noteUpdateEdit.setFocusable(false);
            noteUpdateEdit.setFocusableInTouchMode(false);
            noteUpdateEdit.setClickable(false);

        }

        else if(id == R.id.action_delete){
            delete(key);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete(String key){
        DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notes").child(key);
        deletedNote.removeValue();

    }





}

