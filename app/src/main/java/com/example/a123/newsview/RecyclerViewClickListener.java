package com.example.a123.newsview;

import android.view.View;

import com.example.a123.newsview.model.Article;

public interface RecyclerViewClickListener {
    void onClick(View view, int position, Article article);
}
