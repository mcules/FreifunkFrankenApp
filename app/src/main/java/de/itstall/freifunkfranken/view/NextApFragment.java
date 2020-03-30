package de.itstall.freifunkfranken.view;

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
import de.itstall.freifunkfranken.controller.MyLocationListener;
import de.itstall.freifunkfranken.controller.NextApAdapter;
import de.itstall.freifunkfranken.model.AccessPoint;
import de.itstall.freifunkfranken.model.RequestAps;

public class NextApFragment extends Fragment {
    private RecyclerView rvAps;
    private View rootView;
    private TextView tvTemp;
    //public MyLocationListener myLocationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nextap_fragment, container, false);

        //myLocationListener = new MyLocationListener(rootView);

        rvAps = rootView.findViewById(R.id.rvAps);
        rvAps.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvTemp = rootView.findViewById(R.id.tvTemp);

        List<AccessPoint> accessPointList = new RequestAps(Objects.requireNonNull(this.getContext())).getAccessPointList();
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
