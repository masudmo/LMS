package com.example.mariyamasud.maps.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.example.mariyamasud.maps.R;
import com.example.mariyamasud.maps.StartActivity;
import com.example.mariyamasud.maps.register.Register;
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
public class MembersActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> model;
    DatabaseReference databaseRegister;
    List<Register> registers;
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
        setContentView(R.layout.fragment_emp_list_name);
        databaseRegister = FirebaseDatabase.getInstance().getReference("register");
        registers = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_view);
        model = new ArrayList<>();
        /*for (int i = 0; i < 10; i++) {
            model.add("");
        }*/
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                try{
                    Register register = registers.get(position);
                    String message = "Item " + position;
                    String email = register.getEmail();
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
                    cb.setChecked(!cb.isChecked());
                    if (!(model.contains(email)) && cb.isChecked()) {
                        cb.setVisibility(View.VISIBLE);
                       // model.set(position,email + " ");
                        model.add(email);
                    } else {
                       // model.remove(position);
                        model.remove(email);
                    }
                }catch (NullPointerException e){}
            }

        } );

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
                /*clearing the previous users list

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
                MembersList nameAdapter = new MembersList(MembersActivity.this,registers);
                /*attaching adapter to the listview

                 */
                listView.setAdapter(nameAdapter);
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
        getMenuInflater().inflate( R.menu.mainmenubar, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_save) {
            Intent add_mem = new Intent( this, AddMeetingActivity.class );
            add_mem.putExtra( "emailEditText", model );
            startActivity( add_mem );
            model = null;
        }
        return super.onOptionsItemSelected( item );
    }

}