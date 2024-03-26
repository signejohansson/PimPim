package com.jonfp.birdhouse;

import android.media.MediaPlayer;

public class HammeringActivity extends AccelerometerActivity {

    private boolean hammer_is_extended = false;
    private int hammer_movement_changes = 0;
    private static final int MOVEMENT_THRESHOLD = 13; // Adjusted threshold for hammering
    private static final int movement_changes_THRESHOLD = 10; // Adjusted threshold for hammering

    @Override
    protected void setupMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.hammer);
    }

    @Override
    protected void handleMotion(float x, float y, float z) {
        // Hammering logic goes here
        if(x > MOVEMENT_THRESHOLD){
            hammer_is_extended = true;
            hammer_movement_changes++;
        } else{
            hammer_is_extended = false;
        }

        if(hammer_movement_changes > movement_changes_THRESHOLD) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            System.out.println("YOU ARE hammering");
        }
    }
}
