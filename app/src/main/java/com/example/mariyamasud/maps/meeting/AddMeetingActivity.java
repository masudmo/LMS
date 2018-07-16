package com.example.mariyamasud.maps.meeting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;



import transferObject.TransferObject;

/**
 * Interface OnClickListener inside the View class.
 * This interface can be used for handling the button.
 */

public class AddMeetingActivity extends AppCompatActivity implements OnClickListener {


    private Button addTodoBtn, btnDatePicker, btnTimePicker, btnperson;
    private EditText nameEditText;
    private EditText roomEditText;
    private EditText notesEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    final Context context = this;
    /*our database reference object

     */
    DatabaseReference databaseMeetings;
    ArrayList<String> emailsImport;

    /*Global Exception

     */
    public TransferObject transferObject = new TransferObject();




    /**
     * When an Activity first call or launched then onCreate(Bundle savedInstanceState)
     * method is responsible to create the activity.
     * onCreate(Bundle savedInstanceState): This is an overridden method.
     * It is inside the AppCompatActivity class.
     * This method will be executed when the app is opened (the activity is created).
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * the option of completely replacing the method in our class, or of extending the existing parent class' method.
         * By calling super.onCreate(savedInstanceState);, you tell the Dalvik VM
         * to run your code in addition to the existing code in the onCreate() of the parent class.
         *
         */super.onCreate( savedInstanceState );

        //Global Exception
        transferObject.setCrashText( "D'oh! Its Crash.." ); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(AddMeetingActivity.class ); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText( "Details" ); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText( "Contiune" ); //restart your app. change your button's text what you want
        transferObject.setImagePath( R.drawable.homer );
        transferObject.setBackgorundHex( "#ffffff" ); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor( "#000000" ); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler( new CosmosException( this, transferObject ) ); //this our girl

        /**
         * Set the activity content from a layout resource.
         * This method takes a layout and it set the view as the layout to the activity.
         */
        setContentView( R.layout.activity_add_meeting );

        /**
         *For initializing a View from XML layout file, we have a method findViewById().
         * findViewById(int id)
         * It takes a parameter id. Every components has an specific id that is stored in R.java file.
         *  Everytime  to cast to the respective view
         *
         */
        nameEditText = (EditText) findViewById( R.id.name_edittext );
        roomEditText = (EditText) findViewById( R.id.room_edittext );
        notesEditText = (EditText) findViewById( R.id.notes_edittext );
        dateEditText = (EditText) findViewById( R.id.date_edittext );
        timeEditText = (EditText) findViewById( R.id.time_edittext );
        btnDatePicker = (Button) findViewById( R.id.btn_date );
        btnTimePicker = (Button) findViewById( R.id.btn_time );
        addTodoBtn = (Button) findViewById( R.id.add_record );
        btnperson = (Button) findViewById( R.id.btn_person_picker );
        databaseMeetings = FirebaseDatabase.getInstance().getReference("meetings");

        /**
         * This method takes the reference to the Listener
         * and registers a callback to be invoked when the View is clicked.
         * specify the listener for our button
         */

