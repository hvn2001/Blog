package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.blog.adapter.MainAdapter;
import com.example.blog.http.Blog;
import com.example.blog.http.BlogArticlesCallback;
import com.example.blog.http.BlogHttpClient;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.DataFromNetworkCallback;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private static final int SORT_TITLE = 0; // 1
    private static final int SORT_DATE = 1; // 1

    private int currentSort = SORT_DATE; // 2

    private BlogRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new BlogRepository(getApplicationContext()); // 1

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked(); // implemented later in this lesson
            }
            return false;
        });
        // startActivity(new Intent(this, BlogDetailsActivity.class));
        adapter = new MainAdapter(blog ->
                BlogDetailsActivity.startBlogDetailsActivity(this, blog));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout = findViewById(R.id.refresh);
        // refreshLayout.setOnRefreshListener(this::loadData); // 1
        // loadData();
        refreshLayout.setOnRefreshListener(this::loadDataFromNetwork); // 2
        loadDataFromDatabase(); // 3
        loadDataFromNetwork(); // 4

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search); // 1
        SearchView searchView = (SearchView) searchItem.getActionView(); // 2
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // 3
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // 4
                return true;
            }
        });
    }

    private void loadDataFromDatabase() {
        repository.loadDataFromDatabase(blogList -> runOnUiThread(() -> {
            adapter.setData(blogList);
            sortData();
        }));
    }

    private void loadDataFromNetwork() {
        refreshLayout.setRefreshing(true); // 1

        repository.loadDataFromNetwork(new DataFromNetworkCallback() { // 2
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> { // 3
                    adapter.setData(blogList);
                    sortData();
                    refreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onError() {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    showErrorSnackbar();
                });
            }
        });
    }

    private void onSortClicked() {
        String[] items = {"Title", "Date"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Sort order")
                .setSingleChoiceItems(items, currentSort, (dialog, which) -> {
                    dialog.dismiss();
                    currentSort = which;
                    sortData();
                }).show();
    }

    private void sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle();
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate();
        }
    }

    /**
     * pull-to-refresh listener via the setOnRefreshListener method (1)
     * When this listener is triggered, we can simply execute the loadData method to reload the data.
     * To make the loader indicator stay on the screen while data is loading,
     * we can use the setRefreshing(true) method (2) and
     * when the data loading has been completed, we can use the setRefreshing(false) method (3) (4).
     */
    /*private void loadData() {
        refreshLayout.setRefreshing(true); // 2
        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogArticlesCallback() {
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    adapter.setData(blogList); // 1
                    sortData();
                    // adapter.submitList(blogList);
                });
            }

            @Override
            public void onError() {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false); // 4
                    showErrorSnackbar();
                });
            }
        });
    }*/

    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "Error during loading blog articles list", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.orange500));
        snackbar.setAction("Retry", v -> {
            // loadData();
            loadDataFromNetwork();
            snackbar.dismiss();
        });
        snackbar.show();
    }
}