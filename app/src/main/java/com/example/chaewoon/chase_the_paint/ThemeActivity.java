package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ThemeActivity extends AppCompatActivity {

    Map<String, Class> activities = new HashMap<String, Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        activities.put("com.example.chaewoon.chase_the_paint/animalsbutton", ThemeAnimalActivity.class);
        activities.put("com.example.chaewoon.chase_the_paint/fruitbutton", ThemeFruitActivity.class);
        //activities.put("com.example.chaewoon.chase_the_paint/lovebutton", ThemeAnimalActivity.class);
    }

    public void changeScreen (CardView v){
        int id = v.getId();
        String activityName = getResources().getResourceName(id);
        Class activityPage = activities.get(activityName);
        Intent intent = new Intent(this, activityPage);
    }
}
