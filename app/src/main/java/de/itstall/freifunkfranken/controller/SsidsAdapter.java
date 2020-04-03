package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
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
    private final List<Ssid> ssidList;

    public SsidsAdapter(List<Ssid> ssids) {
        this.ssidList = ssids;
    }

    // viewHolder constructor
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View viewSsidsItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.ssids_item, null);

        return new ViewHolder(viewSsidsItem);
    }

    // sets content for fields in view
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvSsid.setText(ssidList.get(position).getSsid());
    }

    // bind fileds to viewHolder
    @Override
    public int getItemCount() {
        return ssidList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvSsid;

        ViewHolder(View itemView) {
            super(itemView);
            tvSsid = itemView.findViewById(R.id.nextApSsid);
        }
    }
}
