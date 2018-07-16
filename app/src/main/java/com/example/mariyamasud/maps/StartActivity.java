package com.example.mariyamasud.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.register.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//Global Exception
import transferObject.TransferObject;

public class StartActivity extends AppCompatActivity implements OnClickListener {

    EditText email, pass;
    Button login, reg;
    Cursor cursor;
    final Context context = this;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    //Global Exception
    public TransferObject transferObject = new TransferObject();

    /**
     * This implementation should initialize the essential components of the activity
     * this is where that must call setContentView() to define the layout for the activity's user interface
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

		/*Global Exception

		 */
        transferObject.setCrashText( "D'oh! Its Crash.." ); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity( StartActivity.class ); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText( "Details" ); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText( "Contiune" ); //restart your app. change your button's text what you want
        transferObject.setImagePath( R.drawable.homer );
        transferObject.setBackgorundHex( "#ffffff" ); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor( "#000000" ); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler( new CosmosException( this, transferObject ) ); //this our girl
        setContentView( R.layout.activity_start );
        /*Initialization of the FirebaseAuth Object

		 */
        firebaseAuth = FirebaseAuth.getInstance();
		/*Checking whether a user as already Logged In

		 */
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity( new Intent( getApplicationContext(), HomeActivity.class ) );
        }
		/*Initialization of the ProgressDialog object

		 */
        progressDialog = new ProgressDialog( this );
        email = (EditText) findViewById( R.id.edemail );
        pass = (EditText) findViewById( R.id.edpass );
        login = (Button) findViewById( R.id.login );
        reg = (Button) findViewById( R.id.reg );
        login.setOnClickListener( this );
        reg.setOnClickListener( this );
    }

    /**
     * Called when a view has been clicked
     * Interface definition for a callback to be invoked when a view is clicked
     * This Method has function to select the Login Button on the Display Main activity
     */
    private void loginUser() {

        String emails = email.getText().toString();
        String password = pass.getText().toString();

			/*Checking whether the email field is empty and displaying a error message through Toast

			 */
        if (TextUtils.isEmpty( emails )) {
            Toast.makeText( this, "Please enter a valid email address", Toast.LENGTH_SHORT ).show();
            return;
        }

			/*Checking whether the password field is empty and displaying a error message through Toast

			 */
        if (TextUtils.isEmpty( password )) {
            Toast.makeText( this, "Please enter a preferred password", Toast.LENGTH_SHORT ).show();
            return;
        }

			/*Giving the ProgressDialog a message to display while the action in is progress

			 */
        progressDialog.setMessage( "In Progress..." );
			/*Displaying the ProgressDialog

			 */
        progressDialog.show();

        /**
         *
         * @param email user email
         * @param password user password
         */
        firebaseAuth.signInWithEmailAndPassword( emails, password ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
					/*checks whether the user has been successfully logged in

					 */
                if (task.isSuccessful()) {
						/*Displays a login successful message through Toast

						 */
                    Toast.makeText( StartActivity.this, "User Logged In", Toast.LENGTH_SHORT ).show();
                    finish();
						/*Redirects to the User Profile Activity

						 */
                    startActivity( new Intent( getApplicationContext(), HomeActivity.class ) );
                } else {
						/*Displays a login Unsuccessful message through Toast

						 */
                    Toast.makeText( StartActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT ).show();
                }
            }
        } );


    }

    private void redirectToRegister() {
        finish();
        startActivity( new Intent( this, SignUpActivity.class ) );
    }

    @Override
    public void onClick(View view) {

		/*when loginButton is clicked loginUser method is invoked

		 */
        if (view == login) {
            loginUser();
        }

		/*when registerRedirect is clicked redirectToRegister method is invoked

		 */
        if (view == reg) {
            redirectToRegister();
        }

    }


}