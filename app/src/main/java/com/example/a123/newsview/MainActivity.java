package com.example.a123.newsview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123.newsview.api.NetworkService;
import com.example.a123.newsview.model.Article;
import com.example.a123.newsview.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //customizing the search view
        final SearchView search = (SearchView) findViewById(R.id.searchView);
        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search.findViewById(id);
        if (searchEditText != null) {
            searchEditText.setGravity(Gravity.CENTER);
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });
        search.onActionViewExpanded();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search.clearFocus();
            }
        }, 300);
        int searchPlateId = getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = search.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        }


        recyclerView = (RecyclerView) findViewById(R.id.news);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (InternetConnection.checkConnection(MainActivity.this.getApplicationContext())) {

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    return false;
                }
            });

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            NetworkService.getInstance()
                    .getNewsApi()
                    .getAllNews()
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<News> call, @NonNull Response<News> response) {
                            progressDialog.dismiss();
                            News news = response.body();

                            adapter = new ArticleAdapter(news.getArticles());
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<News> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Eror", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    private class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView time;
        public TextView source;
        public TextView description;
        public ImageView newsImage;
        public ImageView href;

        private Article marticle;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = (ImageView) itemView.findViewById(R.id.news_image);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            source = (TextView) itemView.findViewById(R.id.source);
            description = (TextView) itemView.findViewById(R.id.description);
            href = (ImageView) itemView.findViewById(R.id.href);
            href.setOnClickListener(this);

        }

        public void bindArticle(Article article) {
            marticle = article;

            Picasso.with(MainActivity.this)
                    .load(article.getUrlToImage())
                    .into(newsImage);
            title.setText(marticle.getTitle());
            String s = marticle.getPublishedAt();
            time.setText(s.substring(11, s.length() - 4)); //The full date is too long, displayed only a hour
            source.setText(marticle.getSource().getName());
            description.setText(marticle.getDescription());
        }

        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marticle.getUrl()));
            startActivity(browserIntent);
        }
    }

    private class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> implements Filterable {
        private List<Article> mArticle;

        private List<Article> mArticleFull;

        public ArticleAdapter(List<Article> articles) {
            mArticle = articles;
            mArticleFull = new ArrayList<>(articles);
        }

        @NonNull
        @Override
        public ArticleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.news_row, viewGroup, false);
            return new ArticleHolder(view);
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
                mArticle.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}
