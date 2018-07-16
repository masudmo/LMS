package com.example.mariyamasud.maps.register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.StartActivity;
import com.example.mariyamasud.maps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import transferObject.TransferObject;

/**
 * Created by mariyamasud on 21.02.18.
 */

public  class RegisterModifyActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Exception
    public TransferObject transferObject = new TransferObject();



    private Button updateBtn, deleteBtn;

    private EditText nameEditText;
    private EditText dateEditText;
    private EditText languageEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText grupeEditText;
    private EditText occupationEditText;
    private EditText passwordEditText;
    String idi;
    String emaila;
    Boolean choise =false;
    String emails;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    /*FirebaseAuth firebaseAuth;

     */
    final Context context = this;


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
        setContentView(R.layout.activity_modify_register );

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);
        languageEditText = (EditText) findViewById(R.id.language_edittext);
        addressEditText = (EditText) findViewById(R.id.adress_edittext);
        phoneEditText = (EditText) findViewById(R.id.phone_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        grupeEditText = (EditText) findViewById(R.id.group_edittext);
        occupationEditText = (EditText) findViewById(R.id.occupation_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        emails = user.getEmail();

        Intent intent = getIntent();
        idi = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String language = intent.getStringExtra("language");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        emaila = intent.getStringExtra("email");
        String email = intent.getStringExtra("email");
        String grupe = intent.getStringExtra("grupe");
        String occupation = intent.getStringExtra("occupation");
        String password = intent.getStringExtra("password");
        updateBtn.setVisibility( View.INVISIBLE);
        updateBtn.setEnabled(false);
        deleteBtn.setVisibility( View.INVISIBLE);
        deleteBtn.setEnabled(false);

        if(emails.equals(emaila)) {
            updateBtn.setVisibility(View.VISIBLE);
            updateBtn.setEnabled(true);
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setEnabled(true);
        }

        nameEditText.setText(name);
        dateEditText.setText(date);
        languageEditText.setText(language);
        addressEditText.setText(address);
        phoneEditText.setText(phone);
        emailEditText.setText(email);
        grupeEditText.setText(grupe);
        occupationEditText.setText(occupation);
        passwordEditText.setText(password);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

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
                                final String language = languageEditText.getText().toString();
                                final String address = addressEditText.getText().toString();
                                final String phone = phoneEditText.getText().toString();
                                final String email = emailEditText.getText().toString();
                                final String group = grupeEditText.getText().toString();
                                final String occupation = occupationEditText.getText().toString();
                                final String password = passwordEditText.getText().toString();
                                if (name.matches("") || date.matches("") || language.matches("") || address.matches("")
                                        || phone.matches("") || email.matches("") || group.matches("") || occupation.matches("") || password.matches("")
                                        || (!isValidEmail(email)) ) {
                                    Toast nullValueError = Toast.makeText(RegisterModifyActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT);
                                    nullValueError.show();

                                    if (name.matches("")) {
                                        nameEditText.setError("NAME REQUIRED");
                                    }

                                    if (date.matches("")) {
                                        dateEditText.setError("DATE REQUIRED");
                                    }
                                    if (language.matches("")) {
                                        languageEditText.setError("LANGUAGE REQUIRED");
                                    }
                                    if (address.matches("")) {
                                        addressEditText.setError("ADDRESS REQUIRED");
                                    }
                                    if (phone.matches("")) {
                                        phoneEditText.setError("PHONE REQUIRED");
                                    }
                                    if (!isValidEmail(email)) {
                                        emailEditText.setError("EMAIL REQUIRED");
                                    }
                                    if (group.matches("")) {
                                        grupeEditText.setError("GROUPNUMBER REQUIRED");
                                    }
                                    if (occupation.matches("")) {
                                        occupationEditText.setError("OCCUPATION REQUIRED");
                                    }
                                    if (password.matches("")) {
                                        passwordEditText.setError("PASSWORD REQUIRED");
                                    }
                                }
                                else{
                                    try{
                                        boolean isInserted = updateRegister(idi,name, date, language, address, phone, email, group,occupation,password);

                                        if (isInserted == true){
                                            Intent home_intent = new Intent(getApplicationContext(), StartActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(home_intent);
                                            Toast.makeText(RegisterModifyActivity.this, "DATA UPDATED", Toast.LENGTH_LONG).show();
                                        }}
                                    catch(NullPointerException e) {
                                        Toast.makeText(RegisterModifyActivity.this, "DATA NOT UPDATED", Toast.LENGTH_LONG).show();


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
                                try{
                                    boolean deletedRows = deleteRegister(idi);
                                    if (deletedRows == true) {
                                        Intent home_intent = new Intent(getApplicationContext(), StartActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(home_intent);
                                        Toast.makeText(RegisterModifyActivity.this, "DATA DELETED", Toast.LENGTH_LONG).show();
                                    }}catch (IllegalArgumentException e){
                                    Toast.makeText(RegisterModifyActivity.this, "DATA NOT DELETED", Toast.LENGTH_LONG).show();
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

            default:
                break;
        }
    }
    private boolean isValidEmail(String strEmail){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }
    private boolean deleteRegister(String id) {

        try{
            if (user != null && emails.equals(emaila)) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterModifyActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterModifyActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("register").child(id);
                dR.removeValue();
                choise = true;
            }else{
                choise = false;
                Toast.makeText(RegisterModifyActivity.this, "You can't to delete another account!", Toast.LENGTH_SHORT).show();
            } }
        catch (NullPointerException e) {}
        return choise;
    }
    private boolean updateRegister(String id,String name, String dateOfBirth, String language, String address, String phone, String email, String group,String occupation,String password) {

        try{
            if (user != null && emails.equals(emaila)) {
                user.updateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });


                user.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("register").child(id);
                Register register = new Register(id, name, dateOfBirth, language, address, phone, email, group, occupation, password);
                dR.setValue(register);
                choise = true;
                Toast.makeText(RegisterModifyActivity.this, "Updating is successful.", Toast.LENGTH_LONG).show();
            }else{
                choise = false;

            }
        } catch (NullPointerException e) {}
        return choise;

    }

}