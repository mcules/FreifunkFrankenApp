package de.itstall.freifunkfranken.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.NewsAdapter;
import de.itstall.freifunkfranken.controller.NewsRequest;
import de.itstall.freifunkfranken.model.News;

public class NewsFragment extends Fragment {
    private RecyclerView rvNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);

        rvNews = rootView.findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<News> newsList = new NewsRequest(Objects.requireNonNull(this.getContext())).getNewsList();
        showNewsList(newsList);

        return rootView;
    }

    private void showNewsList(List<News> newsList) {
        NewsAdapter newsAdapter = new NewsAdapter(newsList);
        rvNews.setAdapter(newsAdapter);
        rvNews.setItemAnimator(new DefaultItemAnimator());
    }
}
