package de.itstall.freifunkfranken.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import de.itstall.freifunkfranken.R;

public class VpnFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vpn_fragment, container, false);

        WebView vpnWv = rootView.findViewById(R.id.vpnWv);
        vpnWv.loadUrl("https://vpn.freifunk-franken.de");
        vpnWv.setWebViewClient(new WebViewClient());

        return rootView;
    }
}
