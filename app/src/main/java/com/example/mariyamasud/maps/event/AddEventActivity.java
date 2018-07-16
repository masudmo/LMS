package com.example.mariyamasud.maps.event;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.R;
import com.example.mariyamasud.maps.StartActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/*Global Exception

 */
import transferObject.TransferObject;

public class AddEventActivity extends AppCompatActivity implements OnClickListener {
    private Button addTodoBtn,btnDatePicker, btnTimePicker, mEventButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Context context = this;
    DatabaseReference databaseEvents;
    public TransferObject transferObject = new TransferObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        /*Global Exception

         */
        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(StartActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl

        setTitle("Add Record");
        nameEditText = (EditText) findViewById(R.id.name_edittext);
        addressEditText = (EditText) findViewById(R.id.adress_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        addTodoBtn = (Button) findViewById(R.id.add_record);
        mEventButton = (Button) findViewById(R.id.done_button);
        addTodoBtn.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        mEventButton.setOnClickListener(this);
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("ARE YOU SURE?");
                alertDialogBuilder
                        .setMessage("CLICK YES TO ADD!")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                final String name = nameEditText.getText().toString();
                                final String date = dateEditText.getText().toString();
                                final String time = timeEditText.getText().toString();
                                final String address = addressEditText.getText().toString();
                                if (name.matches("") || date.matches("")
                                        || time.matches("")|| address.matches("") ) {
                                    Toast nullValueError = Toast.makeText(AddEventActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT);
                                    nullValueError.show();

                                    if (name.matches("")) {
                                        nameEditText.setError("NAME REQUIRED");
                                    }
                                    if (date.matches("")) {
                                        dateEditText.setError("DATE REQUIRED");
                                    }
                                    if (time.matches("")) {
                                        timeEditText.setError("TIME REQUIRED");
                                    }
                                    if (address.matches("")) {
                                        addressEditText.setError("ADDRESS REQUIRED");
                                    }

                                }else {
                                    try {
                                        boolean isInserted =  addEvent(name, address,time,date);
                                        if (isInserted == true) {
                                           Intent main = new Intent(AddEventActivity.this, EventListActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            startActivity(main);
                                            Toast.makeText(AddEventActivity.this, "DATA INSERTED", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (NullPointerException e) {
                                        Toast.makeText(AddEventActivity.this, "DATA NOT INSERTED", Toast.LENGTH_LONG).show();

                                    }
                                }

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
                    Toast.makeText(AddEventActivity.this, "DATE NOT SELECTED", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_time:
                try{
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
                    Toast.makeText(AddEventActivity.this, "TIME NOT SELECTED", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.done_button:
                try {
                final CharSequence[] items = {
                        "THEATER", "PARTY", "CINEMA", "MUSEUM", "SPORT", "COOKING", "EXCURSION",
                        "CIRCUS"
                };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    /**
                     * Set the title displayed in the Dialog.
                     */
                    builder.setTitle("Choose your Event ");
                    /**
                     * Set a list of items to be displayed in the dialog as the content,
                     * you will be notified of the selected item via the supplied listener.
                     */
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            nameEditText.setText(items[item]);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(AddEventActivity.this, "EVENT NOT SELECTED", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;

        }

    }

    private boolean addEvent(String name,String address,String time,String date) {
        /*getting a unique id using push().getKey() method
        it will create a unique id and we will use it as the Primary Key for our Event
            */
        String id = databaseEvents.push().getKey();
        /*creating an Event Object

         */
        Event event = new Event(id, name, address, time, date);
        /*Saving the Event

         */
        databaseEvents.child(id).setValue(event);
        return true;

    }
}