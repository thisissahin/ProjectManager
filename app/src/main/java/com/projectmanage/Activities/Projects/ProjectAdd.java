package com.projectmanage.Activities.Projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Activities.Projects.Tasks.MainTaskActivity;
import com.projectmanage.R;

import java.util.HashMap;
import java.util.Map;

public class ProjectAdd extends AppCompatActivity {

    EditText projectEditText;
    Button   projetButton;
    DatabaseReference projectDatabase;
    String projectName;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectadd);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        projectEditText = findViewById(R.id.projectEditText);
        projetButton = findViewById(R.id.projectButton);

        projetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectName = projectEditText.getText().toString();

                if(!projectName.isEmpty()){
                    projectDatabase =FirebaseDatabase.getInstance().getReference().child("Projects");
                    String projectKey = projectDatabase.push().getKey();
                    DatabaseReference newMessageDb = projectDatabase.child(projectKey);
                    Map newMessage = new HashMap();
                    newMessage.put("projectName",projectName);
                    newMessageDb.setValue(newMessage);
                    newMessageDb = projectDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId)
                            .child("Projects").child(projectKey);
                    newMessage.clear();
                    newMessage.put("createdBy","me");
                    newMessage.put("projectName",projectName);
                    newMessageDb.setValue(newMessage);
                    newMessageDb = projectDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Users");
                    newMessage.clear();
                    newMessage.put("createdBy",currentUserId);
                    newMessageDb.setValue(newMessage);
                    Intent i = new Intent(ProjectAdd.this, MainTaskActivity.class);
                    i.putExtra("projectKey",projectKey);
                    i.putExtra("projectName",projectName);
                    startActivity(i);
                    finish();


                }
            }
        });


    }
}
