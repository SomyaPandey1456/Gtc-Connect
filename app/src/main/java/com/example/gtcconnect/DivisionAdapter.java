package com.example.gtcconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DivisionAdapter extends BaseAdapter {
    private Context context;
    private List<String> divisionList;

    public DivisionAdapter(Context context, List<String> divisionList) {
        this.context = context;
        this.divisionList = divisionList;
    }

    @Override
    public int getCount() {
        return divisionList.size();
    }

    @Override
    public Object getItem(int position) {
        return divisionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.division_list_item, parent, false);
        }

        // Get references to the views
        ImageView imageView = convertView.findViewById(R.id.imageView13);
        TextView divisionName = convertView.findViewById(R.id.divisionname);

        // Set data for the views
        imageView.setImageResource(R.drawable.division_list); // Placeholder image
        divisionName.setText(divisionList.get(position));

        return convertView;
    }
}
