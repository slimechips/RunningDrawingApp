package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class multiplay extends AppCompatActivity {

    public final String TAG = "multiplay";
    public TextView drawingName;
    public ImageView drawingImage;
    public DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);

        //Definitions for drawing names and images
        drawingName = (TextView)findViewById(R.id.drawingName);
        drawingImage = (ImageView)findViewById(R.id.drawingImage);

        String _objectName = getIntent().getStringExtra("Drawing Object");
        final String _sessionKey = getIntent().getStringExtra("Session Id");

        String _objectImageReference = (String)getResources().getText(getResources()
                .getIdentifier(_objectName, "string", getPackageName()));

        _objectName = _objectName.substring(0, 1).toUpperCase() + _objectName.substring(1);
        drawingName.setText(_objectName);
        drawingImage.setImageURI(Uri.parse(_objectImageReference));

        setHostName(_sessionKey);
    }

    public void voteclick (View view){

        Intent intent = new Intent(this, Muliti.class);

        startActivity(intent);

    }

    public void drawMulti (View view){

        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);
    }

    public String hostName;
    public TextView hostNameView;

    private void setHostName (final String sessionKey) {

        hostNameView = (TextView)findViewById(R.id.hostName);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference sessionRef = mDatabase.child("game_sessions").child(sessionKey);

        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(Objects.equals(postSnapshot.getKey(), "player_a")) {
                        hostName = postSnapshot.child("playerName").getValue().toString();
                        hostNameView.setText(hostName);
                        Log.d(TAG + "onDatachange", hostName);
                        break;
                    }
                }
                setPlayerNameUpdater(sessionKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<TextView> playerNameViews;

    private void setPlayerNameUpdater(final String sessionKey)
    {
        playerNameViews = new ArrayList<>();
        playerNameViews.add((TextView)findViewById(R.id.playerAName));
        playerNameViews.add((TextView)findViewById(R.id.playerBName));
        playerNameViews.add((TextView)findViewById(R.id.playerCName));
        playerNameViews.add((TextView)findViewById(R.id.playerDName));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sessionRef = mDatabase.child("game_sessions").child(sessionKey);

        sessionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot.child("playerName").getValue());
                for(TextView currentView : playerNameViews) {
                    @Nullable String currentViewText = currentView.getText().toString();
                    Log.d(TAG, "testa" + currentViewText);
                    if(Objects.equals("", currentViewText)) {
                        Log.d(TAG, "null");
                        currentView.setText(dataSnapshot.child("playerName").getValue(String.class));
                        break;
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
