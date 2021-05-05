package com.example.cst2335_final.observer;

import com.example.cst2335_final.beans.SearchItem;

import java.util.ArrayList;

/**
 * Observer Pattern interface for API updating UI
 */

public interface Observable {

    /**
     * Registers observers to ArrayList
     *
     * @param o Observer object
     * @see Observer
     */
    void registerObserver(Observer o);

    /**
     * Removes observers from ArrayList
     *
     * @param o Observer Object
     * @see Observer
     */
    void removeObserver(Observer o);

    /**
     * passes Arraylist of SearchItems to Observer
     *
     * @param searchItems list of SearchItems
     * @see SearchItem
     * @see Observer
     */
    void notifyObservers(ArrayList<SearchItem> searchItems);
}
