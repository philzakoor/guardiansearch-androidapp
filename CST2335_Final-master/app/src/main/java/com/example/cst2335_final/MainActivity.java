package com.example.cst2335_final;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Main Activity for application
 * Displays Logo and welcome message
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //removes logo in landscape
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ImageView logo = findViewById(R.id.logo);
            logo.setVisibility(View.GONE);
        }

        //forward to search page with search term
        Intent nextPage = new Intent(this, GuardianSearchActivity.class);
        EditText searchFilter = findViewById(R.id.searchFilter);
        Button guardianSearchBtn = findViewById(R.id.search);
        guardianSearchBtn.setOnClickListener((click) -> {
            nextPage.putExtra("searchFilter", searchFilter.getText().toString());
            startActivity(nextPage);
            finish();
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_main);
    }

    @Override
    public String getHelpMessage() {
        return getResources().getString(R.string.mainHelpMsg);
    }

    @Override
    public String getActivityName() {
        return getResources().getString(R.string.mainActName);
    }
}