package com.example.mariyamasud.maps.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;

import com.example.mariyamasud.maps.HomeActivity;
import com.example.mariyamasud.maps.StartActivity;
import com.example.mariyamasud.maps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import transferObject.TransferObject;

/*Global Exception

 */

public class SignUpActivity extends AppCompatActivity implements OnClickListener {

    /*Global Exception

     */
    public TransferObject transferObject = new TransferObject();

    private Button addTodoBtn;
    private EditText nameEditText;
    private EditText dateEditText;
    private EditText languageEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText grupeEditText;
    private EditText occupationEditText;
    private EditText passwordEditText;
    final Context context = this;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseRegister;
    private DatabaseReference mDatabase;

    /**
     *  This implementation should initialize the essential components of the activity
     *  this is where that must call setContentView() to define the layout for the activity's user interface
     *
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Global Exception
        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(StartActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl

        setContentView(R.layout.activity_sign_up );

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);
        languageEditText = (EditText) findViewById(R.id.language_edittext);
        addressEditText = (EditText) findViewById(R.id.adress_edittext);
        phoneEditText = (EditText) findViewById(R.id.phone_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        grupeEditText = (EditText) findViewById(R.id.group_edittext);
        occupationEditText = (EditText) findViewById(R.id.occupation_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);


        addTodoBtn = (Button) findViewById(R.id.add_record);
        addTodoBtn.setOnClickListener(this);
        /*Initialization of the FirebaseAuth Object

         */
        firebaseAuth = FirebaseAuth.getInstance();

        /*Checking whether a user as already Logged In

         */
       if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        databaseRegister = FirebaseDatabase.getInstance().getReference("register");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("ARE YOU SURE?");

            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog the dialog that received the click
             * @param which the button that was clicked (ex.
             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
             *              of the item clicked
             */
            // set dialog message
            alertDialogBuilder
                    .setMessage("CLICK YES TO ADD!")
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
                                    || phone.matches("") || email.matches("") || group.matches("") || occupation.matches("")
                                    || password.matches("")
                                    || (!isValidEmail(email)) ) {
                                Toast nullValueError = Toast.makeText(SignUpActivity.this, "Kindly fill all the field", Toast.LENGTH_SHORT);
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
                            }else {
                                try{
                               registerNewUser(name, date, language, address, phone, email, group, occupation, password);
                                } catch (NullPointerException e) {

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
            default:
                break;

        }

    }
    private void addRegister( String name, String dateOfBirth, String language, String address, String phone, String email, String group,String occupation,String password) {
        /*getting a unique id using push().getKey() method
         it will create a unique id and we will use it as the Primary Key for our User

         */

        String id = databaseRegister.push().getKey();
        /*creating an User Object

         */
        Register register = new Register(id, name, dateOfBirth,language,address,phone,email,group,occupation,password);
        /*Saving the User

         */
        databaseRegister.child(id).setValue(register);
    }
    private void registerNewUser(final String name, final String dateOfBirth, final String language, final String address, final String phone, final String email, final String group, final String occupation, final String password) {

        progressDialog.setMessage("In Progress...");
        progressDialog.show();

        /**
         *
         * @param email user email
         * @param password user password
         */
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                /*checks whether the user has been successfully registered

                 */
                if(task.isSuccessful()){
                    addRegister(name,dateOfBirth,language,address,phone,email,group,occupation,password);
                    Toast.makeText(SignUpActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    finish();
                    redirectToMain();

                }else{
                    Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * method is used for checking valid email id format
     * @param strEmail
     * @return boolean true for valid false for invalid
     */
    private boolean isValidEmail(String strEmail){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }
    private void redirectToMain() {
        finish();
        startActivity(new Intent(this, StartActivity.class));
    }

}