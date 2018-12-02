package com.example.chaewoon.chase_the_paint;

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

    public GameSession(String sessionId, String drawingObject, Player playerOne) {
        this.sessionId = sessionId;
        this.drawingObject = drawingObject;
        this.playerOne = playerOne;
        this.sessionEnded = false;
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

        return result;
    }
}
