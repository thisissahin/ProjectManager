package com.projectmanage;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Notes.MainNoteActivity;
import com.projectmanage.Settings.SettingsActivity;
import com.projectmanage.Projects.ProjectActivity;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                Intent intentS = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentS);
                return true;

            case R.id.projects:
                Intent intentU = new Intent(MainActivity.this, ProjectActivity.class);
                startActivity(intentU);
                return true;

            case R.id.notes:
                Intent intentN = new Intent(MainActivity.this, MainNoteActivity.class);
                startActivity(intentN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }





}

