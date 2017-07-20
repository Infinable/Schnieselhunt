package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    Schnitzeljagd s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle data= getIntent().getExtras();
        s= data.getParcelable("Schnitzeljagd");
        
        //// TODO: 20.07.2017 : time & points anzeigen lassen 
        TextView dauer = (TextView) findViewById(R.id.TimeValue);
        dauer.setText(String.valueOf(getIntent().getExtras().getDouble("Time")));

        TextView punkte = (TextView) findViewById(R.id.PunkteValue);
        punkte.setText(String.valueOf(getIntent().getExtras().getDouble("Points")));



        Button btn=(Button) findViewById(R.id.return_to_home_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EndActivity.this, MainActivity.class);
                i.putExtra("Schnitzeljagd",(Parcelable)s);
                startActivity(i);
            }
        });
    }
}