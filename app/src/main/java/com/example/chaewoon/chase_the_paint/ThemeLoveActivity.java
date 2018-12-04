package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ThemeLoveActivity extends AppCompatActivity {

    View heartCard;
    View ringCard;
    View roseCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_love);

        heartCard = (View)findViewById(R.id.heartCard);
        heartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo("heart");
            }
        });

        ringCard = (View)findViewById(R.id.ringCard);
        ringCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo("ring");
            }
        });

        roseCard = (View)findViewById(R.id.roseCard);
        roseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo("rose");
            }
        });
    }

    public void uploadInfo(String drawingObject) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = user.getUid();
        final String userName = user.getDisplayName();
        String sessionKey = mDatabase.child("game_sessions").push().getKey();
        Player host = new Player(userId, userName);
        GameSession game = new GameSession(sessionKey, drawingObject, host);
        Map<String, Object> gameValues = game.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/game_sessions/" + sessionKey, gameValues);
        childUpdates.put("/users/" + userId + "/game_sessions/" + sessionKey, game.drawingObject);

        mDatabase.updateChildren(childUpdates);

        goToHostScreen(drawingObject, sessionKey);
    }

    public void goToHostScreen(String drawingObject, String sessionKey) {
        Intent intent = new Intent(this, multiplay.class);
        intent.putExtra("Drawing Object", drawingObject);
        intent.putExtra("Session Id", sessionKey);
        startActivity(intent);
        finish();
    }
}
