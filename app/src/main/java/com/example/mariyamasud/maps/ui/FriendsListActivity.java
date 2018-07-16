package com.example.mariyamasud.maps.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.example.mariyamasud.maps.R;
import com.example.mariyamasud.maps.model.Friend;
import com.example.mariyamasud.maps.model.Message;
import com.example.mariyamasud.maps.model.User;
import com.example.mariyamasud.maps.utilis.Constants;
import com.example.mariyamasud.maps.utilis.EmailEncoding;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class FriendsListActivity extends AppCompatActivity {

    private String TAG = "Friends List Activity";

    private ListView mListView;
    private Toolbar mToolBar;

    private FirebaseListAdapter mFriendListAdapter;
    private ValueEventListener mValueEventListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mCurrentUsersFriends;
    private FirebaseAuth mFirebaseAuth;

    private final List<String> mUsersFriends = new ArrayList<>();
    private String mCurrentUserEmail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_activity);
        initializeScreen();

        mToolBar.setTitle("Find new friends");

        showUserList();
    }

    private void showUserList(){
        mFriendListAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.friend_item, mUserDatabaseReference) {
            @Override
            protected void populateView(final View view, User user, final int position) {
                //Log.e("TAG", user.toString());

                final String email = EmailEncoding.commaEncodePeriod(user.getEmail());
                //Check if this user is already your friend
                final DatabaseReference friendRef =
                        mFirebaseDatabase.getReference(Constants.FRIENDS_LOCATION
                                + "/" + mCurrentUserEmail + "/" + EmailEncoding.commaEncodePeriod(email));

                friendRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(email.equals(mCurrentUserEmail)){
                            view.findViewById(R.id.addFriend).setVisibility(View.GONE);
                            view.findViewById(R.id.removeFriend).setVisibility(View.GONE);
                        }else if(dataSnapshot.getValue() != null){
                            Log.w(TAG, "User is friend");
                            view.findViewById(R.id.addFriend).setVisibility(View.GONE);
                            view.findViewById(R.id.removeFriend).setVisibility(View.VISIBLE);
                        }else{
                            Log.w(TAG, "User is not friend");
                            view.findViewById(R.id.removeFriend).setVisibility(View.GONE);
                            view.findViewById(R.id.addFriend).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(user.getProfilePicLocation() != null && user.getProfilePicLocation().length() > 0){
                    StorageReference storageRef = FirebaseStorage.getInstance()
                            .getReference().child(user.getProfilePicLocation());
                    Glide.with(view.getContext())
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .bitmapTransform(new CropCircleTransformation(view.getContext()))
                            .into((ImageView)view.findViewById(R.id.photoImageView));
                }
                if(!email.equals(mCurrentUserEmail)) {
                    ((TextView) view.findViewById(R.id.messageTextView)).setText(user.getUsername());
                    ((TextView) view.findViewById(R.id.nameTextView)).setText(EmailEncoding.commaDecodePeriod(email));
                }else{
                    ((TextView) view.findViewById(R.id.messageTextView)).setText("You");
                    ((TextView) view.findViewById(R.id.nameTextView)).setText(user.getUsername());
                }
                (view.findViewById(R.id.addFriend)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Clicking row: " + position);
                        Log.w(TAG, "Clicking user: " + email);
                        //Add this user to your friends list, by email
                        addNewFriend(email);
                    }
                });
                (view.findViewById(R.id.removeFriend)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Clicking row: " + position);
                        Log.w(TAG, "Clicking user: " + email);
                        //Add this user to your friends list, by email
                        removeFriend(email);
                    }
                });
              }
        };

        mListView.setAdapter(mFriendListAdapter);

        mValueEventListener = mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null){
                    finish();
                    return;
                }
                mFriendListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeFriend(String friendEmail){
        //Get current user logged in by email
        final String userLoggedIn = mFirebaseAuth.getCurrentUser().getEmail();
        Log.e(TAG, "User logged in is: " + userLoggedIn);
        final DatabaseReference friendsRef = mFirebaseDatabase.getReference(Constants.FRIENDS_LOCATION
                + "/" + EmailEncoding.commaEncodePeriod(userLoggedIn));
        friendsRef.child(EmailEncoding.commaEncodePeriod(friendEmail)).removeValue();
    }

    private void addNewFriend(String newFriendEmail){
        //Get current user logged in by email
        final String userLoggedIn = mFirebaseAuth.getCurrentUser().getEmail();
        Log.e(TAG, "User logged in is: " + userLoggedIn);
        //final String newFriendEncodedEmail = EmailEncoding.commaEncodePeriod(newFriendEmail);
        final DatabaseReference friendsRef = mFirebaseDatabase.getReference(Constants.FRIENDS_LOCATION
                + "/" + EmailEncoding.commaEncodePeriod(userLoggedIn));
        //Add friends to current users friends list
        friendsRef.child(newFriendEmail).setValue(newFriendEmail);
    }

    private void initializeScreen(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserEmail = EmailEncoding.commaEncodePeriod(mFirebaseAuth.getCurrentUser().getEmail().toString());
        //Eventually this list will filter out users that are already your friend
        mUserDatabaseReference = mFirebaseDatabase.getReference().child(Constants.USERS_LOCATION);
        mCurrentUsersFriends = mFirebaseDatabase.getReference().child(Constants.FRIENDS_LOCATION
            + "/" + EmailEncoding.commaEncodePeriod(mFirebaseAuth.getCurrentUser().getEmail()));

        mListView = (ListView) findViewById(R.id.friendsListView);
        try {
            mToolBar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (RuntimeException  e){}
    }
}
