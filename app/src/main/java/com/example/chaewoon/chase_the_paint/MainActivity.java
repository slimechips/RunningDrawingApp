package com.example.chaewoon.chase_the_paint;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public static String playerName;
    public static String playerEmail;
    public static String uid;
    public static Uri photoUrl;
    public TextView signInText;
    public Button signInOutButton;
    List<AuthUI.IdpConfig> providers;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        signInText = (TextView)findViewById(R.id.sign_in_text);
        signInOutButton = (Button)findViewById(R.id.sign_out_button);

        signInOutButton = (Button)findViewById(R.id.sign_out_button);
        signInOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    signOut();
                }
                else {
                    signIn();

                }
                            }
                        });
        signIn();
        //new UpdateSignInText().execute(playerName);

    }

    public void signIn() {
        if (user != null) {
            // com.example.chaewoon.chase_the_paint.User is signed in
            // Name, email address, and profile photo Url
            playerName = user.getDisplayName();
            playerEmail = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            signInOutButton.setText("Sign out");
            signInText.setText("Welcome " + playerName);
        } else {
            //user not signed in
            signInText.setText("Not signed in!");
            signInOutButton.setText("Sign in");
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    /*private class UpdateSignInText extends AsyncTask<String, Void, Void> {
        protected void doInBackground(String ... displayname) {
            if (displayname != null) {
                signInText.setText("Welcome " + displayname);
            }
            else
            {
                signInText.setText("Not signed in. Please sign in.");
            }
        }

    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                playerName = user.getDisplayName();
                playerEmail = user.getEmail();
                photoUrl = user.getPhotoUrl();
                uid = user.getUid();
                addNewUserToDatabase(playerName, playerEmail, uid);
/*
                OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew) {
                                addNewUserToDatabase(playerName, playerEmail, uid);
                            }
                            Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                        }
                    }
                };
*/
                signInOutButton.setText("Sign out");
                signInText.setText("Welcome " + playerName);
                Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                        signInText.setText("Not signed in!");
                        signInOutButton.setText("Sign in");
                    }
                });
        // [END auth_fui_signout]
    }

    public void addNewUserToDatabase (String name, String email, String userId) {

        Log.d("Tag", "user added");
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(name, email);
        mDatabase.child("users").child(userId).child("name").setValue(user.name);
        mDatabase.child("users").child(userId).child("email").setValue(user.email);
    }

    public void singleClicked(View v) {

/*        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, Singleplay.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "please sign in first", Toast.LENGTH_SHORT).show();
        }*/
    }
    public void multiClicked(View v) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, HostOrJoinActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "please sign in first", Toast.LENGTH_SHORT).show();
        }
    }
}

//TODO Social Media function, Actual Multiplayer Functionality (Host Game, Join Game,
//TODO Vote function), Points system, better main menu ui
//TODO App permissions
//TODO , proper character screen