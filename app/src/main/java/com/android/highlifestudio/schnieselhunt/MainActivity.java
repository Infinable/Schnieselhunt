package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
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
