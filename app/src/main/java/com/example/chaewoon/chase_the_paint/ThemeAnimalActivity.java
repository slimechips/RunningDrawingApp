package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThemeAnimalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_animal);
    }

    public void goToHostScreen (View v) {
        Intent intent = new Intent(this, multiplay.class);
        startActivity(intent);
        finish();
    }
}
