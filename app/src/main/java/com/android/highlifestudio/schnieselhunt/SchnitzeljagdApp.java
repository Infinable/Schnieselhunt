package com.android.highlifestudio.schnieselhunt;

import android.*;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by Angelo on 11.07.2017.
 */

public class SchnitzeljagdApp extends Application {
    /*
     * current location, updated with
     * broadcastreceiver implemented on service using class
     */
    public static double longitude;
    public static double latitude;
    public static String filename="Schnitzeljagden.srl";

    public static ArrayList<Schnitzeljagd> Schnitzeljagdlist;
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        readFiles();
        context=getApplicationContext();
    }

    public static void readFiles(){

        ObjectInputStream inputStream=null;
        ArrayList<Schnitzeljagd>temp=null;
        try {
            File path=new File(new File(Environment.getExternalStorageDirectory(), "") + File.separator + filename);
            if(!path.exists())
                new FileOutputStream(path);
            Log.d("Tag",path.getAbsolutePath());
            inputStream = new ObjectInputStream(new FileInputStream(path));
            temp=(ArrayList<Schnitzeljagd>)inputStream.readObject();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {

            if(temp!=null) {
                Schnitzeljagdlist = temp;
                Log.d("tag", temp.toString());
                if(temp.isEmpty()||temp.get(0)!=null)
                Log.d("tag",temp.get(0).getSLocationArrayList().get(0).rätseltext);
            }
            else Schnitzeljagdlist=new ArrayList<Schnitzeljagd>();
            if(!Schnitzeljagdlist.isEmpty())
            Log.d("tag",Schnitzeljagdlist.get(0).getName());

        }
    }
}
