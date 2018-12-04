package com.example.chaewoon.chase_the_paint;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

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
    public Integer playerCount;

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
    }
    //convert from json file
    public GameSession(DataSnapshot dataSnapshot) {
        this.sessionId = (String)dataSnapshot.child("sessionid").getValue();
        this.drawingObject = (String)dataSnapshot.child("drawingObject").getValue();
        this.playerOne = (Player)dataSnapshot.child("player_a").getValue();
        this.playerTwo = (Player)dataSnapshot.child("player_b").getValue();
        this.playerThree = (Player)dataSnapshot.child("player_c").getValue();
        this.playerFour = (Player)dataSnapshot.child("player_d").getValue();
        this.playerFive = (Player)dataSnapshot.child("player_e").getValue();
        this.playerCount = (Integer)dataSnapshot.child("player_count").getValue();
        this.sessionEnded = (Boolean)dataSnapshot.child("session_ended").getValue();
    }

    public final void addPlayer (Player player) {
        if (this.playerTwo == null) {this.playerFive = player;}
        else if (this.playerThree == null) {this.playerThree = player;}
        else if (this.playerFour == null) {this.playerFour = player;}
        else if (this.playerFive == null) {this.playerFive = player;}
        playerCount++;
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
