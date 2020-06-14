package com.example.a123.newsview.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.a123.newsview.R;
import com.example.a123.newsview.RecyclerViewClickListener;
import com.example.a123.newsview.SearchViewQueryTextListener;
import com.example.a123.newsview.model.Article;

import java.util.ArrayList;
import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> implements Filterable {
    private List<Article> mArticle;

    private List<Article> mArticleFull;
    private RecyclerViewClickListener mListener;
    Context context;

    public ArticleAdapter(Context context, List<Article> articles, RecyclerViewClickListener listener ) {
        this.context = context;
        mArticle = articles;
        mArticleFull = new ArrayList<>(articles);
        mListener = listener;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.news_row, viewGroup, false);
        return new ArticleHolder(context, view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder articleHolder, int i) {
        Article article = mArticle.get(i);
        articleHolder.bindArticle(article);
    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Article> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mArticleFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Article article : mArticleFull) {
                    if(article.getTitle().toLowerCase().contains(filterPattern)
                            || article.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(article);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mArticle.clear();
            if((List)filterResults.values != null)
            mArticle.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}