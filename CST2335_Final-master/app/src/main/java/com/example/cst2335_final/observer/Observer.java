package com.example.cst2335_final.observer;

import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;

/**
 * Observer Pattern Interface for API updating UI
 */

public interface Observer {
    /**
     * Gets passed a list of SearchItems from Observable
     *
     * @param searchItems ArrayList of SearchItems
     * @see SearchItem
     * @see Observable
     */
    void update(ArrayList<SearchItem> searchItems);

    /**
     * Gets passed progress update from Observable
     *
     * @param progress integer of progress level
     * @see Observable
     */
    void update(Integer progress);
}
