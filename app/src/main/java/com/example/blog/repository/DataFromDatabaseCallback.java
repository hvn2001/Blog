package com.example.blog.repository;

import com.example.blog.http.Blog;

import java.util.List;

public interface DataFromDatabaseCallback {
    void onSuccess(List<Blog> blogList);
}