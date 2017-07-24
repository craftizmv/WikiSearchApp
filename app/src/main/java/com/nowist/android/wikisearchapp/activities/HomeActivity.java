package com.nowist.android.wikisearchapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.wikisearchapp.BuildConfig;
import com.nowist.android.wikisearchapp.R;
import com.nowist.android.wikisearchapp.Utils;
import com.nowist.android.wikisearchapp.adapter.WikiSuggestionListAdapter;
import com.nowist.android.wikisearchapp.models.Page;
import com.nowist.android.wikisearchapp.models.QueryResponse;
import com.nowist.android.wikisearchapp.models.SuggestionItemDataModel;
import com.nowist.android.wikisearchapp.models.Terms;
import com.nowist.android.wikisearchapp.retrofit.ApiUtils;
import com.nowist.android.wikisearchapp.retrofit.RestApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, WikiSuggestionListAdapter.OnSuggestionItemClickListener {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private WikiSuggestionListAdapter mAdapter;
    private String mCurrentSearchText;
    private int mOffSet = 0;
    private boolean canRun = true;
    private ArrayList<SuggestionItemDataModel> mDataList;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initControl();
    }

    private void initControl() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setActivated(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(null);
        mDataList = new ArrayList<>();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_search_suggestion);
        mEmptyTextView = (TextView) findViewById(R.id.tv_empty_view);

        initRecyclerView();
        setOnLoadMoreListner();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new WikiSuggestionListAdapter(this, mRecyclerView, mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setOnLoadMoreListner() {
        mAdapter.setOnLoadMoreListener(new WikiSuggestionListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataList.add(null);
                        mAdapter.notifyItemInserted(mDataList.size() - 1);
                    }
                });

                if (!TextUtils.isEmpty(mCurrentSearchText)) {
                    handleApiCall(mCurrentSearchText, mOffSet, false);
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Here we call the retrofit api's to get the data
        mSearchView.clearFocus();
        handleApiCall(query, 0, true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        //Call the api to get
        mCurrentSearchText = newText;
        if (canRun) {
            canRun = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canRun = true;
                    handleApiCall(newText, 0, true);
                }
            }, 500);
        }
        return true;
    }

    private void handleApiCall(final String newText, final int offset, final boolean isFromQueryChange) {
        if (!Utils.isNetworkAvailable(this)) {
            checkAndRemoveLoadingElement(offset);
            Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newText)) {
            checkAndRemoveLoadingElement(offset);
            return;
        }
        RestApiService restApiService = ApiUtils.getApiService(getApplicationContext());
        Map<String, String> map = getDefaultQueryMap(newText);
        map.put("gpsoffset", String.valueOf(offset));
        Call<QueryResponse> call = restApiService.getWikiSuggestions(map);
        if (isFromQueryChange) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        call.enqueue(new Callback<QueryResponse>() {
            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                if (offset != 0 && !mDataList.isEmpty()) {
                    mDataList.remove(mDataList.size() - 1);
                    mAdapter.notifyItemRemoved(mDataList.size());
                }
                if (isFromQueryChange) {
                    mProgressBar.setVisibility(View.GONE);
                    mOffSet = 0;
                    mDataList.clear();
                }
                if (response != null && response.body() != null) {

                    QueryResponse queryResponse = response.body();
                    if (queryResponse != null && queryResponse.getQuery() != null) {
                        mOffSet += 10;
                        List<Page> pageList = queryResponse.getQuery().getPages();

                        checkAndUpdateList(pageList);
                        if (pageList.size() > 0) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    updateViews();

                } else {
                    Toast.makeText(HomeActivity.this, "Api returned null response", Toast.LENGTH_SHORT).show();
                }

                mAdapter.setLoaded();
            }

            private void checkAndUpdateList(List<Page> pages) {
                for (Page page : pages) {
                    SuggestionItemDataModel item = new SuggestionItemDataModel();
                    String title = page.getTitle();
                    item.setTitle(title);
                    Terms terms = page.getTerms();
                    if (terms != null && terms.getDescription() != null && terms.getDescription().size() > 0) {
                        String desc = terms.getDescription().get(0);
                        item.setDesc(desc);
                    }
                    if (page.getThumbnail() != null && !TextUtils.isEmpty(page.getThumbnail().getSource())) {
                        item.setImageUrl(page.getThumbnail().getSource());
                    }
                    mDataList.add(item);
                }
            }

            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                if (isFromQueryChange) {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (offset != 0 && !mDataList.isEmpty()) {
                    mDataList.remove(mDataList.size() - 1);
                    mAdapter.notifyItemRemoved(mDataList.size());
                }
                mAdapter.setLoaded();
                if (t != null && BuildConfig.DEBUG) {
                    t.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Api returned null response", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndRemoveLoadingElement(int offset) {
        if (offset != 0 && !mDataList.isEmpty()) {
            mDataList.remove(mDataList.size() - 1);
            mAdapter.notifyItemRemoved(mDataList.size());
        }
    }

    private void updateViews() {
        if (mDataList.isEmpty()) {
            showEmptyView();
        } else {
            showDataView();
        }
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
    }

    private Map<String, String> getDefaultQueryMap(String newText) {
        Map<String, String> map = new HashMap<>();
        map.put("action", "query");
        map.put("format", "json");
        map.put("prop", "pageimages|pageterms");
        map.put("generator", "prefixsearch");
        map.put("redirects", "1");
        map.put("formatversion", "2");
        map.put("piprop", "thumbnail");
        map.put("pithumbsize", "100");
        map.put("pilimit", "10");
        map.put("wbptterms", "description");
        map.put("gpssearch", newText);
        map.put("gpslimit", "10");
        return map;
    }

    @Override
    public void onClick(String itemUri) {
        String modifiedStr = itemUri.replaceAll("\\s+", "_");
        Intent intent = new Intent();
        intent.setClass(this, SearchableResultActivity.class);
        intent.putExtra("title", modifiedStr);
        startActivity(intent);
    }
}
