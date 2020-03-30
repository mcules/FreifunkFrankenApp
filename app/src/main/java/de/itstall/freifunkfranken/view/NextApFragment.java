package de.itstall.freifunkfranken.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.controller.NextApAdapter;
import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.controller.RequestAps;

public class NextApFragment extends Fragment {
    private RecyclerView rvAps;
    private View rootView;
    private TextView tvTemp;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nextap_fragment, container, false);

        sharedPreferences = rootView.getContext().getSharedPreferences("FreifunkFrankenApp", 0);


        rvAps = rootView.findViewById(R.id.rvAps);
        rvAps.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvTemp = rootView.findViewById(R.id.tvTemp);

        List<AccessPoint> accessPointList = new RequestAps(Objects.requireNonNull(this.getContext())).getSortedList(sharedPreferences.getBoolean("OfflineRouter", false), sharedPreferences.getInt("RouterCount", 10));
        showApList(accessPointList);

        return rootView;
    }

    private void showApList(List<AccessPoint> accessPointList) {
        NextApAdapter nextApAdapter = new NextApAdapter(accessPointList);
        rvAps.setAdapter(nextApAdapter);
        rvAps.setItemAnimator(new DefaultItemAnimator());
        tvTemp.setText(String.valueOf(accessPointList.size()));
    }
}
