package com.android.highlifestudio.schnieselhunt;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SelectionActivity extends AppCompatActivity {

    ArrayList<Schnitzeljagd> liste;
    File path;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        setContentView(R.layout.activity_selection);
        View v =this.findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            path= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Schnieseljagd");
        }
        else {
            path = new File(Environment.getExternalStorageDirectory() + "/Documents/Schnieselhunt");
            path.mkdirs();
        }
        String file_name ="Schnitzeljagden.txt";
        file=new File(path,file_name);
        if(!path.mkdirs())
            Log.e("tag","path not created");
        liste=SchnitzeljagdApp.Schnitzeljagdlist;
        //file=new File(path,file_name);
        //write(v);
        //read(v);
        createMenu(v);
    }
    public void write(View view){
        String message ="Schnitzeljagd A, Gebiet A, 3:10, Strecke 200m, 01-10 \n" +
                "Schnitzeljagd B, Gebiet A, 1:20,Strecke 50m, 03-04 \n" +
                "Schnitzeljagd C,f,er,q,q\n" +
                "Schnitzeljagd D,er,r,r,r\n"+
                "Schnitzeljagd E,r,e,e,e\n"+ "Schnitzeljagd F,e,e,e,e\n"+ "Schnitzeljagd G,e,e,e,e\n"+
        "Schnitzeljagd H,e,e,e,e\n"+ "Schnitzeljagd I,e,e,e,e\n"+ "Schnitzeljagd J,e,e,e,e\n"+
                "Schnitzeljagd H,e,e,e,e\n"+ "Schnitzeljagd I,e,e,e,e\n"+ "Schnitzeljagd J,e,e,e,e\n";


        try {
            FileOutputStream wr= new FileOutputStream(file);
            wr.write(message.getBytes());

            Log.d("tag", file.getParent());
           wr.close();
           wr.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void read(View view){
        try {

            Scanner scan= new Scanner(file);


            while (scan.hasNextLine()){
                String line=scan.nextLine();
                Log.d("tag", line);
                String attr[]=line.split(",");
                Log.d("tag", "Testpunkt5");
                try {
                    if (attr.length > 1 && attr[0] != null && attr[1] != null && attr[2] != null && attr[3] != null && attr[4] != null);
                        //liste.add(new Schnitzeljagd(attr[0], attr[1], attr[2], attr[3], attr[4]));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(this, "mach deine File richtig!"+ e.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            scan.close();
            Log.d("tag","TESTPUNKT1");



        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates Dynamic Menu, List of all Schnitzeljagden in a Linear Scroll View Layout
     */
    public void createMenu(View v){
        LinearLayout rl =new LinearLayout(this);
        //rl.setBackgroundResource(R.drawable.nature2);
        rl.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView=new ScrollView(this);
        scrollView.addView(rl);
        Button view ;
        int id=0;
        for(int i=0; i<liste.size();i++) {
            final Schnitzeljagd s=liste.get(i);

            view = new Button(new ContextThemeWrapper(this,R.style.MyStyle));
            view.setBackgroundColor(0xFF3A4B4F);
            view.setText(s.getName());
            view.setId(id);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent i= new Intent(SelectionActivity.this, InfoActivity.class);
                    i.putExtra("Schnitzeljagd",(Parcelable)s);
                    startActivity(i);
                }
            });
            //view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            LinearLayout.LayoutParams details= new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            if(i==liste.size()-1)
                details.setMargins(100,100,100,100);
            else
                details.setMargins(100,100,100,0);
            details.gravity= Gravity.CENTER_HORIZONTAL;


            rl.addView(view, details);

        }
        setContentView(scrollView);
    }

}
