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
import de.itstall.freifunkfranken.model.AccessPoint;

public class NextApAdapter extends RecyclerView.Adapter<NextApAdapter.ViewHolder> {
    private static final String TAG = NextApAdapter.class.getSimpleName();
    private List<AccessPoint> accessPointList;

    public NextApAdapter(List<AccessPoint> aps) {
        this.accessPointList = aps;
    }

    @NonNull
    @Override
    public NextApAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View viewApsItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.nextap_item, null);

        return new ViewHolder(viewApsItem);
    }

    @Override
    public void onBindViewHolder(NextApAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvAp.setText(accessPointList.get(position).getName());
        if (accessPointList.get(position).isOnline()) {
            viewHolder.tvStatus.setText(R.string.statusOnline);
        } else {
            viewHolder.tvStatus.setText(R.string.statusOffline);
        }
        //viewHolder.tvDistance.setText(String.format("%dm", accessPointList.get(position).getDistance(NextApFragment.myLocationListener.latitude, NextApFragment.myLocationListener.longitude)));
    }

    @Override
    public int getItemCount() {
        return accessPointList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAp;
        TextView tvStatus;
        TextView tvDistance;

        ViewHolder(View itemView) {
            super(itemView);
            tvAp = itemView.findViewById(R.id.tvNewsTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDistance = itemView.findViewById(R.id.tvNewsDate);
        }
    }
}
