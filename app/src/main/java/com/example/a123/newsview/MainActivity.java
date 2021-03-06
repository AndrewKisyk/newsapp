package com.example.a123.newsview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a123.newsview.Adapters.ArticleAdapter;
import com.example.a123.newsview.Loaders.NewsLoaderCallback;
import com.example.a123.newsview.model.Article;
import com.example.a123.newsview.model.News;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private RecyclerViewClickListener recyclerlistener;
    private SearchViewQueryTextListener textListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //customizing the search view
        final SearchView search = (SearchView) findViewById(R.id.searchView);
        initView(search);

        initListeners();

        recyclerView = (RecyclerView) findViewById(R.id.news);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (InternetConnection.checkConnection(MainActivity.this.getApplicationContext())) {

            progressDialog = new ProgressDialog(MainActivity.this);

            loadNews(false, search);
        } else
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    private void loadNews(boolean restart, SearchView search) {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

            LoaderManager.LoaderCallbacks<News> callbacks = new NewsLoaderCallback(MainActivity.this, recyclerView, progressDialog, search,
                    recyclerlistener, textListener);
        if (restart) {
            getSupportLoaderManager().restartLoader(1, Bundle.EMPTY, callbacks);
        } else {
            getSupportLoaderManager().initLoader(1, Bundle.EMPTY, callbacks);
        }
    }


    private void initView(final SearchView msearch){
        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) msearch.findViewById(id);
        if (searchEditText != null) {
            searchEditText.setGravity(Gravity.CENTER);
        }
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msearch.setIconified(false);
            }
        });
        msearch.onActionViewExpanded();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msearch.clearFocus();
            }
        }, 300);
        int searchPlateId = getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = msearch.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void initListeners(){
        this.recyclerlistener = (view, position, article) -> {
            initRecyclerViewListener(view, position, article);
        };

        this.textListener = (s, adapter)-> {
            initSearchViewListenerChange(s, adapter);
        };
    }

    private void initSearchViewListenerChange(String s, ArticleAdapter adapter){
        adapter.getFilter().filter(s);

    }

    private void initRecyclerViewListener(View view, int position, Article article){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
        this.startActivity(browserIntent);
    }
}