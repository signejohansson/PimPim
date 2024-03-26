package com.jonfp.birdhouse;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class SawingActivity extends AccelerometerActivity {

    private boolean saw_is_extended = false;
    private int saw_movement_changes = 0;
    private static final int MOVEMENT_THRESHOLD = 4;
    private static final int movement_changes_THRESHOLD = 1;

    private TextView textViewStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawing);
        textViewStatus = findViewById(R.id.textViewSawingStatus);
    }


    @Override
    protected void setupMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.saw_short);
    }

    @Override
    protected void handleMotion(float x, float y, float z) {
        // Sawing logic goes here
        if(y > MOVEMENT_THRESHOLD){
            saw_is_extended = true;
            saw_movement_changes++;
        } else{
            saw_is_extended = false;
        }
        if(saw_movement_changes > movement_changes_THRESHOLD) {
            System.out.println("YOU ARE SAWING");
            textViewStatus.setText("Status: Sawing");
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            saw_movement_changes = 0;
            textViewStatus.setText("Status: Not Sawing");
        }
    }
}
