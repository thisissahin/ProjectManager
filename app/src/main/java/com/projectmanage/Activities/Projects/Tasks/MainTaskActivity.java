package com.projectmanage.Activities.Projects.Tasks;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Activities.Projects.ProjectAddUser;
import com.projectmanage.R;
import com.projectmanage.Adapters.ViewPagerAdapter;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;


public class MainTaskActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String projectKey;
    private String projectName;
    private String currentUserId;

    private static final String TAG = "TaskTag";

    DatabaseReference mDatabaseTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        if(projectKey==null){



        }
        else {
            mDatabaseTask = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).child("Tasks");
        }


        mDatabaseTask.keepSynced(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(projectName);


        tabLayout = findViewById(R.id.mainTaskTab);
        viewPager = findViewById(R.id.taskViewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentOpenTaskActivity(), "Open");
        adapter.AddFragment(new FragmentActiveTaskActivity(), "Active");
        adapter.AddFragment(new FragmentCompletedTaskActivity(), "Completed");
        tabLayout.setTabTextColors( Color.parseColor("#e0e0e0"),Color.parseColor("#FFFFFF") );
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        checkProjectExist();
    }

    public void checkProjectExist(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);
        final DatabaseReference mDatabaseDelete = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Projects").child(projectKey);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    Toast.makeText(MainTaskActivity.this,projectName + " is deleted!",Toast.LENGTH_LONG).show();
                    mDatabaseDelete.removeValue();
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                DatabaseReference deleteProject = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey).
                        child("Users").child(currentUserId);
                deleteProject.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean admin = (Boolean) dataSnapshot.child("admin").getValue();
                        DatabaseReference deleteProject = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);

                        if (admin == true){
                            deleteProject.removeValue();
                            deleteProject = FirebaseDatabase.getInstance().getReference().child("Users").
                                    child(currentUserId).child("Projects").child(projectKey);
                            deleteProject.removeValue();
                            finish();
                        }
                        else{
                            Toast.makeText(MainTaskActivity.this,"You dont have admin access",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.memberAdd:
                Intent i = new Intent(MainTaskActivity.this, ProjectAddUser.class);
                i.putExtra("projectKey",projectKey);
                i.putExtra("projectName",projectName);
                i.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_menu, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

