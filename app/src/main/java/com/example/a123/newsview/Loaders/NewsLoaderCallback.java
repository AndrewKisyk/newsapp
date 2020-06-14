package com.example.a123.newsview.Loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a123.newsview.Adapters.ArticleAdapter;
import com.example.a123.newsview.RecyclerViewClickListener;
import com.example.a123.newsview.SearchViewQueryTextListener;
import com.example.a123.newsview.model.News;

public class NewsLoaderCallback implements LoaderManager.LoaderCallbacks<News> {
    Context context;
    private RecyclerView itemView;
    private ProgressDialog progressDialog;
    private SearchView search;
    private RecyclerViewClickListener mListener;
    private SearchViewQueryTextListener mtextListener;
    public NewsLoaderCallback(Context context, RecyclerView itemView, ProgressDialog progressDialog, SearchView search, RecyclerViewClickListener listener, SearchViewQueryTextListener textListener){
        super();
        this.context = context;
        this.itemView = itemView;
        this.progressDialog = progressDialog;
        this.search = search;
        mListener = listener;
        mtextListener = textListener;
    }
    @NonNull
    @Override
    public Loader<News> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new NewsLoader(context);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<News> loader, News news) {
        showNews(news);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<News> loader) {

    }
    private void showNews(@Nullable News news){
        if(news == null){
            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.dismiss();
        final ArticleAdapter adapter = new ArticleAdapter(context, news.getArticles(), mListener);
        itemView.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               mtextListener.onQueryTextChange(s, adapter);
                return false;
            }
        });
    }
}
