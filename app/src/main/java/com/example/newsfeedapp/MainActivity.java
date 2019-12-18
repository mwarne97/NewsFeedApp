package com.example.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<ArticleClass>> {

    private static ArticleItemAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    private static final int ARTICLE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = findViewById(R.id.negative_message_txt_view);

        ListView listView = findViewById(R.id.list_view);
        mAdapter = new ArticleItemAdapter(this, new ArrayList<ArticleClass>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArticleClass currentListItem = mAdapter.getItem(i);

                Uri articleUri = Uri.parse(currentListItem.getArticleUrl());

                Intent websiteIntention = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(websiteIntention);
            }//End onItemClick
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mEmptyStateTextView.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_ID, null, this);
        }//End If
        else {
            View loadingIndicator = findViewById(R.id.loading_bar);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection_txt);
        }//End Else
    }//End onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }//End If
        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected

    @NonNull
    @Override
    public Loader<List<ArticleClass>> onCreateLoader(int i, @Nullable Bundle bundle) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sectionInfo = preferences.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String orderBy = preferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("section", sectionInfo);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        uriBuilder.appendQueryParameter("api-key", "08d39df2-2b43-41bf-8fa8-1c8b8376ba3c");

        return new ArticleLoader(this, uriBuilder.toString());
    }//End onCreateLoader

    @Override
    public void onLoadFinished(@NonNull Loader<List<ArticleClass>> loader, List<ArticleClass> articleClasses) {
        View loadingIndicator = findViewById(R.id.loading_bar);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if (articleClasses != null || !articleClasses.isEmpty()) {
            mAdapter.addAll(articleClasses);
        }//End If
        else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_news_feed_txt);
        }//End Else

    }//End onLoadFinished

    @Override
    public void onLoaderReset(@NonNull Loader<List<ArticleClass>> loader) {
        mAdapter.clear();
    }//End onLoaderReset

}//End