        addTodoBtn.setOnClickListener( this );
        btnDatePicker.setOnClickListener( this );
        btnTimePicker.setOnClickListener( this );
        btnperson.setOnClickListener( this );


    }

    @Override
    /**
     * method onClick() will execute when the button is tapped
     * the method in this View's context to invoke when the view is clicked.
     */

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                try {
                    emailsImport = (ArrayList<String>) getIntent().getSerializableExtra("emailEditText");
                    String listString = "";
                    for (String s : emailsImport) {
                        if(!(s.equals(""))) {
                            listString += s + ",\t";
                        }
                    }
                    nameEditText.setText( listString );

                }catch (NullPointerException e ){};

                final String name = nameEditText.getText().toString();
                final String room  = roomEditText.getText().toString();
                final String notes = notesEditText.getText().toString();
                final String date = dateEditText.getText().toString();
                final String time = timeEditText.getText().toString();
                final String userId =  user.getUid();


                if (name.matches( "" ) || room.matches( "" ) || date.matches( "" )
                        || time.matches( "" )) {
                    Toast nullValueError = Toast.makeText( AddMeetingActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT );
                    nullValueError.show();

                    if (name.matches( "" )) {
                        nameEditText.setError( "NAME REQUIRED" );
                    }

                    if (room.matches( "" )) {
                        roomEditText.setError( "ROOM REQUIRED" );
                    }
                    if (time.matches( "" )) {
                        timeEditText.setError( "TIME REQUIRED" );
                    }
                    if (date.matches( "" )) {
                        dateEditText.setError( "DATE REQUIRED" );
                    }

                }

                else {
                    try {
                        boolean isInserted = addMeeting(name ,room, notes, date, time,userId );
                        if (isInserted == true) {
                            /**
                             * method define that the intent should be used to start an activity.
                             */

                            Intent main = new Intent( AddMeetingActivity.this, MeetingListActivity.class );
                            main.putExtra("meeting",emailsImport);
                            startActivity( main );
                            /**
                             * LENGTH_LONG:Show the view or text notification for a long period of time.
                             */
                            Toast.makeText( AddMeetingActivity.this, "DATA INSERTED", Toast.LENGTH_LONG ).show();
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText( AddMeetingActivity.this, "DATA NOT INSERTED", Toast.LENGTH_LONG ).show();
                    }
                }
                break;
            case R.id.btn_date:

                /**
                 * Use the current date as the default date in the picker
                 */
                try {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get( Calendar.YEAR );
                    mMonth = c.get( Calendar.MONTH );
                    mDay = c.get( Calendar.DAY_OF_MONTH );


                    DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    dateEditText.setText( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year );

                                }
                            }, mYear, mMonth, mDay );
                    datePickerDialog.show();
                } catch (ParseException e) {
                    Toast.makeText( AddMeetingActivity.this, "DATE NOT SELECTED", Toast.LENGTH_LONG ).show();
                }
                break;

            case R.id.btn_time:
                /**
                 * Calendar's getInstance method returns a Calendar object whose calendar fields
                 * have been initialized with the current date and time:
                 */
                try {
                    final Calendar ca = Calendar.getInstance();
                    /**
                     * Returns the value of the given calendar field.
                     */
                    mHour = ca.get( Calendar.HOUR_OF_DAY );
                    mMinute = ca.get( Calendar.MINUTE );

                    /**
                     * Creates a new time picker dialog.
                     * boolean: whether this is a 24 hour view or AM/PM
                     * public class TimePickerDialog
                     *extends AlertDialog implements DialogInterface.OnClickListener,
                     *TimePicker.OnTimeChangedListener
                     */
                    TimePickerDialog timePickerDialog = new TimePickerDialog( this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    timeEditText.setText( hourOfDay + ":" + minute );
                                }
                            }, mHour, mMinute, false );
                    /**
                     * Start the dialog and display it on screen.
                     */
                    timePickerDialog.show();
                } catch (ParseException e) {
                    Toast.makeText( AddMeetingActivity.this, "TIME NOT SELECTED", Toast.LENGTH_LONG ).show();
                }
                break;
            case R.id.btn_person_picker:
                Intent i4 = new Intent( this, MembersActivity.class );
                startActivity( i4 );
                break;
            default:
                break;

        }

    }
    private boolean addMeeting(String name,String room,String notes,String date,String time,String userId) {
        /*getting a unique id using push().getKey() method

         */
        //it will create a unique id and we will use it as the Primary Key for our Event
        String id = databaseMeetings.push().getKey();
        //creating an Event Object
        Meeting meeting = new Meeting(id,name,room,notes,date,time,userId);
        //Saving the Event
        databaseMeetings.child(id).setValue(meeting);
        return true;

    }
}