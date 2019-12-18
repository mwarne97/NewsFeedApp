package com.example.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleItemAdapter extends ArrayAdapter<ArticleClass> {

    public ArticleItemAdapter(Context context, List<ArticleClass> article) {
        super(context, 0, article);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;

        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.news_article_item,
                    parent, false);
        }//End If

        ArticleClass articleInfo = getItem(position);

        TextView articleTitle = currentView.findViewById(R.id.news_article_title);
        articleTitle.setText(articleInfo.getArticleTitle());

        TextView articleSection = currentView.findViewById(R.id.news_article_section);
        articleSection.setText(articleInfo.getArticleSection());

        TextView articlePublishDate = currentView.findViewById(R.id.news_article_publish_date);

        if (articleInfo.getArticlePublishDate() == null) {
            articlePublishDate.setVisibility(View.GONE);
        }

        String date = articleInfo.getArticlePublishDate();
        String newDate = formatDate(date);

        articlePublishDate.setText(newDate);

        return currentView;
    }//End getView

    private String formatDate(String dateObject) {
        String dateFormatted = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH;mm:ss'Z'",
                Locale.getDefault());

        SimpleDateFormat outputDate = new SimpleDateFormat("EEEE, dd.MM.yyyy",
                Locale.getDefault());

        try {
            Date date = inputDate.parse(dateObject);
            return outputDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }//End formatDate

}//End Main
