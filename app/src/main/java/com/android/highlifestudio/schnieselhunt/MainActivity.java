package com.android.highlifestudio.schnieselhunt;

import android.*;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        setContentView(R.layout.activity_main);
        initButtons();
    }


    public void initButtons(){
        findViewById(R.id.start_button).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.start_button:
                    Intent mainToMenu = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(mainToMenu);
                    break;
            }
        }
    };
}
