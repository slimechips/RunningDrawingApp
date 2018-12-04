package com.example.chaewoon.chase_the_paint;

import java.util.HashMap;

public class Player {

    public String playerId;
    public String playerName;
    public int distanceScore;
    public String playerMapLocation;

    public Player(String playerName) {

        this.playerName = playerName;
    }

    public Player(String playerId, String playerName) {

        this.playerId = playerId;
        this.playerName = playerName;
    }

    public Player(String playerId, String playerName, int distanceScore) {

        this.playerId = playerId;
        this.playerName = playerName;
        this.distanceScore = distanceScore;
    }


    public Player(String playerId, String playerName, int distanceScore, String playerMapLocation) {

        this.playerId = playerId;
        this.playerName = playerName;
        this.distanceScore = distanceScore;
        this.playerMapLocation = playerMapLocation;
    }

    public Player toPlayer(HashMap playerMap) {

        
        this.distanceScore = playerMap.get("distanceScore");
    }
}