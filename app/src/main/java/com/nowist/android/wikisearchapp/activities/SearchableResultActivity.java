package com.nowist.android.wikisearchapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nowist.android.wikisearchapp.R;
import com.nowist.android.wikisearchapp.Utils;

/**
 * Shows the detail of a page
 */
public class SearchableResultActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_result);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);
        mTitle = getIntent().getStringExtra("title");
        initControl();
    }

    private void initControl() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (!TextUtils.isEmpty(mTitle)) {
            getSupportActionBar().setTitle(mTitle);
        } else {
            getSupportActionBar().setTitle(getString(R.string.detail_screen));
        }


        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        if (Utils.isNetworkAvailable(this)) {
            mWebView.loadUrl(getWebUrl());
        } else {
            Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public String getWebUrl() {
        return "https://en.wikipedia.org/w/index.php?" + "title=" + mTitle + "&action=render";
    }
}
