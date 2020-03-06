package com.example.blog.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BlogHttpClient {
    public static final BlogHttpClient INSTANCE = new BlogHttpClient();

    private static final String BASE_URL =
            "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/";
    private static final String BLOG_ARTICLES_URL =
            BASE_URL + "8550ef2064bf14fcf3b9ff322287a2e056c7e153/blog_articles.json";

    private Executor executor;
    private OkHttpClient client;
    private Gson gson;

    private BlogHttpClient() {
        executor = Executors.newFixedThreadPool(4);
        client = new OkHttpClient();
        gson = new Gson();
    }

    /**
    (1) create a Request object which defines the type of request and URL
    (2) use Executor to execute code on the background thread
    (3) execute an OkHttpClient request to get the Response and ResponseBody
    (4) if ResponseBody is not null, we use Gson to parse the JSON by providing JSON String object and class of the result data BlogData.class
    (5) the whole network call section is wrapped by try/catch to catch the exception and log it via Log.e method
    */
    public void loadBlogArticles(BlogArticlesCallback callback) {
        Request request = new Request.Builder() // 1
                .get()
                .url(BLOG_ARTICLES_URL)
                .build();

        executor.execute(() -> { // 2
            try {
                Response response = client.newCall(request).execute(); // 3
                ResponseBody responseBody = response.body();
                if (responseBody != null) { // 4
                    String json = responseBody.string();
                    BlogData blogData = gson.fromJson(json, BlogData.class);
                    if (blogData != null) {
                        callback.onSuccess(blogData.getData());
                        return;
                    }
                }
            } catch (IOException e) { // 5
                Log.e("BlogHttpClient", "Error loading blog articles", e);
            }
            callback.onError();
        });
    }
}
