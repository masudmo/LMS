package com.example.mariyamasud.maps.meeting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import transferObject.TransferObject;


public class ModifyMeetingActivity extends AppCompatActivity implements OnClickListener {


    //private Toolbar mToolbar;
    private Button updateBtn, deleteBtn ,btnDatePicker, btnTimePicker, meetingAcceptBtn, meetingDeclineBtn;

    private EditText nameEditText;
    private EditText roomEditText;
    private EditText notesEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Context context = this;
    String idi;
    String email;
    String room ;
    String notes ;
    String date ;
    String time ;
    /*Global Exception

     */
    public TransferObject transferObject = new TransferObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mToolbar = (Toolbar) findViewById( R.id.meeting_page_toolbar );
        //setSupportActionBar( mToolbar );
        //getSupportActionBar().setTitle( "Modify Meeting" );
        //getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        /*Global Exception

         */
        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(MeetingListActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl


        setContentView(R.layout.activity_modify_meeting);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        roomEditText = (EditText) findViewById(R.id.room_edittext);
        notesEditText = (EditText) findViewById(R.id.notes_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        meetingAcceptBtn = (Button) findViewById(R.id.meeting_accept_btn);
        meetingDeclineBtn = (Button) findViewById(R.id.meeting_decline_btn);

        /**
         *  to receive a extra  data
         *  extra data is represented as strings
         *
         */

        Intent intent = getIntent();
        idi = intent.getStringExtra("id");
        email = intent.getStringExtra("name");
        room = intent.getStringExtra("room");
        notes = intent.getStringExtra("notes");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        meetingAcceptBtn.setVisibility( View.INVISIBLE);
        meetingAcceptBtn.setEnabled(false);
        meetingDeclineBtn.setVisibility( View.INVISIBLE);
        meetingDeclineBtn.setEnabled(false);

        updateBtn.setVisibility( View.INVISIBLE);
        updateBtn.setEnabled(false);
        deleteBtn.setVisibility( View.INVISIBLE);
        deleteBtn.setEnabled(false);
        try {
            if (email.contains(user.getEmail())) {

                meetingAcceptBtn.setVisibility(View.VISIBLE);
                meetingAcceptBtn.setEnabled(true);
                meetingDeclineBtn.setVisibility(View.VISIBLE);
                meetingDeclineBtn.setEnabled(true);


                updateBtn.setVisibility( View.VISIBLE);
                updateBtn.setEnabled(true);
                deleteBtn.setVisibility( View.VISIBLE);
                deleteBtn.setEnabled(true);

            }
        }catch (NullPointerException e){};

        /**
         * Sets the text to be displayed.
         */
        nameEditText.setText(email);
        roomEditText.setText(room);
        notesEditText.setText(notes);
        dateEditText.setText(date);
        timeEditText.setText(time);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        meetingAcceptBtn.setOnClickListener(this);
        meetingDeclineBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("ARE YOU SURE?");
                alertDialogBuilder
                        .setMessage("CLICK YES TO UPDATE!")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                email = nameEditText.getText().toString();
                                room = roomEditText.getText().toString();
                                notes = notesEditText.getText().toString();
                                date = dateEditText.getText().toString();
                                time = timeEditText.getText().toString();
                                if (email.matches("") || room.matches("")  || date.matches("")
                                        || time.matches("")) {
                                    Toast nullValueError = Toast.makeText(ModifyMeetingActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT);
                                    nullValueError.show();

                                    if (email.matches("")) {
                                        nameEditText.setError("NAME REQUIRED");
                                    }

                                    if (room.matches("")) {
                                        roomEditText.setError("ROOM REQUIRED");
                                    }
                                    if (time.matches("")) {
                                        timeEditText.setError("TIME REQUIRED");
                                    }
                                    if (date.matches("")) {
                                        dateEditText.setError("DATE REQUIRED");
                                    }

                                }else {
                                 try{

                               boolean isInserted = updateMeeting(idi,email, room, notes, date, time);
                                if (isInserted == true){
                                    Intent home_intent = new Intent(getApplicationContext(), MeetingListActivity.class);
                                    startActivity(home_intent);
                                    Toast.makeText(ModifyMeetingActivity.this, "DATA UPDATED", Toast.LENGTH_LONG).show();
                                }}
                               catch (NullPointerException e) {
                                   Toast.makeText(ModifyMeetingActivity.this, "DATA NOT UPDATED", Toast.LENGTH_LONG).show();
                               }}
                            }


                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.btn_delete:
                AlertDialog.Builder alertDialogBuilde = new AlertDialog.Builder(
                        context);
                alertDialogBuilde.setTitle("ARE YOU SURE");
                alertDialogBuilde
                        .setMessage("CLICK YES TO DELETE!")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                try{
                                boolean deletedRows = deleteMeeting(idi);
                                if (deletedRows == true) {
                                    Intent home_intent = new Intent(getApplicationContext(), MeetingListActivity.class);
                                    startActivity(home_intent);
                                    Toast.makeText(ModifyMeetingActivity.this, "DATA DELETED", Toast.LENGTH_LONG).show();
                                }} catch (IllegalArgumentException e){
                                    Toast.makeText(ModifyMeetingActivity.this, "DATA NOT DELETED", Toast.LENGTH_LONG).show();

                            }}
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog1 = alertDialogBuilde.create();
                alertDialog1.show();
                break;
            case R.id.btn_date:
                try{
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                }catch (ParseException e){
                    Toast.makeText(ModifyMeetingActivity.this, "DATE NOT SELECTED", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_time:
                try {
                    final Calendar ca = Calendar.getInstance();
                    mHour = ca.get(Calendar.HOUR_OF_DAY);
                    mMinute = ca.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    timeEditText.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }catch (ParseException e){
                    Toast.makeText(ModifyMeetingActivity.this, "TIME NOT SELECTED", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.meeting_accept_btn:
                Intent intent_meetingList = new Intent(ModifyMeetingActivity.this, MeetingListActivity.class);
                update();
                startActivity(intent_meetingList);

                break;
            case R.id.meeting_decline_btn:

                String new_string;
                new_string = email.replace(user.getEmail(), "");
                updateMeeting(idi,new_string, room, notes, date, time);

                if(! new_string.matches(".*[a-zA-Z]+.*")){

                    deleteMeeting(idi);

                }

                Intent intent_meetingList_decline = new Intent(ModifyMeetingActivity.this, MeetingListActivity.class);
                startActivity(intent_meetingList_decline);
                break;
            default:
                break;
        }
    }
    private boolean deleteMeeting(String id) {
        /*getting the specified meeting reference

         */
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("meetings").child(id);
        /*removing meeting

         */
        dR.removeValue();
        return true;
    }
    private boolean updateMeeting(String id,String name, String room, String notes,String time,String date) {
        Meeting meet = new Meeting();
        String idUser =  meet.getUserId();
        /*getting the specified meeting reference

         */
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("meetings").child(id);
        /*updating meeting

         */
        Meeting meeting = new Meeting(id, name,room, notes,time,date,idUser);
        dR.setValue(meeting);
        return true;
    }

    public void update(){

        setContentView(R.layout.activity_list_meeting);
        TextView textView_name = (TextView)findViewById(R.id.name);
        textView_name.setText( nameEditText.getText().toString() );

        try {
            if (textView_name.getText().toString().contains(user.getEmail())) {

                textView_name.setTextColor( 0xff000000 );

            }
        }catch (NullPointerException e){};

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );

        getMenuInflater().inflate( R.menu.meeting_menu, menu );

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        if (item.getItemId() == R.id.meeting_menu_accept_btn) {

            Intent intent_meetingList = new Intent(ModifyMeetingActivity.this, MeetingListActivity.class);
            startActivity(intent_meetingList);

        }

        if (item.getItemId() == R.id.meeting_menu_decline_btn) {

            String new_string;
            new_string = email.replace(user.getEmail(), "");
            updateMeeting(idi,new_string, room, notes, date, time);
            if(! new_string.matches(".*[a-zA-Z]+.*")){

                deleteMeeting(idi);
            }
            Intent intent_meetingList_decline = new Intent(ModifyMeetingActivity.this, MeetingListActivity.class);
            startActivity(intent_meetingList_decline);

        }
        return true;
    }*/

}
