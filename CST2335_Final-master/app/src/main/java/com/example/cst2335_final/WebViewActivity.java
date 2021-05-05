package com.example.cst2335_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cst2335_final.fragments.WebViewFragment;

/**
 * Activity for built-in web browser
 */

public class WebViewActivity extends BaseActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = findViewById(R.id.webview);

        Bundle webPage = getIntent().getExtras();

        WebViewFragment fragment = new WebViewFragment(); //add a DetailFragment
        fragment.setArguments(webPage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment) //Add the fragment in FrameLayout
                .commit();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_web_view);
    }

    @Override
    public String getHelpMessage() {
        return getResources().getString(R.string.webViewHelpMsg);
    }

    @Override
    public String getActivityName() {
        return getResources().getString(R.string.webViewActivityName);
    }
}