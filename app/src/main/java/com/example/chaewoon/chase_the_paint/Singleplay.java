package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Singleplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplay);
    }

    public void singleClickedd (View v) {

        Intent intent = new Intent(Singleplay.this, Single.class);

        startActivity(intent);
    }

    public void uploadClick(View v) {

        Toast.makeText(getApplicationContext(),"upload complete", Toast.LENGTH_SHORT).show();

    }
}
