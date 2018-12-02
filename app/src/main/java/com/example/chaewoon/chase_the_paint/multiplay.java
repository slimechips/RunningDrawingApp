package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class multiplay extends AppCompatActivity {

    public TextView drawingName;
    public ImageView drawingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);

        //Definitions for drawing names and images
        drawingName = (TextView)findViewById(R.id.drawingName);
        drawingImage = (ImageView)findViewById(R.id.drawingImage);

        String _objectName = getIntent().getStringExtra("Drawing Object");
        String _objectImageReference = (String)getResources().getText(getResources()
                .getIdentifier(_objectName, "string", getPackageName()));

        _objectName = _objectName.substring(0, 1).toUpperCase() + _objectName.substring(1);
        drawingName.setText(_objectName);
        drawingImage.setImageURI(Uri.parse(_objectImageReference));
    }

    public void voteclick (View view){

        Intent intent = new Intent(this, Muliti.class);

        startActivity(intent);

    }

    public void drawMulti (View view){

        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);
    }

}
