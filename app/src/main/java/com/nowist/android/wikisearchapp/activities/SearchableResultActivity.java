package com.nowist.android.wikisearchapp.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.nowist.android.wikisearchapp.R;

/**
 * Shows the detail of a page
 */
public class SearchableResultActivity extends AppCompatActivity {

    private RecyclerView mSearchSuggestionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_result);
        handleIntent(getIntent());
        initControl();
    }

    private void initControl() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            //Launch Detail Activity
//            Intent wordIntent = new Intent(this, SearchableResultActivity.class);
//            wordIntent.setData(intent.getData());
//            startActivity(wordIntent);
        }
    }

    private void doMySearch(String query) {

    }
}
