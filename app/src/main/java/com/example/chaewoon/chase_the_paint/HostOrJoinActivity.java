package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class HostOrJoinActivity extends AppCompatActivity {

    Map<String, Class> activities = new HashMap<String, Class>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_or_join);

        activities.put("com.example.chaewoon.chase_the_paint:id/hostbutton", ThemeActivity.class);
        //activities.put("com.example.chaewoon.chase_the_paint:id/joinbutton", JoinActivity.class);

    }

    public void changeHostOrJoinScreen(View v) {
        int id = v.getId();
        String activityName = getResources().getResourceName(id);
        Class activityPage = activities.get(activityName);
        Intent intent = new Intent(this, activityPage);
        startActivity(intent);
    }
}