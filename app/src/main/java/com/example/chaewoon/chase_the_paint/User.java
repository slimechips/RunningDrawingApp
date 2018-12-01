package com.example.chaewoon.chase_the_paint;

import java.util.List;

public class User {
    public String name;
    public String email;
    public String userId;
    public class friends {
    }
    public String gameSession;

    public User(){

    }

    public User(String name, String email) {

        this.name = name;
        this.email = email;
    }

/*
    public User(String name, String email, String friendId) {

        this.name = name;
        this.email = email;
    }

    public User(String name, String email, List<String> friends, String gameSession) {

        this.name = name;
        this.email = email;
        this.friends = friends;
        this.gameSession = gameSession;
    }*/
}
