package com.example.a123.newsview.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.a123.newsview.api.NetworkService;
import com.example.a123.newsview.model.Article;
import com.example.a123.newsview.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsLoader extends Loader<News> {
    private static final String TAG = "NewsLoader.java";
    private final Call<News> call;
    @Nullable
    private News news;
    public NewsLoader(@NonNull Context context) {
        super(context);
        call =  NetworkService.getInstance().getNewsApi().getAllNews();
    }

    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        if(news != null)
            deliverResult(news);
        else
            forceLoad();
    }

    @Override
    protected void onForceLoad(){
        super.onForceLoad();

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                news = response.body();
                deliverResult(news);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG,"On failure");
                deliverResult(null);
            }
        });
    }

    @Override
    protected void onStopLoading(){
        call.cancel();
        super.onStopLoading();
    }
}
