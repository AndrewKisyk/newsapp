package com.example.a123.newsview.api;
import com.example.a123.newsview.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface NewsApi {
    @GET("https://newsapi.org/v2/top-headlines?country=ua&apiKey=51b157c59c084aedaea1854740e3037a")
    public Call<News> getAllNews();
}
