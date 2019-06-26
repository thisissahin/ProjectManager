package com.projectmanage.Activities.Projects.Tasks;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Activities.Projects.ProjectAddUser;
import com.projectmanage.R;
import com.projectmanage.Adapters.ViewPagerAdapter;


public class MainTaskActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String projectKey;
    private String projectName;

    DatabaseReference mDatabaseTask;
    private String currentUserId;


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
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                DatabaseReference deletedNote = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);
                deletedNote.removeValue();
                deletedNote = FirebaseDatabase.getInstance().getReference().child("Users").
                        child(currentUserId).child("Projects").child(projectKey);
                deletedNote.removeValue();
                finish();
                return true;
            case R.id.memberAdd:
                Intent i = new Intent(MainTaskActivity.this, ProjectAddUser.class);
                i.putExtra("projectKey",projectKey);
                i.putExtra("projectName",projectName);
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

