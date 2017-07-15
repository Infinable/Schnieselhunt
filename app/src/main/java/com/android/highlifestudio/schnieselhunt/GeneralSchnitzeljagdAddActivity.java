package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GeneralSchnitzeljagdAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    Button next;
    EditText time, distance, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_schnitzeljagd_add);

        time=(EditText)findViewById(R.id.ZeitText);
        distance=(EditText)findViewById(R.id.StreckeText);
        name=(EditText)findViewById(R.id.NameText);
        next=(Button)findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                if (name.getText().toString()!="Name der Schnitzeljagd" && name.getText().toString()!="") {
                    b.putString("name",name.getText().toString());
                    if (time.getText().toString() != "Zeitangabe" && time.getText().toString() != "") {
                        b.putString("time", time.getText().toString());
                        {
                            if (distance.getText().toString() != "Länge der Strecke" && distance.getText().toString() != "") {

                                b.putString("distance", distance.getText().toString());
                                b.putString("difficulty",spinner.getSelectedItem().toString());
                                Intent i=new Intent(GeneralSchnitzeljagdAddActivity.this, AddActivity.class);
                                i.putExtras(b);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(GeneralSchnitzeljagdAddActivity.this, "Bitte einen Streckenwert eingeben!", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(GeneralSchnitzeljagdAddActivity.this, "Bitte einen Zeitwert eingeben!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(GeneralSchnitzeljagdAddActivity.this,"Bitte einen Namen eingeben",Toast.LENGTH_SHORT).show();
            }

        });

        spinner=(Spinner)findViewById(R.id.SchwierigkeitSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.difficulties,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
