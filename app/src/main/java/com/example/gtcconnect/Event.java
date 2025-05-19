package com.example.gtcconnect;

import java.util.Date;

public class Event {
    private String id; // Unique event ID
    private String title;
    private String eventIconUrl;
    private String venue;
    private String description;
    private String type;
    private Date date;

    public Event(String id, String title, String eventIconUrl, String venue, String description, String type, Date date) {
        this.id = id;
        this.title = title;
        this.eventIconUrl = eventIconUrl;
        this.venue = venue;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEventIconUrl() {
        return eventIconUrl;
    }

    public String getVenue() {
        return venue;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }
}
