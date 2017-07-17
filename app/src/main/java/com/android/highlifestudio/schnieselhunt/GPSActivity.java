package com.android.highlifestudio.schnieselhunt;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * GPS Activity which asks for permission to GPS and for the user to turn on GPS
 */
public class GPSActivity extends AppCompatActivity
        {
    static final int PERMISSION_ACCESS_FINE_LOCATION=1;
    static final String LOG_TAG="GPSActivity";


    private BroadcastReceiver broadcastReceiver;
    TextView latitudeText;
    TextView longitudeText;
    TextView prompt;
    Schnitzeljagd s;
    ArrayList<SLocation> SLocationArrayList;
    SLocation currRiddleLoc;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        s=getIntent().getExtras().getParcelable("Schnitzeljagd");
        SLocationArrayList =s.getSLocationArrayList();

        Log.d("tag",s.getSLocationArrayList().get(0).rätseltext);
        Iterator<SLocation> listIt;
        if(SLocationArrayList !=null && !SLocationArrayList.isEmpty()) {
            listIt = SLocationArrayList.iterator();
            currRiddleLoc = listIt.next();
        }

        if(checkPlayServices()) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)+ ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
        latitudeText= (TextView) findViewById(R.id.latitudeText);
        longitudeText= (TextView)findViewById(R.id.longitudeText);
        prompt=(TextView)findViewById(R.id.moveToLocationPrompt);
        prompt.setText("Please move to the next Location:\nLongitude "+currRiddleLoc.longitude+"\nLatitude: "+currRiddleLoc.latitude);

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
                    location=b.getParcelable("Location");
                    latitudeText.setText(b.getString("Latitude"));
                    longitudeText.setText(b.getString("Longitude"));
                    SchnitzeljagdApp.longitude=Double.valueOf(b.getString("Longitude"));
                    SchnitzeljagdApp.latitude=Double.valueOf(b.getString("Latitude"));
                }
            };
            registerReceiver(broadcastReceiver,new IntentFilter(GPSService.LOCATION_UPDATE));
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
                        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)+ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)+ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                            Log.d("TAG","Permission granted");
                        }
            }
        }




}
