package com.hfad.simplenews;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hfad.simplenews.adapter.NewsAdapter;
import com.hfad.simplenews.model.Article;
import com.hfad.simplenews.model.TopNews;
import com.hfad.simplenews.retrofit.NewsService;
import com.hfad.simplenews.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "a7d4dd5cb94945459fb62ea34e344e89";
    private final static String COUNTRY = "ua";
    private final static String LANGUAGE = "en";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article>articleList;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        onLoadingRefresh("");
    }

    private void showAllNews(final String keyword) {
        refreshLayout.setRefreshing(true);
        NewsService service = RetrofitClient.getInstance().create(NewsService.class);
        Call<TopNews>call;
        if (keyword.length() > 0){
            call = service.getNewsSearch(keyword, LANGUAGE, "publishedAt", API_KEY);
        }else {
            call = service.getAllNews(COUNTRY, API_KEY);
        }
        call.enqueue(new Callback<TopNews>() {
            @Override
            public void onResponse(Call<TopNews> call, Response<TopNews> response) {
                TopNews topNews = response.body();
                articleList = (List<Article>)topNews.getArticles();
                showOnRecyclerView();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<TopNews> call, Throwable t) {
                Log.e(TAG, "Фатальна помилка, неможливо завантажити дані");
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void showOnRecyclerView() {
        recyclerView = findViewById(R.id.recycler);
        adapter = new NewsAdapter(this, articleList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search news...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){
                    onLoadingRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    @Override
    public void onRefresh() {
        showAllNews("");
    }

    private void onLoadingRefresh(final String keyword){
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                showAllNews(keyword);
            }
        });
    }
}
