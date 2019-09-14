package com.example.a123.newsview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a123.newsview.R;
import com.example.a123.newsview.model.Article;
import com.squareup.picasso.Picasso;


public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView title;
    public TextView time;
    public TextView source;
    public TextView description;
    public ImageView newsImage;
    public ImageView href;

    private Article marticle;
    Context context;

    public ArticleHolder(Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;
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

        Picasso.with(context)
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
        ArticleHolder.this.context.startActivity(browserIntent);
    }
}
