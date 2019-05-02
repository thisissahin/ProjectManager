package com.projectmanage.Notes;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class taskmanager extends Application

{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
