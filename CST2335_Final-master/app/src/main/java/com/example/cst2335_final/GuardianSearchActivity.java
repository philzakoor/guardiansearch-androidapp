package com.example.cst2335_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.cst2335_final.adapters.SearchListAdapter;
import com.example.cst2335_final.api.GuardianNewsAPI;
import com.example.cst2335_final.beans.SearchItem;
import com.example.cst2335_final.database.DBHandler;
import com.example.cst2335_final.fragments.WebViewFragment;
import com.example.cst2335_final.observer.Observer;

import java.util.ArrayList;

/**
 * Activity for searching Guardian News
 * Users are able to enter a search item into a textbox and search Guardian News
 * Users are able to click the item and view detailed information
 * Users are able to save articles for later viewing
 */
public class GuardianSearchActivity extends BaseActivity implements Observer {

    private static final String TAG = "MainActivity";
    private final ArrayList<SearchItem> items = new ArrayList<>();
    private BaseAdapter searchListAdapter;
    private ProgressBar progressBar;
    private EditText searchFilter;
    private boolean hasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize frame layout
        FrameLayout fLayout = findViewById(R.id.frameLayout);
        hasFragment = fLayout != null;

        //initialize search filter and listener
        searchFilter = findViewById(R.id.searchFilter);
        searchFilter.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchString = searchFilter.getText().toString();
                guardianSearch(searchString);
                handled = true;
            }
            return handled;
        });

        //initialize progressbar
        progressBar = findViewById(R.id.searchProgressBar);

        //initialize searchItems list with click listener
        ListView searchItems = findViewById(R.id.list);
        searchItems.setAdapter(searchListAdapter = new SearchListAdapter(this, items));
        searchItems.setOnItemClickListener((parent, view, position, id) -> showMessage(position));

        //initialize search button with click listener
        Button searchBtn = findViewById(R.id.search);
        searchBtn.setOnClickListener((click) -> {
            //search guardian news
            String searchString = searchFilter.getText().toString();
            guardianSearch(searchString);
            Toast.makeText(this, R.string.searchToast, Toast.LENGTH_SHORT).show();
        });

        //get passed or last search data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //if searchFilter was passed, change textbox to passed filter and search guardian news
            String passedFilter = extras.getString("searchFilter");
            searchFilter.setText(passedFilter);
            guardianSearch(passedFilter);
        } else {
            //show last searched item
            String lastSearch = prefs.getString("lastSearch", "");
            searchFilter.setText(lastSearch);
        }
    }

    @Override
    public String getHelpMessage() {
        return getResources().getString(R.string.searchHelpMsg);
    }

    @Override
    public String getActivityName() {
        return getResources().getString(R.string.guardianSearchActName);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_guardian_search);
    }

    /**
     * Takes user's search input and executes GuardianNewsAPI to search Guardian News.
     *
     * @see GuardianNewsAPI
     */
    private void guardianSearch(String searchString) {
        //shows progressbar
        progressBar.setVisibility(View.VISIBLE);

        //search guardian news
        AsyncTask<String, Integer, ArrayList<SearchItem>> api = new GuardianNewsAPI(this);
        api.execute(searchString);

        //hide keyboard
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        //save search Filter
        saveLastSearchFilter(searchString);
    }

    /**
     * Saves last search filter to shared preferences
     *
     * @param searchString user's search filter
     */
    private void saveLastSearchFilter(String searchString) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("lastSearch", searchString);
        prefsEditor.apply();
    }

    /**
     * shows detailed alert of search item
     *
     * @param position search item's position arraylist
     */
    private void showMessage(int position) {
        //initialize Alert dialog
        SearchItem item = items.get(position);


        View searchAlertView = getLayoutInflater().inflate(R.layout.row_searchalert, null);

        TextView searchAlterTitle = searchAlertView.findViewById(R.id.searchAlertTitleView);
        searchAlterTitle.setText(item.getTitle());

        TextView searchAlterSection = searchAlertView.findViewById(R.id.searchAlertSectionView);
        searchAlterSection.setText(item.getSection());

        TextView searchAlterUrl = searchAlertView.findViewById(R.id.searchAlertUrlView);
        searchAlterUrl.setText(item.getUrl().toString());

        //create detailed search item message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog detailAlertDialog = builder
                .setTitle(this.getString(R.string.searchAlertTitle))
                .setMessage(this.getString(R.string.searchAlertMessage))
                .setView(searchAlertView)
                .setPositiveButton(R.string.addToFavourite, (click, arg) -> {

                    //check to see if favourites are allowed
                    boolean allowFavourites = prefs.getBoolean("allowFavourites", true);

                    if (allowFavourites) {
                        //save to database
                        DBHandler dbHandler = new DBHandler(getApplicationContext());
                        dbHandler.addFavourite(items.get(position));
                        Toast.makeText(this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getResources().getString(R.string.favouritesSnackMsg) + getResources().getString(R.string.off);
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.searchAlertDismiss, (click, arg) -> {
                })
                .create();
        detailAlertDialog.show();

        //add click listener to url
        searchAlterUrl.setOnClickListener((click) -> {

            Bundle webPage = new Bundle();
            webPage.putString("url", item.getUrl().toString());

            //if fragment load webpage into fragment
            if (hasFragment) {
                WebViewFragment fragment = new WebViewFragment(); //add a DetailFragment
                fragment.setArguments(webPage); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment) //Add the fragment in FrameLayout
                        .commit();

            } else {
                //start webview activity
                Intent webView = new Intent(GuardianSearchActivity.this, WebViewActivity.class);
                webView.putExtras(webPage);
                startActivity(webView);
            }
            detailAlertDialog.dismiss();
        });
    }

    @Override
    public void update(ArrayList<SearchItem> searchItems) {
        //clear and add new search items
        items.clear();
        items.addAll(searchItems);
        searchListAdapter.notifyDataSetChanged();

        //hide progress bar
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void update(Integer progress) {
        progressBar.setScrollBarSize(progress);
        if (progress == 100) {
            progressBar.setVisibility(View.GONE);
        }
    }
}