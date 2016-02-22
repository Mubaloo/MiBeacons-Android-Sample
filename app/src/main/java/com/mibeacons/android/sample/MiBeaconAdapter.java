package com.mibeacons.android.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mibeacons.sdk.model.MiBeacon;

import java.util.ArrayList;
import java.util.List;

public class MiBeaconAdapter extends RecyclerView.Adapter<MiBeaconAdapter.ViewHolder> {
    private Context context;
    private List<MiBeacon> miBeaconList = new ArrayList<>();

    public MiBeaconAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mibeacon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MiBeacon miBeacon = miBeaconList.get(position);
        holder.majorMinor.setText(String.format(context.getString(R.string.mibeacon_major_minor), miBeacon.getMajor(), miBeacon.getMinor()));
        holder.proximity.setText(String.format(context.getString(R.string.mibeacon_proximity), miBeacon.getCurrentProx().name()));
        holder.uuid.setText(String.format(context.getString(R.string.mibeacon_uuid), miBeacon.getUuid()));
    }

    @Override
    public int getItemCount() {
        return miBeaconList.size();
    }

    public void setBeacons(List<MiBeacon> miBeacons) {
        miBeaconList.clear();
        miBeaconList.addAll(miBeacons);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView majorMinor;
        TextView proximity;
        TextView uuid;

        public ViewHolder(View itemView) {
            super(itemView);
            majorMinor = (TextView) itemView.findViewById(R.id.mibeacon_major_minor);
            proximity = (TextView) itemView.findViewById(R.id.mibeacon_proximity);
            uuid = (TextView) itemView.findViewById(R.id.mibeacon_uuid);
        }
    }
}
