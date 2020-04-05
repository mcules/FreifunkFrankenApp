package de.itstall.freifunkfranken.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.SsidsAdapter;
import de.itstall.freifunkfranken.controller.SsidsFragmentListener;
import de.itstall.freifunkfranken.controller.SsidsRequest;
import de.itstall.freifunkfranken.model.Ssid;

// Shows the SSID list and adds to mobile
public class SsidsFragment extends androidx.fragment.app.Fragment {
    // --Commented out by Inspection (05.04.2020 22:13):private static final String TAG = SsidsFragment.class.getSimpleName();
    public static List<Ssid> ssidList;
    private RecyclerView rvSsids;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ssids_fragment, container, false);
        rvSsids = rootView.findViewById(R.id.rvSsids);
        rvSsids.setLayoutManager(new LinearLayoutManager(getActivity()));

        SsidsFragmentListener ssidsFragmentListener = new SsidsFragmentListener();

        Button ssidsFragmentBtnAddAll = rootView.findViewById(R.id.ssidsFragmentBtnAddAll);
        ssidsFragmentBtnAddAll.setOnClickListener(ssidsFragmentListener);

        // Request SSID list
        ssidList = new SsidsRequest(Objects.requireNonNull(this.getContext())).getSsidList();

        // Show SSID list
        showSsidList(ssidList);

        return rootView;
    }

    // bind list to view
    private void showSsidList(List<Ssid> ssidList) {
        SsidsAdapter ssidsAdapter = new SsidsAdapter(ssidList);
        rvSsids.setAdapter(ssidsAdapter);
        rvSsids.setItemAnimator(new DefaultItemAnimator());
    }
}