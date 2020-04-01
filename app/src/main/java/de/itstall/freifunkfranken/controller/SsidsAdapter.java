package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.itstall.freifunkfranken.R;
import de.itstall.freifunkfranken.model.Ssid;

public class SsidsAdapter extends RecyclerView.Adapter<SsidsAdapter.ViewHolder> {
    private List<Ssid> ssidList;
    private WifiManager wifiManager;

    public SsidsAdapter(List<Ssid> ssids) {
        this.ssidList = ssids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View viewSsidsItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.ssids_item, null);

        return new ViewHolder(viewSsidsItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvSsid.setText(ssidList.get(position).getSsid());
    }

    @Override
    public int getItemCount() {
        return ssidList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSsid;

        ViewHolder(View itemView) {
            super(itemView);
            tvSsid = itemView.findViewById(R.id.nextApSsid);
        }
    }
}
