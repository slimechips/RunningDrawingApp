package com.example.chaewoon.chase_the_paint;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GameSession {
    public String sessionId;
    public String drawingObject;
    public Player playerOne;
    public Player playerTwo;
    public Player playerThree;
    public Player playerFour;
    public Player playerFive;
    public Boolean sessionEnded;
    public int playerCount;

    public GameSession(){};

    public GameSession(String sessionId, String drawingObject, Player playerOne, int playerCount) {
        this.sessionId = sessionId;
        this.drawingObject = drawingObject;
        this.playerOne = playerOne;
        this.playerCount = playerCount;
    }

    public GameSession(String sessionId, String drawingObject, Player playerOne) {
        this.sessionId = sessionId;
        this.drawingObject = drawingObject;
        this.playerOne = playerOne;
        this.sessionEnded = false;
        this.playerCount = 1;
    }

    public GameSession(String sessionId, String drawingObject, Player playerOne, Player playerTwo,
                        Player playerThree, Player playerFour, Player playerFive, Boolean sessionEnded) {
        this.sessionId = sessionId;
        this.drawingObject = drawingObject;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerThree = playerThree;
        this.playerFour = playerFour;
        this.playerFive = playerFive;
        this.sessionEnded = sessionEnded;
        this.playerCount = 5;
    }
    //convert from json file
    public GameSession(DataSnapshot dataSnapshot) {
        this.sessionId = (String)dataSnapshot.child("session_id").getValue();
        this.drawingObject = (String)dataSnapshot.child("drawingObject").getValue();
        this.playerOne = Player.ConvertToPlayer(dataSnapshot.child("player_a").getValue());
        this.playerTwo = Player.ConvertToPlayer(dataSnapshot.child("player_b").getValue());
        this.playerThree = Player.ConvertToPlayer(dataSnapshot.child("player_c").getValue());
        this.playerFour = Player.ConvertToPlayer(dataSnapshot.child("player_d").getValue());
        this.playerFive = Player.ConvertToPlayer(dataSnapshot.child("player_e").getValue());
        this.playerCount = ((Long)dataSnapshot.child("player_count").getValue()).intValue();
        this.sessionEnded = (Boolean)dataSnapshot.child("session_ended").getValue();
    }

    public final String addPlayer (Player player) {
        String playerLetter = "player_a";
        if (this.playerTwo == null) {this.playerTwo = player; playerLetter = "player_b";}
        else if (this.playerThree == null) {this.playerThree = player; playerLetter = "player_c";}
        else if (this.playerFour == null) {this.playerFour = player; playerLetter = "player_d";}
        else if (this.playerFive == null) {this.playerFive = player; playerLetter = "player_e";}
        this.playerCount++;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference gameSessionRef = mDatabase.child("game_sessions").child(this.sessionId);
        gameSessionRef.updateChildren(this.toMap());
        return playerLetter;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("session_id", sessionId);
        result.put("drawingObject", drawingObject);
        result.put("player_a", playerOne);
        result.put("player_b", playerTwo);
        result.put("player_c", playerThree);
        result.put("player_d", playerFour);
        result.put("player_e", playerFive);
        result.put("player_count", playerCount);
        result.put("session_ended", sessionEnded);
        return result;
    }
}
