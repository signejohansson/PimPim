package com.jonfp.birdhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonprank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play video from drawable
                VideoView videoView = new VideoView(MainActivity.this);
                setContentView(videoView);

                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.output);
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        });

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
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


