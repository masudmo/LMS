package com.example.mariyamasud.maps.event;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.mariyamasud.maps.R;

import java.util.List;

/**
 * Created by mariyamasud on 16.02.18.
 */

public class EventList extends ArrayAdapter<Event> {

    private Activity context;
    /*to store all events

     */
    List<Event> events;
    /*constructor

     */
    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.activity_list_event, events);
        this.context = context;
        this.events = events;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_list_event, null, true);

        TextView titleTextView = (TextView) listViewItem.findViewById(R.id.name);
        TextView addressTextView = (TextView) listViewItem.findViewById(R.id.adress);
        TextView dateTextView = (TextView) listViewItem.findViewById(R.id.date);
        TextView timeTextView = (TextView) listViewItem.findViewById(R.id.time);

        Event event = events.get(position);
        titleTextView .setText(event.getEventName());
        addressTextView.setText(event.getEventAddress());
        dateTextView.setText(event.getEventDate());
        timeTextView.setText(event.getEventTime());
        return listViewItem;
    }
}