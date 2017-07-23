package com.nowist.android.wikisearchapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nowist.android.wikisearchapp.R;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initControl();
        handleIntent(getIntent());
    }

    private void initControl() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setActivated(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_search_suggestion);
    }

    private void doMySearch(String query) {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
//        searchView.
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }*/

//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }

    private void handleIntent(Intent intent) {
        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.setClass(this, SearchableResultActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Mayank", "Query Submitted called");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Mayank", "Query Text Change called");
        return false;
    }
}
