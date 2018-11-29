package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void singleClicked(View v) {

        Intent intent = new Intent(this, Singleplay.class);

        startActivity(intent);
    }
    public void multiClicked(View v) {

        Intent intent = new Intent(this, HostOrJoinActivity.class);

        startActivity(intent);
    }
}

//TODO Social Media function, Logins, Actual Multiplayer Functionality (Host Game, Join Game,
//TODO Vote function), Points system, better main menu ui, colours selected can be  seen
//TODO App permissions, Firebase
//TODO change yellow colour, proper character screen