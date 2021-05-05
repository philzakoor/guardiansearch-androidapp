package com.example.cst2335_final.beans;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Stores title, url and section from a search on Guardian News
 */

public class SearchItem {

    private long id;
    private String title;
    private URL url;
    private String section;

    public SearchItem() {
        this.title = null;
        this.url = null;
        this.section = null;
    }

    public SearchItem(String title, URL url, String section, long id) {
        this.title = title;
        this.url = url;
        this.section = section;
        this.id = id;
    }

    public SearchItem(int id, String title, String url, String section) {
        this.title = title;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.section = section;
        this.id = id;

    }

    /**
     * getter for ID
     *
     * @return searchItem's Id
     */
    public long getId() {
        return id;
    }

    /**
     * setter for id
     *
     * @param id searchItem Id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getter for title
     *
     * @return searchItem's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for title
     *
     * @param title searchItem's Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for url
     *
     * @return searchItem's url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * setter for url
     *
     * @param url searchItem's URL
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    public String getStringUrl() {
        return url.toString();
    }

    public void setStringUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter for section
     *
     * @return searchItem's section
     */
    public String getSection() {
        return section;
    }

    /**
     * setter for section
     *
     * @param section searchItems's Section
     */
    public void setSection(String section) {
        this.section = section;
    }
}
