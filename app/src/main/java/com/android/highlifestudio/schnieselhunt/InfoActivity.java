package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity which describes a specific Schnitzeljagd
 */
public class InfoActivity extends AppCompatActivity {
    Schnitzeljagd s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle data= getIntent().getExtras();
        s= data.getParcelable("Schnitzeljagd");

        TextView name=(TextView) findViewById(R.id.Name);
        name.setText(s.getName());

        TextView dauer= (TextView) findViewById(R.id.DauerValue);
        dauer.setText(s.getZeit());

        TextView strecke= (TextView) findViewById(R.id.StreckeValue);
        strecke.setText(s.getEntfernung());

        TextView difficulty=(TextView) findViewById(R.id.Schwierigkeit);
        difficulty.setText(s.difficulty);

        TextView description= (TextView) findViewById(R.id.Beschreibung);
        description.setText(s.beschreibung);

        Button btn=(Button) findViewById(R.id.lets_go_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(InfoActivity.this, GPSActivity.class);
                i.putExtra("Schnitzeljagd",(Parcelable)s);
                startActivity(i);
            }
        });

    }
}
