package com.example.mariyamasud.maps.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.caughtglobalexceptionlibrary.CosmosException;

import com.example.mariyamasud.maps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import transferObject.TransferObject;

public class MeetingListActivity extends AppCompatActivity {

    //Global Exception
    public TransferObject transferObject = new TransferObject();
    /**
     * ListView is a view group that displays a list of scrollable item
     */
    private ListView listView;
    ArrayList<String> meetingImport;

    /*
    our database reference object
     */
    DatabaseReference databaseMeetings;
    List<Meeting> meetings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Global Exception
        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(MeetingListActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl

        setContentView(R.layout.fragment_list_meeting );
        databaseMeetings = FirebaseDatabase.getInstance().getReference("meetings");
        meetings = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list_view);
        /**
         * Sets the view to show if the adapter is empty
         */
        //listView.setEmptyView(findViewById(R.id.empty));
         meetingImport = (ArrayList<String>) getIntent().getSerializableExtra("meeting");

        /**
         *  OnCLickListiner For List Items
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /**
                 * to see in another activity
                 */
                Meeting meeting = meetings.get(i);
                Intent modify_intent = new Intent(getApplicationContext(), ModifyMeetingActivity.class);
                modify_intent.putExtra("name", meeting.getMeetingName());
                modify_intent.putExtra("room", meeting.getMeetingRoom());
                modify_intent.putExtra("notes", meeting.getMeetingNotes());
                modify_intent.putExtra("date", meeting.getMeetingDate());
                modify_intent.putExtra("time", meeting.getMeetingTime());
                modify_intent.putExtra("id", meeting.getMeetingId());
                modify_intent.putExtra("meeting",meetingImport);

                startActivity(modify_intent);
            }
        });
    }
    /*to fetch all dates von database und show it in listview

     */
    @Override
    protected void onStart() {
        super.onStart();
        /*attaching value event listener

         */
        databaseMeetings.addValueEventListener(new ValueEventListener() {
            @Override
            /*wenn etwas change to read all dates from database

             */
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*clearing the previous artist list

                 */
                meetings.clear();
                /*iterating through all the nodes

                 */
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    /*getting meeting

                     */
                    Meeting meeting = postSnapshot.getValue(Meeting.class);
                    /*adding meeting to the list

                     */
                    meetings.add(meeting);
                }
                /*creating adapter

                 */
                MeetingList meetingAdapter = new MeetingList(MeetingListActivity.this,meetings);
                /*attaching adapter to the listview

                 */
                listView.setAdapter(meetingAdapter);
            }

            @Override
            /*wenn some error

             */
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * To specify the options menu for an activity,
     * override onCreateOptionsMenu() (fragments provide their own onCreateOptionsMenu() callback).
     * In this method, inflate  menu resource (defined in XML) into the Menu provided in the callback.
     * options menu will be defined as an XML file
     * Inflate Menu Resource
     * getMenuInflater:Returns a MenuInflater with this context.
     *  inflate(int resource, ViewGroup root)
     *Inflate a new view hierarchy from the specified xml resource.
     * The menu contains an item to add a new record from the ActionBar.
     * @param menu
     * @return true,to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        /**
         * to check if choose
         */
        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddMeetingActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

}