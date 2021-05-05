package com.example.cst2335_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.cst2335_final.adapters.SearchListAdapter;
import com.example.cst2335_final.beans.SearchItem;
import com.example.cst2335_final.database.DBHandler;

import java.util.ArrayList;

/**
 * Activity that lists user's favourites
 * Users are able to click on item and see details
 * Users are able to delete favourite from list
 * Users are able to view article
 */
public class FavouritesActivity extends BaseActivity {

    private final ArrayList<SearchItem> items = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView searchItems;
    private SearchListAdapter searchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.searchProgressBar);

        searchItems = findViewById(R.id.list);
        searchItems.setAdapter(searchListAdapter = new SearchListAdapter(this, items));

        searchItems.setOnItemClickListener((parent, view, position, id) -> showMessage(position));
        getFavouriteArticles();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_favourites);
    }

    @Override
    public String getHelpMessage() {
        return getResources().getString(R.string.favouritesHelpMsg);
    }

    @Override
    public String getActivityName() {
        return getResources().getString(R.string.favouritesActivityName);
    }

    /**
     * Connects to database and gets list of saved articles
     */

    private void getFavouriteArticles() {

        progressBar.setVisibility(View.VISIBLE);
        searchItems.setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.nodata);
        DBHandler dbHandler = new DBHandler(this);
        items.addAll(dbHandler.getAllFavourite());
        searchListAdapter.notifyDataSetChanged();

        if (items.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            searchItems.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        searchItems.setVisibility(View.VISIBLE);

    }

    /**
     * Shows details of search item
     *
     * @param position The search item's position in llist
     */

    private void showMessage(int position) {
        SearchItem item = items.get(position);
        View searchAlertView = getLayoutInflater().inflate(R.layout.row_searchalert, null);

        TextView searchAlterTitle = searchAlertView.findViewById(R.id.searchAlertTitleView);
        searchAlterTitle.setText(item.getTitle());

        TextView searchAlterSection = searchAlertView.findViewById(R.id.searchAlertSectionView);
        searchAlterSection.setText(item.getSection());

        TextView searchAlterUrl = searchAlertView.findViewById(R.id.searchAlertUrlView);
        searchAlterUrl.setText(item.getUrl().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog detailsAlertDialog = builder.setTitle(this.getString(R.string.searchAlertTitle))
                .setMessage(this.getString(R.string.searchAlertMessage))
                .setView(searchAlertView)
                .setPositiveButton(R.string.deleteSearchSB, (click, arg) -> {
                    DBHandler dbHandler = new DBHandler(getApplicationContext());
                    dbHandler.deleteFavourite(items.get(position));
                    Toast.makeText(this, getResources().getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
                    items.remove(position);
                    searchListAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.searchAlertDismiss, (click, arg) -> {
                })
                .create();
        detailsAlertDialog.show();

        searchAlterUrl.setOnClickListener((click) -> {
            //start webview activity
            Intent openUrl = new Intent(FavouritesActivity.this, WebViewActivity.class);
            openUrl.putExtra("url", item.getStringUrl());
            startActivity(openUrl);
            detailsAlertDialog.dismiss();
        });
    }
}