package com.example.chaewoon.chase_the_paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Muliti extends AppCompatActivity {

    public final static String appName = "runningdrawing";

    ImageView playerMap;
    //List<ImageView> friendMap = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muliti);

        //friendMap.add((ImageView)findViewById(R.id.friendmap1));
        //friendMap.add((ImageView)findViewById(R.id.friendmap2));
        //riendMap.add((ImageView)findViewById(R.id.friendmap3));
        //friendMap.add((ImageView)findViewById(R.id.friendmap4));

        loadPlayerImage("Map.jpg");
        loadFriendImage((ImageView)findViewById(R.id.friendmap1),"FriendMap1.jpg");
        loadFriendImage((ImageView)findViewById(R.id.friendmap2),"FriendMap2.jpg");
        loadFriendImage((ImageView)findViewById(R.id.friendmap3),"FriendMap3.jpg");
        loadFriendImage((ImageView)findViewById(R.id.friendmap4),"FriendMap4.jpg");
    }

    private void loadPlayerImage(String imageName) {
        try {
            String path1 = android.os.Environment.getExternalStorageDirectory().toString()
                    + "/" + appName + "/" + imageName;
            playerMap = (ImageView) findViewById(R.id.paint1);
            Log.d("tag", path1);
            Bitmap bitmap = BitmapFactory.decodeFile(path1);
            playerMap.setImageBitmap(bitmap);
            Log.d("Loading Friend Img", "image set");
        }
        catch (Exception e) {
            Log.d("Loading player img", "player img not found");
        }
    }

    private void loadFriendImage(ImageView friendMap, String imageName) {
        try {
            String path1 = getCacheDir().toString() + "/" + imageName;
            Log.d("tag", path1);
            Bitmap bitmap = BitmapFactory.decodeFile(path1);
            friendMap.setImageBitmap(bitmap);
            Log.d("Loading Friend Img", "image set");

        }
        catch (Exception e) {
            Log.d("Loading Friend Img", "image not found");
        }

    }

    /*public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        //BitmapFactory.Options optionss = new BitmapFactory.Options();
        //optionss.inPreferredConfig = Bitmap.Config.RGB_565;


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(path,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;}
*/
}
