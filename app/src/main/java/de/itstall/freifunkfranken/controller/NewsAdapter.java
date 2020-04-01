package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String TAG = NewsAdapter.class.getSimpleName();
    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View viewNewsItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, null);

        return new ViewHolder(viewNewsItem);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvNewsDate.setText(newsList.get(position).getDate());
        viewHolder.tvNewsTitle.setText(newsList.get(position).getTitle());
        viewHolder.tvNewsDescription.setText(Html.fromHtml(
                newsList.get(position).getDescription() +
                        "<br><br><a href=\"" + newsList.get(position).getLink() + "\">" +
                        viewHolder.itemView.getContext().getString(R.string.newsArticleRead) +
                        "</a>"
        ));
        viewHolder.tvNewsDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNewsTitle;
        TextView tvNewsDate;
        TextView tvNewsDescription;

        ViewHolder(View viewNewsItem) {
            super(viewNewsItem);
            tvNewsDate = viewNewsItem.findViewById(R.id.newsTvDate);
            tvNewsTitle = viewNewsItem.findViewById(R.id.newsTvTitle);
            tvNewsDescription = viewNewsItem.findViewById(R.id.newsTvDescription);
        }
    }
}
