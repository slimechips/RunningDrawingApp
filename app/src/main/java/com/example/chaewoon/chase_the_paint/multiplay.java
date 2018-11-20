package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class multiplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);
    }

    public void voteclick (View v){

            Intent intent = new Intent(this,Muliti.class);

            startActivity(intent);

    }
}
