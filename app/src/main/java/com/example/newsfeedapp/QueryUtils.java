package com.example.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {

    public QueryUtils() {
    }

    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e("", "Issue creating URL", e);
        }
        return url;
    }//End

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }//End If

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }//End If
            else {
                Log.e("Response Code Error", "Error Response Code: " + urlConnection.getResponseCode());
            }//End Else
        } catch (IOException e) {
            Log.e("Response Code Error", "Error Response Code: " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }//End If
            if (inputStream != null) {
                inputStream.close();
            }//End If
        }//End Finally
        return jsonResponse;
    }//End makeHttpRequest

    public static List<ArticleClass> grabArticleData(String urlRequest) {

        URL mUrl = createURL(urlRequest);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e("Http Request Error", "Issue with HTTP request", e);
        }

        List<ArticleClass> articleClass = extractFromJson(jsonResponse);
        return articleClass;
    }//End grabArticleData

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }//End While
        }//End If
        return output.toString();
    }//End readFromStream

    private static List<ArticleClass> extractFromJson(String jsonInfo) {

        if (TextUtils.isEmpty(jsonInfo)) {
            return null;
        }//End If

        List<ArticleClass> articleClass = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonInfo);
            JSONObject baseJsonResults = baseJsonResponse.getJSONObject("response");
            JSONArray articleArray = baseJsonResults.getJSONArray("results");

            for (int index = 0; index < articleArray.length(); index++) {
                JSONObject currentArticle = articleArray.getJSONObject(index);
                String section = currentArticle.getString("sectionName");
                String title = currentArticle.getString("webTitle");
                String date = currentArticle.getString("webPublicationDate");
                String url = currentArticle.getString("webUrl");

                ArticleClass article = new ArticleClass(title, section, date, url);
                articleClass.add(article);
            }//End For

        } catch (JSONException e) {
            Log.e("", "Issue Parsing JSON", e);
        }
        return articleClass;
    }//End extractFromJson

}//End
