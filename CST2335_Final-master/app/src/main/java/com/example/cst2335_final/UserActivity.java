package com.example.cst2335_final;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cst2335_final.database.DBHandler;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activity that shows user's preferences
 * Allows user to clear all favourites
 * Allows user to add/change username
 * Allows user to stop saving searches to database
 */

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //gets shared preferences Username and allowFavourites
        String userName = prefs.getString("userName", getResources().getString(R.string.userNameHint));
        boolean allowFavourites = prefs.getBoolean("allowFavourites", true);

        //initalizes Username Text
        TextView userNameText = findViewById(R.id.userName);
        userNameText.setText(userName);

        //initalizes allowFavourites Checkbox
        CheckBox allowFavouritesCB = findViewById(R.id.savedSearchCB);
        allowFavouritesCB.setChecked(allowFavourites);

        //Adds listener to username to edit username
        userNameText.setOnClickListener((click) -> {
                    View searchAlertView = getLayoutInflater().inflate(R.layout.row_useralert, null);
                    EditText userNameAlertText = searchAlertView.findViewById(R.id.userNameAlert);

                    AlertDialog.Builder nameAlert = new AlertDialog.Builder(this);
                    nameAlert.setTitle(R.string.userName)
                            .setView(searchAlertView)
                            .setNegativeButton(R.string.searchAlertDismiss, (c, arg) -> {
                            })
                            .setPositiveButton(R.string.save, (c, arg) -> {
                                SharedPreferences.Editor prefsEditor = prefs.edit();
                                prefsEditor.putString("userName", userNameAlertText.getText().toString());
                                prefsEditor.apply();

                                userNameText.setText(userNameAlertText.getText().toString());
                            })
                            .create()
                            .show();
                }
        );

        //Adds listener for allowFavourites checkbox
        allowFavouritesCB.setOnCheckedChangeListener((b, c) -> {

            String message = getResources().getText(R.string.favouritesSnackMsg).toString();
            SharedPreferences.Editor prefsEditor = prefs.edit();

            if (c) {
                message += " " + getResources().getString(R.string.on);
            } else {
                message += " " + getResources().getString(R.string.off);
            }

            allowFavouritesCB.setChecked(c);
            prefsEditor.putBoolean("allowFavourites", c);
            prefsEditor.apply();

            Snackbar.make(b, message, Snackbar.LENGTH_SHORT).show();
        });

        //Adds listener to button to clear database
        Button clearBtn = findViewById(R.id.clearFavourites);
        clearBtn.setOnClickListener((click) -> {
            DBHandler db = new DBHandler(getApplicationContext());
            db.clearFavourites();
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_user);
    }

    @Override
    public String getHelpMessage() {
        return getResources().getString(R.string.unavailableFeature);
    }

    @Override
    public String getActivityName() {
        return getResources().getString(R.string.userActivityName);
    }
}