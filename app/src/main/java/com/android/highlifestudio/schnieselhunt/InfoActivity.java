package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity which describes a specific Schnitzeljagd
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle data= getIntent().getExtras();
        Schnitzeljagd s= (Schnitzeljagd) data.getParcelable("Schnitzeljagd");

        TextView name=(TextView) findViewById(R.id.Name);
        name.setText(s.getName());

        TextView dauer= (TextView) findViewById(R.id.DauerValue);
        dauer.setText(s.getZeit());

        TextView strecke= (TextView) findViewById(R.id.StreckeValue);
        strecke.setText(s.getEntfernung());

        TextView start= (TextView) findViewById(R.id.StartValue);
        start.setText(s.getStartpunkt());

        Button btn=(Button) findViewById(R.id.lets_go_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(InfoActivity.this, GPSActivity.class);
                startActivity(i);
            }
        });

    }
}