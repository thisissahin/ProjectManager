package com.projectmanage.Activities;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.projectmanage.Adapters.ViewPagerAdapter;
import com.projectmanage.Activities.Notes.FragmentNoteActivity;
import com.projectmanage.Activities.Projects.FragmentProjectActivity;
import com.projectmanage.Activities.Projects.Requests.FragmentRequestActivity;
import com.projectmanage.Activities.Settings.SettingsActivity;
import com.projectmanage.R;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolBar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(toolBar);
        tabLayout = findViewById(R.id.mainTab);
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentRequestActivity(), "Requests");
        adapter.AddFragment(new FragmentProjectActivity(), "Projects");
        adapter.AddFragment(new FragmentNoteActivity(), "Notes");
        tabLayout.setTabTextColors( Color.parseColor("#e0e0e0"),Color.parseColor("#FFFFFF") );
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                Intent intentS = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentS);
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

