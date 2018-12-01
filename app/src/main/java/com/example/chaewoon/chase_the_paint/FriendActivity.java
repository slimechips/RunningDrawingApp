package com.example.chaewoon.chase_the_paint;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendActivity extends AppCompatActivity {

    private final String TAG = "FriendActivity";
    public DatabaseReference mDatabase;
    public boolean validEmail;

    public EditText inputFriendEmail;
    public Button btnAddFriend;
    public TextView friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        inputFriendEmail = (EditText)findViewById(R.id.input_friendEmail);
        btnAddFriend = (Button)findViewById(R.id.btn_addFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmail = inputFriendEmail.getText().toString();
                addNewFriend(friendEmail);
            }
        });

        friendList = (TextView)findViewById(R.id.friendList);
        updateFriendList();
    }

    public void addNewFriend (final String friendEmail){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String userEmail = user.getEmail();
        validEmail = false;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = mDatabase.child("users");


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (Objects.equals(postSnapshot.child("email").getValue(), friendEmail)){
                        Log.d(TAG, "valid email");
                        validEmail = true;
                        postSnapshot.getRef().child("friends").child(userId).setValue(validEmail);
                        usersRef.child(userId).child("friends").child(postSnapshot.getKey()).setValue(validEmail);
                        break;
                    }
                }
                if (validEmail) {
                    Toast.makeText(FriendActivity.this, "friend added", Toast.LENGTH_SHORT).show();
                    updateFriendList();
                } else {
                    Toast.makeText(FriendActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public List<String> friendIdList;
    public List<User> friends;


    public void updateFriendList () {

        friendIdList = new ArrayList<String>();
        friends = new ArrayList<User>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final DatabaseReference usersRef = mDatabase.child("users");
        final DatabaseReference userFLRef = usersRef.child(userId).child("friends");

        userFLRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String friendId = postSnapshot.getKey();
                    friendIdList.add(friendId);

                }
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (String friendId : friendIdList) {
                            String friendName = dataSnapshot.child(friendId).child("name").getValue().toString();
                            String friendEmail = dataSnapshot.child(friendId).child("email").getValue().toString();
                            friends.add(new User(friendName, friendEmail));
                        }

                        Log.d(TAG, friendIdList.toString());
                        Log.d(TAG, friends.toString());

                        friendList.setText("");

                        int totalFriends = friends.size();

                        for (int friendCount = 0; friendCount < totalFriends; friendCount++) {
                            String friendText = Integer.toString(friendCount + 1) + ". " +
                                    friends.get(friendCount).name + "\n" + "Email: " +
                                    friends.get(friendCount).email + "\n\n";
                            friendList.append(friendText);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
