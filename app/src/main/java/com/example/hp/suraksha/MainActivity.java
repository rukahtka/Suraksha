package com.example.hp.suraksha;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import android.widget.Toast;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsApi;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks, OnConnectionFailedListener {

    ImageButton mAlarmImageBtn;
    TextView mNameText, mCollegeIDText;
    ImageView mImage;
    static int postStatus;
    LocationManager mLocationManager;
    TextView txt;
    SharedPreferences shared;
    SharedPreferences.Editor edt;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    String[] PERMISSIONS = new String[]{"android.permission.ACCESS_FINE_LOCATION"};
    int PERMISSION_ALL = 1;
    private MyLocationListener myLocationLisner;
    private LocationRequest mLocationRequest;
    private boolean isGoogleClientInitilize;
    private Bitmap bitmap;
    class MyLocationListener implements LocationListener {
        MyLocationListener() {
        }

        public void onLocationChanged(Location location) {
            Log.d("test123", "Location:" + location);
            if (location != null) {
                WebUtil.setCurrentLocation(location.getLatitude(), location.getLongitude());

            }
        }

    }

    class ClickListener implements OnClickListener {
        ClickListener() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MainActivity.this.finish();
        }
    }


    class Result implements ResultCallback<LocationSettingsResult> {
        Result() {
        }

        public void onResult(LocationSettingsResult result) {
            Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case 0:
                    MainActivity.this.mGoogleApiClient.connect();
                    Log.d("test", "Location Access is already enabled, No need to show location dialog");
                    return;
                case 6:
                    Log.d("test", "Location Permission Off : Ask to enable Access to Location from settings");
                    try {
                        status.startResolutionForResult(MainActivity.this, 3);
                        return;
                    } catch (SendIntentException e) {
                        return;
                    }
                default:
                    return;
            }
        }
    }


    public void createLocationRequest() {
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(60000);
        this.mLocationRequest.setFastestInterval(60000);
        this.mLocationRequest.setPriority(100);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(this.mLocationRequest);
        Log.d("test", "Location request is created now checking the Location access status from settings");
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            this.mGoogleApiClient.connect();
        }
        LocationServices.SettingsApi.checkLocationSettings(this.mGoogleApiClient, builder.build()).setResultCallback(new Result());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        shared = this.getSharedPreferences("shared", Context.MODE_PRIVATE);
         edt = shared.edit();
        if (!shared.contains("register")) {
            startActivity(new Intent(MainActivity.this, SubmitClass.class));
        }

        setContentView(R.layout.activity_main);

        if (!hasPermissions(this, this.PERMISSIONS)) {
            Log.d("test", "Inside permission check method hasPermissions");
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL);
        }


        mAlarmImageBtn = (ImageButton) findViewById(R.id.alarmBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.lay_sendmessagedialogbox);
                txt = (TextView) dialog.findViewById(R.id.dialogBoxTextView);
                Button btn = (Button) dialog.findViewById(R.id.dialogBoxSendBtn);
                dialog.setTitle("Send Message");
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!WebUtil.isLocationSet()) {
                            dialog.dismiss();
                        }
                        if (txt.length() != 0) {
                            if (WebUtil.isLocationSet()) {
                                new MessageToDB().execute(shared.getString("collegeID", null), txt.getText().toString(), String.valueOf(WebUtil.getCurLat()), String.valueOf(WebUtil.getCurLog()));
                                Log.d("678","sendTo: "+WebUtil.getCurLat()+"  "+WebUtil.getCurLog());
                                dialog.dismiss();
                                new SentMessageDataBaseOpenHelper(MainActivity.this, "db_problemDataBase", null, 1).addProblem(txt.getText().toString(), "" + Calendar.getInstance().getTime());
                            }
                        }
                        if (!WebUtil.isLocationSet()) {
                            Toast.makeText(MainActivity.this, "Waiting for location...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }


        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        mCollegeIDText= (TextView) header.findViewById(R.id.collegeIDText);
        mNameText= (TextView) header.findViewById(R.id.studName);
        mCollegeIDText.setText(shared.getString("collegeID",null).toUpperCase());
        mNameText.setText(shared.getString("Name",null).toUpperCase());


        mAlarmImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Finish");
                builder.setMessage("Want to send Emergency Alert to admin???");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           /* if(mLastLocation==null){
                                new MessageToDB().execute(shared.getString("collegeID", null), "I need help",String.valueOf(18.48816214),String.valueOf(73.79103356));
                                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                            }*/
                        if (WebUtil.isLocationSet()) {

                            new MessageToDB().execute(shared.getString("collegeID", null), "I need help", String.valueOf(WebUtil.getCurLat()), String.valueOf(WebUtil.getCurLog()));
                            Log.d("678","sendTo: "+WebUtil.getCurLat()+"  "+WebUtil.getCurLog());
                            Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                        }
                        if (!WebUtil.isLocationSet()) {
                            Toast.makeText(MainActivity.this, "Waiting for location...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You Pressed NO Btn", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sentMessage) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SentMessageListFragment()).addToBackStack(null).commit();

            // Handle the camera action
        } else if (id == R.id.nav_receivedMessage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReceivedMessagesFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_aboutCommittee) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutCommitte()).addToBackStack(null).commit();

        } else if (id == R.id.nav_aboutApp) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutApp()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("test123", "Google Api Client Connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("got123","got Location");
        this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
        if (this.mLastLocation != null) {
            Log.d("test123", "Lat:" + this.mLastLocation.getLatitude());
            Log.d("test123", "Log:" + this.mLastLocation.getLongitude());
            WebUtil.setCurrentLocation(this.mLastLocation.getLatitude(), this.mLastLocation.getLongitude());
        }
        startLocationUpdate();
    }



    public void startLocationUpdate() {
        try {
            this.myLocationLisner = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, this.myLocationLisner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (locationMode != 0) {
                return true;
            }
            return false;
        }
        String locationProviders = Secure.getString(context.getContentResolver(), "location_mode");
        Log.d("test", "");
        if (TextUtils.isEmpty(locationProviders)) {
            return false;
        }
        return true;
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (VERSION.SDK_INT >= 23 && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        } else if (!(VERSION.SDK_INT >= 23 || context == null || permissions == null)) {
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        if (isLocationEnabled(this)) {
            if (!this.isGoogleClientInitilize) {
                this.isGoogleClientInitilize = true;
                this.mGoogleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
                this.mGoogleApiClient.connect();
            }
            createLocationRequest();
            return;
        }
        createLocationRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("test", "inside onRequestPermissionsResult : LocationPermission else");
                    new AlertDialog.Builder(this).setTitle("Suraksha Location").setPositiveButton("OK", new ClickListener()).show();
                    return;
                }
                Log.d("test", "inside onRequestPermissionsResult : LocationPermission if ");
                this.mGoogleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
                this.mGoogleApiClient.connect();
                createLocationRequest();
                return;

        }
    }



}

