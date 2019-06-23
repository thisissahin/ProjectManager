package com.projectmanage.Projects;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.R;

import java.util.HashMap;
import java.util.Map;

public class ProjectAddUser extends AppCompatActivity {
    EditText userAddEditText;
    Button userAddButton;
    String projectKey;
    String projectName;
    String currentUserId;
    DatabaseReference mFirebaseDatabaseReference;
    Query query;
    ValueEventListener listener;
    Boolean clicked= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_adduser);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
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

        userAddEditText = findViewById(R.id.userAddEditText);
        userAddButton = findViewById(R.id.userAddButton);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = userAddEditText.getText().toString();
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                query = mFirebaseDatabaseReference.child("Users").orderByChild("Username").equalTo(userName);
                clicked = true;

                query.addValueEventListener( listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {

                            String userKey = postSnapshot.getKey();
                            DatabaseReference userDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Requests").child(projectKey);
                            Map newUser = new HashMap();
                            newUser.put("requestFrom",currentUserId);
                            newUser.put("projectName",projectName);
                            userDatabase.setValue(newUser);
                            newUser.clear();
                            finish();
                            mFirebaseDatabaseReference.removeEventListener(listener);
                            query.removeEventListener(listener);





                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(clicked == true){

        }

    }
}
