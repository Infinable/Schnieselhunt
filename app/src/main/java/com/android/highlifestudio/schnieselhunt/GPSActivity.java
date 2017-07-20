package com.android.highlifestudio.schnieselhunt;

import android.Manifest;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
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
            //Text at the top, describing the Activity
    TextView prompt;
    Schnitzeljagd s;
    ArrayList<SLocation> SLocationArrayList;
    SLocation currRiddleLoc;
    Location location;
            //text to next loc
    TextView rätseltext;
            //image to next Loc
    ImageView rätselImage;
            //increased when User reaches one Location
    int counter=0;
    int maxLength = 0;
    double time = 0;
    double points = 0;
    Handler handler;
    boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        s=getIntent().getExtras().getParcelable("Schnitzeljagd");
        SLocationArrayList =s.getSLocationArrayList();
        maxLength = SLocationArrayList.size()-1;

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
        rätselImage=(ImageView) findViewById(R.id.Rätselimage);
        rätseltext=(TextView) findViewById(R.id.Rätseltext);
        rätseltext.setText(SLocationArrayList.get(counter).rätseltext);
        File imgFile=null;
        if(SLocationArrayList.get(counter).picpath!=null)
        imgFile=new File(SLocationArrayList.get(counter).picpath);
        if(imgFile!=null && imgFile.exists()){
            Bitmap bitmap= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            rätselImage.setImageBitmap(bitmap);
        }


        handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Bardia", "run was called");
                if (!finished)
                    time++;
                Log.d("Bardia", "time: "+ time);
                if (!finished)
                    handler.postDelayed(this, 1000);
            }
        };
        if (!finished) {
            handler.postDelayed(runnable, 1000);
        }

    }

    private boolean checkLocation(Location l, int a){
        float accuracy = l.getAccuracy();
        Location temp = new Location("");
        temp.setLatitude(SLocationArrayList.get(counter).latitude);
        temp.setLongitude(SLocationArrayList.get(counter).longitude);
        float distance = l.distanceTo(temp);
        Log.d("haha", String.valueOf(accuracy));

        if (distance < (accuracy + (accuracy/2))){
            if (maxLength == counter)
                finished = true;
            TaskStackBuilder builder= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                builder = TaskStackBuilder.create(this);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.addParentStack(GPSActivity.class);
            }
            if (!finished) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_event_available_black_24dp)
                        .setContentTitle("Du hast das Ziel gefunden!")
                        .setContentText("Glückwunsch, du hast dein Ziel erreicht, schaue nach was dein nächstes Ziel ist.");
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, mBuilder.build());

                Toast.makeText(this, "Glückwunsch, du hast den Punkt gefunden.", Toast.LENGTH_SHORT).show();
            }
            else if (finished) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_event_available_black_24dp)
                        .setContentTitle("Du hast das letzte Ziel erreicht!")
                        .setContentText("Herzlichen Glückwunsch, du hast das letzte Ziel gefunden. :)");
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, mBuilder.build());

                Toast.makeText(this, "Wow, du hast alle Punkte gefunden!! Gut gemacht. :)", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return false;
    }

    private void refreshLayout() {
        if(SLocationArrayList.size()<=counter)
            return;
        prompt.setText("Schlage dich vor zum nächsten Ort. Bereits abgelaufene Orte: "+(counter+1)+
        "Geschwindigkeit: "+" Zeit: "+location.getTime());
        rätseltext.setText(SLocationArrayList.get(counter).rätseltext);
        File imgFile = null;
        if (SLocationArrayList.get(counter).picpath != null)
            imgFile = new File(SLocationArrayList.get(counter).picpath);
        if (imgFile != null && imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            rätselImage.setImageBitmap(bitmap);
        }
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

                    if (counter == maxLength && checkLocation(location, counter)) {
                        points = (1 / time) * 3000;
                        Log.d("Bardia", "ENDPUNKTE: " + points);
                        Intent i = new Intent(GPSActivity.this, EndActivity.class);
                        i.putExtra("Schnitzeljagd",(Parcelable) s);
                        i.putExtra("Time", time);
                        i.putExtra("Points", points);
                        startActivity(i);
                    }
                    else if (checkLocation(location, counter)){
                        Log.d(LOG_TAG,"Counter erhöht. Bisheriger Wert: "+counter);
                        counter++;
                        refreshLayout();
                    }
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
