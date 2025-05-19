package com.example.gtcconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private EventClickListener eventClickListener;

    // Constructor
    public EventAdapter(Context context, List<Event> eventList, EventClickListener eventClickListener) {
        this.context = context;
        this.eventList = eventList;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set event title
        holder.eventTitle.setText(event.getTitle() != null ? event.getTitle() : "No Title");

        // Set event date
        if (event.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            holder.eventDate.setText(sdf.format(event.getDate()));
        } else {
            holder.eventDate.setText("No Date Available");
        }

        // Load event poster using Glide
        Glide.with(context)
                .load(event.getEventIconUrl())
                .placeholder(R.drawable.gtc) // Fallback image if no URL is provided
                .error(R.drawable.gtc)
                .into(holder.eventImage);

        // Handle list item click
        holder.itemView.setOnClickListener(v -> {
            if (eventClickListener != null) {
                eventClickListener.onEventClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Update event list for filters
    public void updateEventList(List<Event> filteredEvents) {
        this.eventList = filteredEvents;
        notifyDataSetChanged();
    }

    // ViewHolder class
    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle, eventDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventLogo);
            eventTitle = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
        }
    }

    // Click listener interface
    public interface EventClickListener {
        void onEventClick(Event event);
    }
}
