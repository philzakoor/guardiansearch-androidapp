package com.example.cst2335_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Base Activity which other activities extend
 * includes toolbar and drawers
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        //initialize shared preferences
        prefs = getSharedPreferences("SearchPrefs", Context.MODE_PRIVATE);

        //initialize toolbar
        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //initialize drawers
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.activityName);
        headerName.setText(getActivityName());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        menuSelection(item);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        menuSelection(item);

        return false;
    }

    /**
     * Takes users menu selection and forwards the user to the correct activity
     *
     * @param item the menu item selected by user
     */
    private void menuSelection(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                finish();
                break;
            case R.id.search:
                Intent searchPage = new Intent(this, GuardianSearchActivity.class);
                startActivity(searchPage);
                finish();
                break;
            case R.id.user:
                Intent userPage = new Intent(this, UserActivity.class);
                startActivity(userPage);
                finish();
                break;
            case R.id.favourites:
                Intent savedPage = new Intent(this, FavouritesActivity.class);
                startActivity(savedPage);
                finish();
                break;
            case R.id.help:
                AlertDialog.Builder helpAlert = new AlertDialog.Builder(this);
                helpAlert
                        .setTitle(R.string.help)
                        .setMessage(getHelpMessage())
                        .create()
                        .show();
                break;
        }
    }

    /**
     * Abstract method for activities help message to user
     *
     * @return activity help message
     */
    public abstract String getHelpMessage();

    /**
     * Abstract method for activity name to show in drawers
     *
     * @return activity name
     */
    public abstract String getActivityName();
}