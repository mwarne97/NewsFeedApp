package com.example.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<ArticleClass>> {

    private String mUrl;

    public ArticleLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<ArticleClass> loadInBackground() {
        if (mUrl == null) {
            return null;
        }//End If
        List<ArticleClass> results = QueryUtils.grabArticleData(mUrl);
        return results;
    }//End loadBackground

}//End
