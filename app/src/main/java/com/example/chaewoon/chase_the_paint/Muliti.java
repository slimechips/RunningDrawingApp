package com.example.chaewoon.chase_the_paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Muliti extends AppCompatActivity {

    public final static String appName = "runningdrawing";
    public List<Player> currentPlayers;
    public List<TextView> playerTextViews = new ArrayList<TextView>();
    TextView titleText;



    ImageView playerMap;
    TextView userTextView;
    List<ImageView> friendMap = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muliti);

        titleText = (TextView)findViewById(R.id.drawingName);

        friendMap.add((ImageView)findViewById(R.id.friendmap1));
        friendMap.add((ImageView)findViewById(R.id.friendmap2));
        friendMap.add((ImageView)findViewById(R.id.friendmap3));
        friendMap.add((ImageView)findViewById(R.id.friendmap4));

        playerTextViews.add((TextView)findViewById(R.id.text_name_player_a));
        playerTextViews.add((TextView)findViewById(R.id.text_name_player_b));
        playerTextViews.add((TextView)findViewById(R.id.text_name_player_c));
        playerTextViews.add((TextView)findViewById(R.id.text_name_player_d));

        setDrawingObject();
        setPlayerImageUpdater();
    }

    public String drawingObject;


    private void setDrawingObject() {

        DatabaseReference _mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
        String _userName = _user.getDisplayName();
        DatabaseReference sessionRef = _mDatabase.child("game_sessions").child(getIntent()
                .getStringExtra("Session Id"));
        playerMap = (ImageView) findViewById(R.id.paint1);
        userTextView = (TextView) findViewById(R.id.text_name_user);

        sessionRef.child("drawingObject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drawingObject = (String) dataSnapshot.getValue();
                titleText.setText(drawingObject);
                setUser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String text1;
    String text2;

    private void setUser () {

        DatabaseReference _mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
        String _userName = _user.getDisplayName();
        DatabaseReference sessionRef = _mDatabase.child("game_sessions").child(getIntent()
                .getStringExtra("Session Id"));
        playerMap = (ImageView) findViewById(R.id.paint1);
        userTextView = (TextView) findViewById(R.id.text_name_user);



        try {

            String path1 = android.os.Environment.getExternalStorageDirectory().toString()
                    + "/" + appName + "/" + "Map.jpg";
            text1 =_userName + "'s Score: " + getIntent().getStringExtra("Distance Score");
            text2 = "\nVotes: 0" + "\nClick to like this drawing!";
            userTextView.setText(text1 + text2);
            Log.d("tag", path1);
            Bitmap bitmap = BitmapFactory.decodeFile(path1);
            playerMap.setImageBitmap(bitmap);
            Log.d("Loading Friend Img", "image set");
        }
        catch (Exception e) {
            Log.d("Loading player img", "player img not found");
        }


        sessionRef.child(getIntent().getStringExtra("Player Letter")).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (Objects.equals(dataSnapshot.getValue(), "likes")) {
                            text2 = "Votes: " + dataSnapshot.getValue() + "\nClick to like this drawing!";
                            userTextView.setText(text1 + text2);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

    }

    private void getSessionData () {

        DatabaseReference _mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
        final String _userId = _user.getUid();
        DatabaseReference sessionRef = _mDatabase.child("game_sessions").child(getIntent()
                .getStringExtra("Session Id"));
        final String[] playerRefs = new String[]{"player_a", "player_b", "player_c", "player_d", "player_e"};
        currentPlayers = new ArrayList<>();

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
    public int currentViewCount = 0;

    private void setPlayerImageUpdater() {
        DatabaseReference _mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
        final String _userId = _user.getUid();
        final DatabaseReference sessionRef = _mDatabase.child("game_sessions").child(getIntent()
                .getStringExtra("Session Id"));
        final String[] playerRefs = new String[]{"player_a", "player_b", "player_c", "player_d", "player_e"};

        sessionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (Arrays.asList(playerRefs).contains(dataSnapshot.getKey())
                        && !Objects.equals(dataSnapshot.child("playerId").getValue(), _userId)) {
                    currentViewCount++;
                    Player currentPlayer = Player.ConvertToPlayer(dataSnapshot.getValue());
                    Log.d("as", currentPlayer.playerName);
                    currentPlayer.likes = 0;
                    //currentPlayers.add(currentPlayer);
                    String displayText = currentPlayer.playerName + "'s Score: "
                            + Math.round(currentPlayer.distanceScore) + "\nLikes: " + currentPlayer.likes
                            + "\nClick to like this drawing!";

                    playerTextViews.get(currentViewCount - 1).setText(displayText);
                    loadWithGlide(currentPlayer);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i<playerRefs.length; i++) {
                    String currentPlayerRef = playerRefs[i];
                    if (Objects.equals(dataSnapshot.getValue(), currentPlayerRef)) {
                        Player currentPlayer = Player.ConvertToPlayer(dataSnapshot.getValue());
                        String displayText = currentPlayer.playerName + "'s Score: "
                                + Math.round(currentPlayer.distanceScore) + "\nLikes: " + currentPlayer.likes
                                + "\nClick to like this drawing!";
                    }

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void loadWithGlide (Player currentPlayer) {
        ImageView currentImageView = friendMap.get(currentViewCount-1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference playerMapRef = storageRef.child(currentPlayer.playerMapLocation);


        GlideApp.with(this).load(playerMapRef).into(currentImageView);
        Log.d("asdf", "glided finish");
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
