package com.example.chaewoon.chase_the_paint;

//package com.bluebirds.avinash.uberdemo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements LocationListener,
        OnMapReadyCallback, GoogleApiClient
                .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RotationGestureDetector.OnRotationGestureListener{
    private RotationGestureDetector mRotationDetector;

    View vieww;

    public final static String appName = "runningdrawing";

    CountDownTimer timeLeft;

    private GoogleMap mMap;
    private final int MY_LOCATION_REQUEST_CODE = 100;
    private Handler handler;
//    private GoogleApiClient googleApiClient;

    public final static int SENDING = 1;
    public final static int CONNECTING = 2;
    public final static int ERROR = 3;
    public final static int SENT = 4;
    public final static int SHUTDOWN = 5;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private Location previousLocation;

    List<Polyline> lines = new ArrayList<Polyline>();

    public boolean stoppedRunning = false;
    Button stopButton;

    public double estimatedDistance = 0;
    TextView distancebox;
    TextView timertext;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        distancebox = (TextView)findViewById(R.id.distanceField);
        timertext = (TextView)findViewById(R.id.timertext);
        stopButton = (Button)findViewById(R.id.stopButton);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mRotationDetector = new RotationGestureDetector(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        timeLeft = new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {
                timertext.setText("Time left (s): " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                stopLocationUpdates();
                stoppedRunning = true;
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                try {
                    mMap.setMyLocationEnabled(false);
                } catch (SecurityException e) {
                    //
                }
                stopButton.setText("Finish");
            }
        }.start();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case SENDING:

                        break;

                }

            }
        };
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near singapore, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lines.add(mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED)));

        // Add a marker in singapore and move the camera
        LatLng singapore = new LatLng(-1.3521, 103.8198);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
        mMap.getUiSettings().setRotateGesturesEnabled(false);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }


    }
    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }


   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    LatLng previouslatLng;
    List<LatLng> points;
    LatLng newPoint;

    @Override
    public void onLocationChanged(Location location) {
        if (newPoint != null)
        {
            previouslatLng = newPoint;
        }
        newPoint = new LatLng(location.getLatitude(), location.getLongitude());
        points = lines.get(lines.size()-1).getPoints();
        points.add(newPoint);
        lines.get(lines.size()-1).setPoints(points);

        double rota = 0.0;
        double startrota = 0.0;
        if (previousLocation != null) {

            rota = bearingBetweenLocations(previouslatLng, new LatLng(location.getLatitude
                    (), location.getLongitude()));

            final int R = 6371; // Radius of the earth

            double lat1 = previouslatLng.latitude;
            double lat2 = newPoint.latitude;
            double lon1 = previouslatLng.longitude;
            double lon2 = newPoint.longitude;
            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters

            distance = Math.pow(distance, 2);
            estimatedDistance += distance;
            distancebox.setText("Distance(m): " + String.format("%.02f", estimatedDistance));
            Log.d(TAG, "distance :" + estimatedDistance);
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
            //mRotationDetector = new RotationGestureDetector(this);
        }

        previousLocation = location;
        Log.d(TAG, "Firing onLocationChanged..........................");
        Log.d(TAG, "lat :" + location.getLatitude() + "long :" + location.getLongitude());
        Log.d(TAG, "bearing :" + location.getBearing());

//        new ServerConnAsync(handler, MapsActivity.this,location).execute();



    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

        String imageName = "Map";
        @Override
        public void onSnapshotReady(Bitmap snapshot) {

            Bitmap bmImg = snapshot;
            OutputStream fout = null;

            String filePath = "Map.jpeg";

            File filename;

            try {
                String path1 = android.os.Environment.getExternalStorageDirectory()
                        .toString();
                Log.i("in save()", "after mkdir");
                File file = new File(path1 + "/" + appName);
                if (!file.exists())
                    file.mkdirs();
                filename = new File(file.getAbsolutePath() + "/" + imageName
                        + ".jpg");
                Log.i("in save()", "after file");
                FileOutputStream out = new FileOutputStream(filename);
                Log.i("in save()", "after outputstream");
                bmImg.compress(Bitmap.CompressFormat.JPEG, 60, out);
                out.flush();
                out.close();
                Log.i("in save()", "after outputstream closed");
                //File parent = filename.getParentFile();
                ContentValues image = getImageContent(filename);
                Uri result = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
                Toast.makeText(getApplicationContext(),
                        "File is Saved in  " + filename, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

                e.printStackTrace();
            }
            ExitScreen();
        }
        public ContentValues getImageContent(File parent) {

            ContentValues image = new ContentValues();
            image.put(MediaStore.Images.Media.TITLE, appName);
            image.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
            image.put(MediaStore.Images.Media.DESCRIPTION, "App Image");
            image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            image.put(MediaStore.Images.Media.ORIENTATION, 0);
            image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString()
                    .toLowerCase().hashCode());
            image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName()
                    .toLowerCase());
            image.put(MediaStore.Images.Media.SIZE, parent.length());
            image.put(MediaStore.Images.Media.DATA, parent.getAbsolutePath());
            return image;
        }

    };

    public boolean mapShown = true;

    public void hideShowMap (View view) {
        if (mapShown) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style2_json));
                mapShown = false;

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);

            }
        }
        else {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_json));
                mapShown = true;
                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);

            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        //Log.d("RotationGestureDetector", "Touch detected");
        //stopLocationUpdates();
        mRotationDetector.dispatchTouchEvent(event);
        //startLocationUpdates();
        return super.dispatchTouchEvent(event);
    }
    @Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
        Log.d("RotationGestureDetector", "Rotating");
        float angle = rotationDetector.getAngle();
        angle = (float) Math.toRadians(angle);
        angle = angle * 0.1f;
        Log.d("RotationGestureDetector", "Rotation: " + Float.toString(angle));
        LatLng centrePoint = newPoint;
        for (int j = 0 ; j < lines.size(); j++){
            points = lines.get(j).getPoints();
            for (int i = 0; i < (points.size()); i++) {
                LatLng currentPoint = points.get(i);
                double ydiff = currentPoint.latitude - centrePoint.latitude;
                double xdiff = currentPoint.longitude - centrePoint.longitude;
                double r = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
                double teeta;
                if (xdiff > 0) {
                    teeta = Math.atan(ydiff / xdiff);
                } else if (xdiff < 0) {
                    teeta = Math.atan(ydiff / xdiff) + Math.PI;
                } else if (ydiff > 0) {
                    teeta = Math.PI * 0.5;
                } else {
                    teeta = Math.PI * -0.5;
                }
                teeta = teeta + angle;
                ydiff = r * Math.sin(teeta);
                xdiff = r * Math.cos(teeta);
                LatLng rotatedPoint = new LatLng((centrePoint.latitude + ydiff), (centrePoint.longitude + xdiff));
                points.set(i, rotatedPoint);
                Log.d("RotationGestureDetector", "Xdiff " + points.get(i).longitude);
            }
            lines.get(j).setPoints(points);
        }
    }

    public void ChangeColour(View view) {
        List<LatLng>startPoint = new ArrayList<>();
        startPoint.add(newPoint);
        String colour = view.getTag().toString();
        Log.d(TAG, "change colour " + colour);
        if (Objects.equals(colour,"red")) {
            Log.d(TAG, "changing to red");
            lines.add(mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED)));
            lines.get(lines.size()-1).setPoints(startPoint);
        } else
        if (Objects.equals(colour,"green")) {
            Log.d(TAG, "changing to green");
            lines.add(mMap.addPolyline(new PolylineOptions().width(5).color(Color.GREEN)));
            lines.get(lines.size()-1).setPoints(startPoint);
        } else
        if (Objects.equals(colour,"blue")) {
            Log.d(TAG, "changing to blue");
            lines.add(mMap.addPolyline(new PolylineOptions().width(5).color(Color.BLUE)));
            lines.get(lines.size()-1).setPoints(startPoint);

        } else
        if (Objects.equals(colour,"yellow")) {
            Log.d(TAG, "changing to yellow");
            lines.add(mMap.addPolyline(new PolylineOptions().width(5).color(Color.YELLOW)));
            lines.get(lines.size()-1).setPoints(startPoint);

        } else {
            Log.d(TAG, "no valid colour");
        }
    }

    public void StopRunning(View view) {
        if (!stoppedRunning) {
            stopLocationUpdates();
            timeLeft.cancel();
            stoppedRunning = true;
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            try {
                mMap.setMyLocationEnabled(false);
            } catch (SecurityException e) {
                //
            }
            stopButton.setText("Finish");
        } else {
            mMap.snapshot(callback);
        }
    }

    private void ExitScreen() {
        Intent intent = new Intent(this, Muliti.class);
        Log.d(TAG, "starting new activity");
        startActivity(intent);
        finish();
    }

}