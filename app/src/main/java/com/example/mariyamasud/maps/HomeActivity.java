package com.example.mariyamasud.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mariyamasud.maps.register.RegisterListActivity;
import com.example.mariyamasud.maps.ui.ChatListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.mariyamasud.maps.event.EventListActivity;
import com.example.mariyamasud.maps.meeting.MeetingListActivity;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById( R.id.main_page_toolbar );
        setSupportActionBar( mToolbar );
        getSupportActionBar().setTitle( "LMS Home" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( false );

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            sendToStart();

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent( HomeActivity.this, StartActivity.class );
        startActivity( startIntent );
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );

        getMenuInflater().inflate( R.menu.main_menu, menu );

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        if (item.getItemId() == R.id.main_logout_btn) {

            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }

        if (item.getItemId() == R.id.main_all_btn) {

            Intent settingsIntent = new Intent( HomeActivity.this, RegisterListActivity.class );
            startActivity( settingsIntent );

        }


        return true;
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.etevent:
                Intent i = new Intent( HomeActivity.this, EventListActivity.class );
                startActivity( i );
                break;
            case R.id.etchat:
                Intent i3 = new Intent( this, ChatListActivity.class );
                startActivity( i3 );
                break;
            case R.id.etmeeting:
                Intent i4 = new Intent( this, MeetingListActivity.class );
                startActivity( i4 );
                break;
            default:
                break;
        }

    }
}
