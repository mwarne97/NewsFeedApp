package com.example.newsfeedapp;

public class ArticleClass {

    private String articleTitle;
    private String articleSection;
    private String articlePublishDate;
    private String articleUrl;

    public ArticleClass(String articleTitle, String articleSection, String articlePublishDate, String articleUrl) {
        this.articleTitle = articleTitle;
        this.articleSection = articleSection;
        this.articlePublishDate = articlePublishDate;
        this.articleUrl = articleUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticlePublishDate() {
        return articlePublishDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getArticleSection() {
        return articleSection;
    }

}//End
