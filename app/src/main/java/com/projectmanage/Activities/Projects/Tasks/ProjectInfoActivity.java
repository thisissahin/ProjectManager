package com.projectmanage.Activities.Projects.Tasks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectmanage.Activities.Projects.ProjectAddUser;
import com.projectmanage.Adapters.UserListAdapter;
import com.projectmanage.Models.UsersObject;
import com.projectmanage.R;

import java.util.ArrayList;


public class ProjectInfoActivity extends AppCompatActivity implements UserListAdapter.OnUserListener {

    EditText projectNameEditText, projectNoteEditText;
    LinearLayout layout;

    String projectKey, projectName, projectNote = null;
    String currentUserId;
    DatabaseReference databaseProject;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    UserListAdapter adapter;
    ArrayList<UsersObject> userListNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                projectKey = null;
                projectName = null;
            } else {
                projectKey = extras.getString("projectKey");
                projectName = extras.getString("projectName");
            }
        } else {
            projectKey = (String) savedInstanceState.getSerializable("projectKey");
            projectName = (String) savedInstanceState.getSerializable("projectName");

        }
        if (projectKey != null) {
            databaseProject = FirebaseDatabase.getInstance().getReference().child("Projects").child(projectKey);

        }
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout = findViewById(R.id.addMemberLayout);
        projectNameEditText = findViewById(R.id.projectInfoName);
        projectNoteEditText = findViewById(R.id.projectInfoNote);
        setRecyclerView();
        getProjectData();
        getUserIds();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectAddUser.class);
                intent.putExtra("projectKey",projectKey);
                intent.putExtra("projectName",projectName);
                startActivity(intent);
            }
        });



    }


    public void getProjectData() {

        databaseProject.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    projectName = dataSnapshot.child("projectName").getValue().toString();
                    projectNote = dataSnapshot.child("projectNote").getValue().toString();
                    projectNameEditText.setText(projectName);
                    projectNoteEditText.setText(projectNote);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getUserIds() {
        DatabaseReference databaseProjectUsersIds = databaseProject.child("Users");
        databaseProjectUsersIds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListNames.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    final String userId = data.getKey();
                    final String userStatus = data.child("Status").getValue().toString();


                    DatabaseReference databaseProjectUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    databaseProjectUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userName = dataSnapshot.child("Username").getValue().toString();
                            UsersObject newMessage = new UsersObject(userName,userId,userStatus);
                            userListNames.add(newMessage);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void setRecyclerView() {

        recyclerView = findViewById(R.id.projectInfoRcy);
        mLayoutManager = new LinearLayoutManager(ProjectInfoActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserListAdapter(userListNames, ProjectInfoActivity.this,this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) mLayoutManager).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onUserClick(final int position, View v) {
        final PopupMenu popupMenu = new PopupMenu(ProjectInfoActivity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu,popupMenu.getMenu());
        final String userId = userListNames.get(position).getUserKey();

        databaseProject.child("Users").child(currentUserId).child("Status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userStatus = dataSnapshot.getValue().toString();

                if (userStatus.equals("Admin")){
                    Log.i("TAG",userStatus);
                    if (!userId.equals(currentUserId)){
                        popupMenu.show();
                    }
                    else {
                        Toast.makeText(ProjectInfoActivity.this,"You cant change your own status !",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(ProjectInfoActivity.this,"You dont have admin access !",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {



                if (menuItem.getItemId() == R.id.userAdmin){
                    DatabaseReference userStatus = databaseProject.child("Users").child(userId).child("Status");
                    userStatus.setValue("Admin").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userListNames.get(position).setUserStatus("Admin");
                            adapter.notifyDataSetChanged();
                        }
                    });

                    userListNames.get(position).setUserStatus("Admin");
                    adapter.notifyDataSetChanged();

                }

                if (menuItem.getItemId() == R.id.userMember){
                    DatabaseReference userStatus = databaseProject.child("Users").child(userId).child("Status");
                    userStatus.setValue("Member").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userListNames.get(position).setUserStatus("Member");
                            adapter.notifyDataSetChanged();
                        }
                    });


                }

                return false;
            }
        });
    }

}
