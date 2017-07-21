package com.android.highlifestudio.schnieselhunt;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import static com.android.highlifestudio.schnieselhunt.GPSActivity.PERMISSION_ACCESS_FINE_LOCATION;

/**
 * Functionality: Check for GPS Permission
 *        Check for GPS enabled
 *        Start GPS Service if given
 */
public class AddActivity extends AppCompatActivity {

    private TextView latitudeText,longitudeText;
    Button add,addPicture,locAdd;
    ImageView imageView;
    EditText riddleText;
    String imageName;
    String path;
    File photoFile;
    Location location;
    /**
     * development purpose
     */
    File textfile;

    //Liste von aktuellen Orten
    ArrayList<SLocation> list=new ArrayList<>();

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    //Request code
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private BroadcastReceiver broadcastReceiver;
    static String LOG_TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = this.getSharedPreferences("Share",Context.MODE_PRIVATE);
        edit=pref.edit();
        if(pref.getInt("value",0)==0) {
            edit.putInt("value", 0);
            edit.commit();
        }

        StrictMode.VmPolicy.Builder builder= new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initWidgets();

        //check if google play services is there and check for gps
        if (checkPlayServices()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)+ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)+ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
    }
    public void writeToTextFile(){
        try {
            /**developer purpose
             *
             */
            textfile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
                    ,"Schnieselhunt"+File.separator+"Schnitzeljagd"+pref.getInt("" +
                    "value",0)+".txt");
            textfile.getParentFile().mkdirs();
            Log.d(LOG_TAG,"new File created under: "+textfile.getAbsolutePath());
            FileOutputStream f=new FileOutputStream(textfile);
            PrintWriter wr= new PrintWriter(f);
            Bundle b=getIntent().getExtras();
            wr.append("Name: "+b.getString("name")+"\n");
            wr.append("Description: "+b.getString("description")+"\n");
            wr.println("Time: "+b.getString("time"));
            wr.println("Distance "+b.getString("distance"));
            wr.println("Difficulty"+b.getString("difficulty"));
            Iterator<SLocation> iterator=list.iterator();
            while (iterator.hasNext()){
                SLocation location=iterator.next();
                wr.println("First Location");
                wr.println("Latitude: "+location.latitude);
                wr.println("Longitude: "+location.longitude);
                wr.println("Rätseltext: "+location.rätseltext);
            }
            wr.close();
            f.flush();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initWidgets(){
        locAdd=(Button)findViewById(R.id.addLocation);
        locAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocToList();
            }
        });
        add=(Button)findViewById(R.id.addSchnitzeljagd);
        add.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(list.isEmpty()){
                    Toast.makeText(AddActivity.this.getApplicationContext(),"Füge bitte zumindest einen Ort der Schnitzeljagd hinzu!",Toast.LENGTH_SHORT).show();
                    return;
                }
                writeToTextFile();
                writeToFile();
                Toast.makeText(AddActivity.this,"Erfolgreich Schnitzeljagd gespeichert!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        longitudeText=(TextView)findViewById(R.id.LongitudeTextView);
        latitudeText=(TextView)findViewById(R.id.LatitudeTextView);

        addPicture=(Button)findViewById(R.id.addPictureBtn);
        addPicture.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile=null;
                int count=pref.getInt("value",0);

                imageName=getIntent().getStringExtra("name")+(count++);
                Log.d(LOG_TAG,"Count: "+count+"Image name: "+imageName);
                edit.putInt("value",count);
                edit.commit();

                Log.d(LOG_TAG,imageName);
                File folder= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Schnieselhunt");
                folder.mkdir();
                Log.d(LOG_TAG,folder.getAbsolutePath());
                photoFile=new File(folder,imageName+".jpg");
                path=photoFile.getAbsolutePath();
                Log.d(LOG_TAG,path);


                if(takePic.resolveActivity(getPackageManager())!=null)
                    if(photoFile!=null) {
                        //Uri photoURI= Uri.fromFile(photoFile);
                        takePic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
                    }
                }

        });
        imageView=(ImageView)findViewById(R.id.locationImage);
        riddleText=(EditText)findViewById(R.id.riddletext);
    }

    public void addLocToList(){
        if(riddleText.getText().toString().equals("")){
            Toast.makeText(this,"Bitte eine Beschreibung zum jetzigen Ort hinzufügen. Optional ist auch ein Bild hinzufügbar",Toast.LENGTH_SHORT).show();
            return;
        }
        SLocation sLocation=new SLocation(riddleText.getText().toString(),path,SchnitzeljagdApp.longitude,SchnitzeljagdApp.latitude);
        if(list!=null)
        list.add(sLocation);
        else list=new ArrayList<>();
        imageView.setImageResource(android.R.color.transparent);
        riddleText.setText("");
    }
    /**
     * adds current Location with Schnitzeljagd to list in application as new Schnitzeljagd and saves extended list in file
     * dependency: Schnitzeljagdlist in Application is latest
     */
    public void writeToFile(){
        Bundle b=getIntent().getExtras();
        Schnitzeljagd s=new Schnitzeljagd(b.getString("name"),b.getString("description"),b.getString("time"),b.getString("distance"),"",b.getString("difficulty"),list);
        Log.d(LOG_TAG,b.getString("difficulty"));
        SchnitzeljagdApp.Schnitzeljagdlist.add(s);
        try{
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(new File(new File(Environment.getExternalStorageDirectory(),"")+File.separator+SchnitzeljagdApp.filename)));
            oos.writeObject(SchnitzeljagdApp.Schnitzeljagdlist);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            SchnitzeljagdApp.readFiles();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK && resultCode!=RESULT_CANCELED) {

            try {
                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(photoFile));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
        int result = gAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (gAPI.isUserResolvableError(result)) {
                gAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "Client connected");
        super.onStart();
        startService(new Intent(this, GPSService.class));

    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, GPSService.class));
        super.onStop();
        Log.d(LOG_TAG, "Stopped");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null)
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle b = intent.getExtras();
                    location=b.getParcelable("Location");
                    latitudeText.setText(b.getString("Latitude"));
                    longitudeText.setText(b.getString("Longitude"));
                    SchnitzeljagdApp.longitude = Double.valueOf(b.getString("Longitude"));
                    SchnitzeljagdApp.latitude = Double.valueOf(b.getString("Latitude"));

                }
            };
        registerReceiver(broadcastReceiver, new IntentFilter(GPSService.LOCATION_UPDATE));
    }

    @Override
    protected void onPause() {
        if (broadcastReceiver != null)
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
