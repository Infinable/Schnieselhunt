package com.android.highlifestudio.schnieselhunt;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.w3c.dom.Text;

/**
 * GPS Activity which asks for permission to GPS and for the user to turn on GPS
 */
public class GPSActivity extends AppCompatActivity
        {
    static final int PERMISSION_ACCESS_FINE_LOCATION=1;
    static final String LOCATION_UPDATE="Location update";
    static final String LOG_TAG="GPSActivity";

    private BroadcastReceiver broadcastReceiver;
    TextView latitudeText;
    TextView longitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);


        if(checkPlayServices()) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
        latitudeText= (TextView) findViewById(R.id.latitudeText);
        longitudeText= (TextView)findViewById(R.id.longitudeText);

    }



    static final int PLAY_SERVICES_RESOLUTION_REQUEST=9000;

    private boolean checkPlayServices(){
        GoogleApiAvailability gAPI= GoogleApiAvailability.getInstance();
        int result=gAPI.isGooglePlayServicesAvailable(this);
        if(result!=ConnectionResult.SUCCESS) {
            if (gAPI.isUserResolvableError(result)) {
                gAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        Log.d("TAG","Client connected");
        super.onStart();
        startService(new Intent(this,GPSService.class));

    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, GPSService.class));
        super.onStop();
        Log.d(LOG_TAG,"Stopped");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver==null)
            broadcastReceiver= new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle b=intent.getExtras();
                    latitudeText.setText(b.getString("Latitude"));
                    longitudeText.setText(b.getString("Longitude"));
                }
            };
            registerReceiver(broadcastReceiver,new IntentFilter(LOCATION_UPDATE));
    }

    @Override
    protected void onPause() {
        if(broadcastReceiver!=null)
            unregisterReceiver(broadcastReceiver);
        super.onPause();
    }




    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case PERMISSION_ACCESS_FINE_LOCATION:
                    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED )
                        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                            Log.d("TAG","Permission granted");
                        }
            }
        }




}
