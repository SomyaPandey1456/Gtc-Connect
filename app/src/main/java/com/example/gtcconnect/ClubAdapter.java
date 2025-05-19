package com.example.gtcconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class ClubAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<Club> clubs;

    public ClubAdapter(Context context, List<Club> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @Override
    public int getCount() {
        return clubs.size();
    }

    @Override
    public Object getItem(int position) {
        return clubs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.club_list_item, parent, false);
            holder = new ViewHolder();
            holder.clubLogo = convertView.findViewById(R.id.clubLogo);
            holder.clubName = convertView.findViewById(R.id.clubName);
            holder.divisionName = convertView.findViewById(R.id.divisionname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Club club = clubs.get(position);

        // Set data to views
        holder.clubName.setText(club.getClubName());
        holder.divisionName.setText(club.getDivName());
        Glide.with(context).load(club.getClubImage()).into(holder.clubLogo);

        // Set the click listener to pass clubId to the next activity
        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ClubDetails.class);
            intent.putExtra("clubId", club.getId());  // Pass the club's ID
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView clubLogo;
        TextView clubName;
        TextView divisionName;
    }
}
