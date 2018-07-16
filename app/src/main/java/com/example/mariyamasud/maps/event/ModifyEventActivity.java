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

import com.example.mariyamasud.maps.MapsActivity;
import com.example.mariyamasud.maps.R;
import com.example.mariyamasud.maps.StartActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import transferObject.TransferObject;


public class ModifyEventActivity extends AppCompatActivity implements OnClickListener {


    private Button updateBtn, deleteBtn ,btnDatePicker, btnTimePicker, mEventButton , viewADRESS ;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Context context = this;
    String idi;

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
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl

        setTitle("Modify Record");
        setContentView(R.layout.activity_modify_event);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        addressEditText = (EditText) findViewById(R.id.adress_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);


        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        mEventButton = (Button) findViewById(R.id.done_button);
        viewADRESS = (Button) findViewById(R.id.view_address);

        Intent intent = getIntent();
        idi = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        nameEditText.setText(name);
        addressEditText.setText(address);
        dateEditText.setText(date);
        timeEditText.setText(time);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        mEventButton.setOnClickListener(this);
        viewADRESS.setOnClickListener(this);
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
                               final String name = nameEditText.getText().toString();
                               final String date = dateEditText.getText().toString();
                               final String time = timeEditText.getText().toString();
                                final String address = addressEditText.getText().toString();

                                if (name.matches("")  || date.matches("")
                                        || time.matches("")|| address.matches("") ) {
                                    Toast nullValueError = Toast.makeText(ModifyEventActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT);
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

                                        boolean isInserted = updateEvent(idi,name,address, date, time);
                                        if (isInserted == true){
                                            Intent home_intent = new Intent(getApplicationContext(), EventListActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                             startActivity(home_intent);
                                            Toast.makeText(ModifyEventActivity.this, "DATA UPDATED", Toast.LENGTH_LONG).show();
                                        }
                                    }catch(NullPointerException e) {
                                        Toast.makeText(ModifyEventActivity.this, "DATA NOT UPDATED", Toast.LENGTH_LONG).show();
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

            case R.id.btn_delete:
                AlertDialog.Builder alertDialogBuilde = new AlertDialog.Builder(
                        context);
                alertDialogBuilde.setTitle("ARE YOU SURE");
                alertDialogBuilde
                        .setMessage("CLICK YES TO DELETE!")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                try {
                                    boolean deletedRows = deleteEvent(idi);
                                    if (deletedRows == true) {
                                        Intent home_intent = new Intent(getApplicationContext(), EventListActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(home_intent);
                                        Toast.makeText(ModifyEventActivity.this, "DATA DELETED", Toast.LENGTH_LONG).show();
                                    }
                                }catch(IllegalArgumentException e){
                                    Toast.makeText(ModifyEventActivity.this, "DATA NOT DELETED", Toast.LENGTH_LONG).show();
                            }
                            }
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
                try {
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
                    Toast.makeText(ModifyEventActivity.this, "DATE NOT SELECTED", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ModifyEventActivity.this, "TIME NOT SELECTED", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.done_button:
                final CharSequence[] items = {
                        "THEATHER", "PARTY", "CINEMA", "MUSEUM", "SPORT", "COOKING", "EXCURSION",
                        "CIRCUS"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose your Event ");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        nameEditText.setText(items[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.view_address:
                Intent i = new Intent(ModifyEventActivity.this,MapsActivity.class);
                i.putExtra("EdiTtEXTvALUE",addressEditText.getText().toString());
                startActivity(i);
                break;
            default:
                break;
        }
    }
    private boolean deleteEvent(String id) {
        /*getting the specified event reference

         */
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);
        /*removing event

         */
        dR.removeValue();
        return true;
    }
    private boolean updateEvent(String id, String name, String address,String time,String date) {
        /*getting the specified event reference

         */
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);
        /*updating event

         */
        Event event = new Event(id, name, address,time,date);
        dR.setValue(event);
        return true;
    }
}
