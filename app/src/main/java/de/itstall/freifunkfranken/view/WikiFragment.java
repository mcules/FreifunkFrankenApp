package de.itstall.freifunkfranken.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import de.itstall.freifunkfranken.R;

public class WikiFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wiki_fragment, container, false);

        WebView wikiWv = rootView.findViewById(R.id.wikiWv);
        wikiWv.getSettings().setJavaScriptEnabled(true);
        wikiWv.loadUrl("https://wiki.freifunk-franken.de");
        wikiWv.setWebViewClient(new WebViewClient());

        return rootView;
    }
}
