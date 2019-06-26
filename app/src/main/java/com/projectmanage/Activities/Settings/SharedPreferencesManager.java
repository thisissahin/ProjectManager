package com.projectmanage.Activities.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    private final int PRIVATE_MODE_SHARED_PREF = 0;

    private final String PREF_NAME = "FONT";

    private final String KEY_TO_USE = PREF_NAME + "setFont";


    public SharedPreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE_SHARED_PREF);
        editor = pref.edit();
    }

    public void setVariable(float keySize) {
        editor.putFloat(KEY_TO_USE, keySize);
        editor.commit();
    }

    public float getVariable() {
        return pref.getFloat(KEY_TO_USE, 16);
    }

}


