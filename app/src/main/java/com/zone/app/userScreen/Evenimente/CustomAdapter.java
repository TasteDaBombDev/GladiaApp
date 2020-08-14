package com.zone.app.userScreen.Evenimente;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.GroundOverlay;
import com.squareup.picasso.Picasso;
import com.zone.app.R;
import com.zone.app.utils.AVLtree;
import com.zone.app.utils.CustomInfoAdapter;
import com.zone.app.utils.EventsDetails;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<EventsDetails> database;
    Context context;

    public CustomAdapter(Context context, AVLtree<EventsDetails> database) {
        this.context = context;
        this.database = database.toArrayList();
    }

    public CustomAdapter(Context context, ArrayList<EventsDetails> database) {
        this.context = context;
        this.database = database;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.name.setText(database.get(position).getName());
        Picasso.get().load(database.get(position).getPoza()).into(holder.image);

        holder.itemView.setOnClickListener(view -> {

        });

        holder.date.setText(database.get(position).getDate() + "\n" + database.get(position).getOra());
        holder.type.setText(database.get(position).getType().toUpperCase());
        String k = ((int)(database.get(position).getDistance()*10))/10 + " Km";
        holder.dist.setText(k);

//        holder.root.removeAllViews();
//        String[] s = detalii.get(position).split("\\|");
//        for (int i = 0; i < s.length; i++) {
//            TextView t = new TextView(context);
//            t.setText(s[i].trim());
//            holder.root.addView(t);
//        }
    }

    @Override
    public int getItemCount() {
        return database.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, date, type, dist;
        ImageView image;
        LinearLayout root;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.data);
            root = itemView.findViewById(R.id.root);
            dist = itemView.findViewById(R.id.distance);
        }
    }
}
