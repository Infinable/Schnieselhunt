package com.android.highlifestudio.schnieselhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initButtons();
    }

    public void initButtons(){
        findViewById(R.id.selection_button).setOnClickListener(buttonClickListener);
        findViewById(R.id.add_button).setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.selection_button:
                    Intent menuToSelection = new Intent(MenuActivity.this, SelectionActivity.class);
                    startActivity(menuToSelection);
                    break;
                case R.id.add_button:
                    Intent menuToAdd = new Intent(MenuActivity.this, AddActivity.class);
                    startActivity(menuToAdd);
                    break;
            }
        }
    };


}
