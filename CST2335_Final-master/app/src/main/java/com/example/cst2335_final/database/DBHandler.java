package com.example.cst2335_final.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to create and manage favourites database
 * Database holds user's favourites articles
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version private
    static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "FavouriteNews";
    // Contacts table name
    private static final String TABLE_FAVOURITES = "favourites";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_SECTION = "section";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVOURITES +
                "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
                KEY_URL + " TEXT," + KEY_SECTION + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        // Creating tables again
        onCreate(db);
    }

    /**
     * creates new record in favourites table
     *
     * @param searchItem the article to save
     */
    // Adding new Favourite item
    public void addFavourite(SearchItem searchItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, searchItem.getTitle());
        values.put(KEY_URL, searchItem.getStringUrl());
        values.put(KEY_SECTION, searchItem.getSection());
        db.insert(TABLE_FAVOURITES, null, values);
        db.close();
    }

    /**
     * queries database for searchItem
     *
     * @param id the id of searchItem
     * @return the quarried article from database
     */
    public SearchItem getFavourite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITES, new String[]{KEY_ID, KEY_TITLE, KEY_URL, KEY_SECTION},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        SearchItem contact = new SearchItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return contact;
    }

    /**
     * Queries database to create list of SearchItems of user's saved articles
     *
     * @return list of searchItems
     * @see SearchItem
     */
    // Getting All favourites
    public List<SearchItem> getAllFavourite() {
        List<SearchItem> favouriteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SearchItem Favourite = new SearchItem();
                Favourite.setId(Integer.parseInt(cursor.getString(0)));
                Favourite.setTitle(cursor.getString(1));
                Favourite.setStringUrl(cursor.getString(2));
                Favourite.setSection(cursor.getString(3));
                // Adding contact to list
                favouriteList.add(Favourite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favouriteList;
    }

    /**
     * returns the count of favourite in database
     *
     * @return the number rows in favourite database
     */
    public int getFavouriteCount() {
        String countQuery = "SELECT * FROM " + TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();// return count
        return cursor.getCount();
    }

    /**
     * updates favourites from database
     *
     * @param searchItem item that needs to be updated
     * @return number of rows updated
     * @see SearchItem
     */
    public int updateFavourite(SearchItem searchItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, searchItem.getTitle());
        values.put(KEY_URL, searchItem.getStringUrl());
        // updating row
        return db.update(TABLE_FAVOURITES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(searchItem.getId())});
    }

    /**
     * deletes single searchItem from user's database
     *
     * @param searchItem item to delete
     * @see SearchItem
     */
    public void deleteFavourite(SearchItem searchItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, KEY_ID + " = ?",
                new String[]{String.valueOf(searchItem.getId())});
        db.close();
    }

    /**
     * deletes all favourites from user's database
     */
    public void clearFavourites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVOURITES);
    }
}

