package com.hfad.simplenews.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.simplenews.Common;
import com.hfad.simplenews.R;
import com.hfad.simplenews.model.Article;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    List<Article> articleList;

    public NewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_news_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(articleList.get(position).getSource().getName());
        holder.author.setText(articleList.get(position).getAuthor());
        holder.date.setText(Common.formattedDate(articleList.get(position).getPublishedAt()));
        holder.title.setText(articleList.get(position).getTitle());
        holder.description.setText(articleList.get(position).getDescription());

        Glide.with(context).load(articleList.get(position).getUrlToImage()).into(holder.news_image);

        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Uri uri = Uri.parse(articleList.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView news_image;
        TextView name, date, author, title, description;
        ItemClickListener listener;

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            news_image = (ImageView)itemView.findViewById(R.id.news_image);
            name = (TextView)itemView.findViewById(R.id.name);
            date = (TextView)itemView.findViewById(R.id.date);
            author = (TextView)itemView.findViewById(R.id.author);
            title = (TextView)itemView.findViewById(R.id.title_news);
            description = (TextView)itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
