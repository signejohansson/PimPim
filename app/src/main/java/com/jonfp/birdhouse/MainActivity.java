package com.jonfp.birdhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the new activity

                Intent intent = new Intent(MainActivity.this, tool_selection.class);
                startActivity(intent);
            }
        });

        //Shortcut knapp till kameran och vidare!
        findViewById(R.id.buttonCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the new activity

                Intent intent2 = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent2);
            }
        });

    }
}


