package com.example.mariyamasud.maps.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.StartActivity;
import com.example.mariyamasud.maps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import transferObject.TransferObject;

/*Global Exception

 */
public class RegisterListActivity extends AppCompatActivity {
    private ListView listView;

    /*our database reference object

     */
    DatabaseReference databaseRegister;
    List<Register> registers;

    /*Global Exception

     */
    public TransferObject transferObject = new TransferObject();


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

        setContentView(R.layout.fragment_emp_listregister);

        databaseRegister = FirebaseDatabase.getInstance().getReference("register");
        registers = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list_view);
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent The AdapterView where the click happened.
         * @param view The view within the AdapterView that was clicked (this
         *            will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Register register= registers.get(i);
                Intent  modify_intent = new Intent(getApplicationContext(),RegisterModifyActivity.class);

                modify_intent.putExtra("name", register.getName());
                modify_intent.putExtra("date", register.getDateOfBirth());
                modify_intent.putExtra("language",register.getLanguage());
                modify_intent.putExtra("address", register.getAddress());
                modify_intent.putExtra("phone", register.getPhone());
                modify_intent.putExtra("email", register.getEmail());
                modify_intent.putExtra("grupe", register.getGroup());
                modify_intent.putExtra("occupation", register.getOccupation());
                modify_intent.putExtra("password", register.getPassword());
                modify_intent.putExtra("id", register.getId());

                startActivity(modify_intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*attaching value event listener

         */
        databaseRegister.addValueEventListener(new ValueEventListener() {
            @Override
            /*wenn etwas change to read all dates from database

             */
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*clearing the previous user list

                 */
                registers.clear();
                /*iterating through all the nodes

                 */
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    /*getting user

                     */
                   Register register = postSnapshot.getValue(Register.class);
                    /*adding user to the list

                     */
                    registers.add(register);
                }
                /*creating adapter

                 */
                RegisterList registerAdapter = new RegisterList(RegisterListActivity.this,registers);
                /*attaching adapter to the listview

                 */
                listView.setAdapter(registerAdapter);
            }
            @Override
            /*wenn some error

             */
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}