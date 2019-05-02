package com.projectmanage;







import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mEmail,mPassword;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireAuthstlis;
    private TextView mSignUp;
    private String email;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        fireAuthstlis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        };

        mLogin = findViewById(R.id.btn_login);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mSignUp = findViewById(R.id.link_signup);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()==true) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener
                            (LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mProgressBar.setVisibility(View.VISIBLE);
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
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
}






