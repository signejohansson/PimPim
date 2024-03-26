package com.jonfp.birdhouse;

import android.media.MediaPlayer;

public class SawingActivity extends AccelerometerActivity {

    private boolean saw_is_extended = false;
    private int saw_movement_changes = 0;
    private static final int MOVEMENT_THRESHOLD = 7;
    private static final int movement_changes_THRESHOLD = 5;

    @Override
    protected void setupMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.saw);
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
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }
}
