package com.example.blog.repository;

import android.content.Context;

import com.example.blog.database.AppDatabase;
import com.example.blog.database.BlogDAO;
import com.example.blog.database.DatabaseProvider;
import com.example.blog.http.Blog;
import com.example.blog.http.BlogHttpClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BlogRepository {

    private BlogHttpClient httpClient;
    private AppDatabase database;
    private Executor executor;

    public BlogRepository(Context context) {
        httpClient = BlogHttpClient.INSTANCE;
        database = DatabaseProvider.getInstance(context.getApplicationContext());
        executor = Executors.newSingleThreadExecutor();
    }

    public void loadDataFromDatabase(DataFromDatabaseCallback callback) {
        executor.execute(() -> callback.onSuccess(database.blogDao().getAll()));
    }

    public void loadDataFromNetwork(DataFromNetworkCallback callback) {
        executor.execute(() -> {
            List<Blog> blogList = httpClient.loadBlogArticles();
            if (blogList == null) {
                callback.onError(); // 1
            } else {
                BlogDAO blogDAO = database.blogDao();
                blogDAO.deleteAll(); // 2
                blogDAO.insertAll(blogList); // 3
                callback.onSuccess(blogList); // 4
            }
        });
    }
}