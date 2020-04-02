package de.itstall.freifunkfranken.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
    private OnItemClicked onClick;

    public NextApAdapter(List<AccessPoint> aps) {
        this.accessPointList = aps;
    }

    @NonNull
    @Override
    public NextApAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View viewApsItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.nextap_item, null);

        return new ViewHolder(viewApsItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(NextApAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvAp.setText(accessPointList.get(position).getName());
        viewHolder.tvStatus.setText(
                accessPointList.get(position)
                        .isOnline() ? R.string.statusOnline : R.string.statusOffline
        );

        double distance = accessPointList.get(position).getDistance();
        String distanceText = "";

        if (distance > 999) distanceText = String.format("%3.2f km", distance / 1000);
        else distanceText = String.format("%3.0f m", distance);

        viewHolder.tvDistance.setText(distanceText);

        viewHolder.tvStatus.setTextColor(
                (accessPointList.get(position).isOnline()) ? Color.GREEN : Color.RED);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return accessPointList.size();
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAp;
        TextView tvStatus;
        TextView tvDistance;

        ViewHolder(View itemView) {
            super(itemView);
            tvAp = itemView.findViewById(R.id.nextApTvSsid);
            tvStatus = itemView.findViewById(R.id.nextApTvStatus);
            tvDistance = itemView.findViewById(R.id.nextApTvDistance);
        }
    }
}
