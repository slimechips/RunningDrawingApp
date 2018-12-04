package com.example.chaewoon.chase_the_paint;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Player {

    public String playerId;
    public String playerName;
    public Double distanceScore;
    public StorageReference playerMapLocation;

    public Player(){

    }

    public Player(String playerName) {

        this.playerName = playerName;
    }

    public Player(String playerId, String playerName) {

        this.playerId = playerId;
        this.playerName = playerName;
    }

    public Player(String playerId, String playerName, Double distanceScore) {

        this.playerId = playerId;
        this.playerName = playerName;
        this.distanceScore = distanceScore;
    }


    public Player(String playerId, String playerName, Double distanceScore, StorageReference playerMapLocation) {

        this.playerId = playerId;
        this.playerName = playerName;
        this.distanceScore = distanceScore;
        this.playerMapLocation = playerMapLocation;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("playerId", this.playerId);
        result.put("playerName", this.playerName);
        result.put("distanceScore", this.distanceScore);
        result.put("playerMapLocation", this.playerMapLocation);
        return result;
    }

    //Convert from json hashmap to object form
    public static Player ConvertToPlayer(Object playerObject) {

        if (playerObject != null) {
            HashMap playerMap = (HashMap) playerObject;
            Player player = new Player();
            if (playerMap.containsKey("distanceScore")) {
                player.distanceScore = (double) (long) playerMap.get("distanceScore");
            }
            if (playerMap.containsKey("playerName")) {
                player.playerName = (String) playerMap.get("playerName");
            }
            if (playerMap.containsKey("playerId")) {
                player.playerId = (String) playerMap.get("playerId");
            }
            if (playerMap.containsKey("playerMapLocation")) {
                player.playerMapLocation = (StorageReference) playerMap.get("playerMapLocation");
            }
            Log.d("asdf", "one success");
            return player;
        }
        else {
            return null;
        }
    }
}