package com.example.mariyamasud.maps.event;

/**
 * Created by mariyamasud on 10.02.18.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event {
    private String eventId;
    private String eventName;
    private String eventAddress;
    private String eventDate;
    private String eventTime;

    public Event() {
        //this constructor is required
    }

    public Event(String eventId, String eventName, String eventAddress, String eventTime, String eventDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }
}