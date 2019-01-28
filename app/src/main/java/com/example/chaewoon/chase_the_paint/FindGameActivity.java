package com.example.chaewoon.chase_the_paint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class FindGameActivity extends AppCompatActivity {

    private final String TAG = "FindGameActivity";
    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_game);
        parentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout);
        getJoinSessions();
    }

    public void getJoinSessions() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final DatabaseReference currentUserFlRef = mDatabase.child("users").child(userId).child("friends");
        final DatabaseReference usersRef = mDatabase.child("users");

        final ValueEventListener getFriends = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String friendId = postSnapshot.getKey();
                    getFriendSessions(usersRef, mDatabase, friendId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        currentUserFlRef.addListenerForSingleValueEvent(getFriends);

    }

    public void getFriendSessions(DatabaseReference usersRef, final DatabaseReference mDatabase, final String friendId) {
        usersRef.child(friendId).child("game_sessions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String sessionId = postSnapshot.getKey();
                    getSessionInformation(mDatabase, sessionId, friendId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getSessionInformation(final DatabaseReference mDatabase, final String sessionId, final String friendId) {
        mDatabase.child("game_sessions").child(sessionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String drawingObject = null;
                int playerCount = 0;
                String hostName = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (Objects.equals(postSnapshot.getKey(), "drawingObject")) {
                        drawingObject = postSnapshot.getValue().toString();
                    } else if (Objects.equals(postSnapshot.getKey(), "player_count")) {
                        playerCount = Integer.parseInt(postSnapshot.getValue().toString());
                    }

                    if(drawingObject != null && playerCount != 0) {
                        getHostName(mDatabase, sessionId, friendId, drawingObject, playerCount);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getHostName(DatabaseReference mDatabase, final String sessionId, String friendId,
                            final String drawingObject, final int playerCount) {
        mDatabase.child("game_sessions").child(sessionId).child("player_a")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String hostName;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(postSnapshot.getKey(), "playerName")) {
                        hostName = postSnapshot.getValue().toString();
                        Player host = new Player(hostName);
                        GameSession validSession = new GameSession(sessionId, drawingObject, host, playerCount);
                        addCard(validSession);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCard(final GameSession validSession) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card_join_game, null);
        ViewGroup cardGroup = (ViewGroup)card;

        TextView hostNameText = (TextView)cardGroup.findViewWithTag("text_host");
        String hostName = validSession.playerOne.playerName;
        hostNameText.setText("Host: " + hostName);

        TextView drawingNameText = (TextView)cardGroup.findViewWithTag("text_drawing");
        String drawingName = validSession.drawingObject;
        drawingNameText.setText("Drawing: " + drawingName);

        TextView playerCountText = (TextView)cardGroup.findViewWithTag("text_players");
        int playerCount = validSession.playerCount;
        playerCountText.setText("Players: " + Integer.toString(playerCount));

        ImageView objectImage = (ImageView)cardGroup.findViewWithTag("image_drawing_join_small");
        String _objectImageReference = (String)getResources().getText(getResources()
                .getIdentifier(drawingName, "string", getPackageName()));
        objectImage.setImageURI(Uri.parse(_objectImageReference));

        parentLinearLayout.addView(card, parentLinearLayout.getChildCount() - 1);

        cardGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    joinGame(validSession.sessionId);
                }
                return true;
            }
        });
    }

    public void joinGame(String sessionId) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String userName = user.getDisplayName();
        final @Nullable DatabaseReference gameSessionRef = mDatabase.child("game_sessions").child(sessionId);
        final DatabaseReference usersRef = mDatabase.child("users");

        if (gameSessionRef != null) {
            //game session exists
            gameSessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GameSession currentSession = new GameSession(dataSnapshot);
                    String playerLetter = null;
                    Player currentUser;
                    if ((Long)dataSnapshot.child("player_count").getValue() > 5L)
                    {Toast.makeText(getApplicationContext(), "Lobby Full", Toast.LENGTH_SHORT).show(); }
                    else if (Objects.equals(userId, dataSnapshot.child("player_a").child("playerId").getValue()))
                    { playerLetter = "player_a"; }
                    else if (Objects.equals(userId, dataSnapshot.child("player_b").child("playerId").getValue()))
                    { playerLetter = "player_b"; }
                    else if (Objects.equals(userId, dataSnapshot.child("player_c").child("playerId").getValue()))
                    { playerLetter = "player_c"; }
                    else if (Objects.equals(userId, dataSnapshot.child("player_d").child("playerId").getValue()))
                    { playerLetter = "player_d"; }
                    else if (Objects.equals(userId, dataSnapshot.child("player_e").child("playerId").getValue()))
                    { playerLetter = "player_e"; }
                    else {
                        currentUser = new Player(userId, userName, 0.0);
                        playerLetter = currentSession.addPlayer(currentUser);
                    }
                    goToMultiplayActivity(currentSession, playerLetter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Invalid Game Session", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToMultiplayActivity(GameSession currentSession, String playerLetter) {

        Intent intent = new Intent(this, multiplay.class);
        intent.putExtra("Drawing Object", currentSession.drawingObject);
        intent.putExtra("Session Id", currentSession.sessionId);
        intent.putExtra("Player Letter", playerLetter);
        startActivity(intent);
        finish();
    }
}
