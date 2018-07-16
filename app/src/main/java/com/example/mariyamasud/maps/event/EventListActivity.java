package com.example.mariyamasud.maps.event;

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
import com.example.mariyamasud.maps.StartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/*Global Exception

 */
import transferObject.TransferObject;
public class EventListActivity extends AppCompatActivity {

    private ListView listView;
    DatabaseReference databaseEvents;
    List<Event> events;
    /*Global Exception

     */
    public TransferObject transferObject = new TransferObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Global Exception

         */
        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(StartActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this, transferObject)); //this our girl

        setContentView(R.layout.fragment_event_list );
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");
        events = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list_view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                Intent intent = new Intent(getApplicationContext(), ModifyEventActivity.class);
                intent.putExtra("id", event.getEventId());
                intent.putExtra("name", event.getEventName());
                intent.putExtra("address", event.getEventAddress());
                intent.putExtra("date", event.getEventDate());
                intent.putExtra("time", event.getEventTime());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*attaching value event listener

         */
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            /*wenn etwas change to read all dates from database

             */
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*clearing the previous event list

                 */
                events.clear();
                /*iterating through all the nodes

                 */
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    /*getting event

                     */
                    Event event = postSnapshot.getValue(Event.class);
                    /*adding event to the list

                     */
                    events.add(event);
                }
                /*creating adapter

                 */
                 EventList eventAdapter = new EventList(EventListActivity.this, events);
                /*attaching adapter to the listview

                 */
                listView.setAdapter(eventAdapter);
            }
            @Override
            /*wenn some error

             */
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddEventActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

}