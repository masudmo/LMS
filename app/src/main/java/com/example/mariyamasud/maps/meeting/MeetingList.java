package com.example.mariyamasud.maps.meeting;

/**
 * Created by mariyamasud on 16.02.18.
 */
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariyamasud.maps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MeetingList extends ArrayAdapter<Meeting> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Activity context;
    /*
    to store all the meetings
     */
    List<Meeting> meetings;
    private Intent intent;

    /*
    constructor
     */
    public MeetingList(Activity context, List<Meeting> meetings) {
        super(context, R.layout.activity_list_meeting, meetings);
        this.context = context;
        this.meetings = meetings;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.activity_list_meeting, null, true );

        TextView titleTextView = (TextView) listViewItem.findViewById( R.id.name );
        TextView roomTextView = (TextView) listViewItem.findViewById( R.id.room );
        TextView notesTextView = (TextView) listViewItem.findViewById( R.id.notes );
        TextView dateTextView = (TextView) listViewItem.findViewById( R.id.date );
        TextView timeTextView = (TextView) listViewItem.findViewById( R.id.time );


        Meeting meeting = meetings.get( position );
        titleTextView.setText( meeting.getMeetingName() );
        roomTextView.setText( meeting.getMeetingRoom() );
        notesTextView.setText( meeting.getMeetingNotes() );
        dateTextView.setText( meeting.getMeetingDate() );
        timeTextView.setText( meeting.getMeetingTime() );

        try {
            if (titleTextView.getText().toString().contains(user.getEmail())) {

                titleTextView.setTextColor( 0xffff0000 );

            }
        }catch (NullPointerException e){};

        return listViewItem;

    }
}
