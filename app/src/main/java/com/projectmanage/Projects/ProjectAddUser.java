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
    TextView usernameTextView;
    String projectKey;
    String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_adduser);
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

        userAddEditText = findViewById(R.id.userAddEditText);
        userAddButton = findViewById(R.id.userAddButton);
        usernameTextView = findViewById(R.id.usernameTextView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = userAddEditText.getText().toString();
                final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = mFirebaseDatabaseReference.child("Users").orderByChild("Username").equalTo(userName);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {

                            String userKey = postSnapshot.getKey();
                            usernameTextView.setText(userKey);
                            DatabaseReference userDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Projects").child(projectKey);
                            Map newUser = new HashMap();
                            newUser.put("createdBy",currentUserId);
                            userDatabase.setValue(newUser);
                            DatabaseReference projetDatabase =FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Users").push();
                            newUser.clear();
                            newUser.put("user",currentUserId);
                            projetDatabase.setValue(newUser);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

    }
}
