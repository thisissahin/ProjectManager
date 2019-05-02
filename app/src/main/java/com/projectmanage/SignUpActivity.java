package com.projectmanage;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail,mPassword,mUserName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireAuthstlis;
    private String email;
    private String password;
    private String userName;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        fireAuthstlis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }
        };

        mRegister = findViewById(R.id.btn_signup);
        mUserName = findViewById(R.id.input_username);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.SignUpProgressBar);
        mProgressBar.setVisibility(View.INVISIBLE);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()==true) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener
                            (SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        mProgressBar.setVisibility(View.INVISIBLE);

                                        String userid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                                        Map userInfo = new HashMap();

                                        userInfo.put("Username",userName);
                                        currentUserDb.updateChildren(userInfo);

                                    }

                                }
                            });
                }

            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        userName = mUserName.getText().toString();



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (userName.isEmpty() || userName.length() < 4 || userName.length() > 15) {
            mUserName.setError("Write username");
            valid = false;
        } else {
            mUserName.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireAuthstlis);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireAuthstlis);
    }

}


