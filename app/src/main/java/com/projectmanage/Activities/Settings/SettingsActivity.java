package com.projectmanage.Activities.Settings;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.projectmanage.R;


public class SettingsActivity extends AppCompatActivity {
    TextView ClickFontSize,FontSize;
    SharedPreferencesManager mSharedPrefManager;
    LinearLayout settingFont;
    int fontVariable = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ClickFontSize = findViewById(R.id.clickFontSize);
        FontSize = findViewById(R.id.fontSize);
        settingFont = findViewById(R.id.fontSetting);
        mSharedPrefManager = new SharedPreferencesManager(this);

        settingFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(SettingsActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.font_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == (R.id.sp15)){
                            fontVariable = 15;
                            mSharedPrefManager.setVariable(fontVariable);
                            FontSize.setText("15" );

                        }

                        if (item.getItemId() == (R.id.sp17)){
                            fontVariable = 17;
                            mSharedPrefManager.setVariable(17);
                            FontSize.setText("17");


                        }

                        if (item.getItemId() == (R.id.sp20)){
                            fontVariable = 20;
                            mSharedPrefManager.setVariable(20);
                            FontSize.setText("20");


                        }


                        return true;
                    }

                });

            }

        });

        FontSize.setText(" " + (int) mSharedPrefManager.getVariable());










    }
}

