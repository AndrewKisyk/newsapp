package com.example.a123.newsview;

import android.widget.SearchView;

import com.example.a123.newsview.Adapters.ArticleAdapter;
import com.example.a123.newsview.model.Article;
@FunctionalInterface
public interface SearchViewQueryTextListener  {
    void onQueryTextChange(String s, ArticleAdapter adapter);
}
