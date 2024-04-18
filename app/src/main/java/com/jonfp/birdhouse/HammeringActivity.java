package com.jonfp.birdhouse;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class HammeringActivity extends AccelerometerActivity {

    private boolean hammer_is_extended = false;
    private int hammer_movement_changes = 0;

    private boolean redirecting = false;
    private int hammer_count = 0;
    private static final int MOVEMENT_THRESHOLD = 13; // Adjusted threshold for hammering
    private static final int movement_changes_THRESHOLD = 1; // Adjusted threshold for hammering
    private TextView textViewStatus;
    private TextView textStatus;

    @Override
    protected void setupMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.hammer_sound);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawing);
        textViewStatus = findViewById(R.id.textViewSawingStatus);
        textStatus = findViewById(R.id.textViewSawingInstructions);
        textStatus.setText("Börja hammra!");


    }


    @Override
    protected void handleMotion(float x, float y, float z) {
        // Hammering logic goes here

        System.out.println(Math.abs(x));

        if(Math.abs(x) > MOVEMENT_THRESHOLD){
            hammer_is_extended = true;
            hammer_movement_changes++;
        } else{
            hammer_is_extended = false;
        }

        if(hammer_movement_changes > movement_changes_THRESHOLD) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            textViewStatus.setText("Status: Hammering");

            System.out.println("YOU ARE hammering");
            hammer_movement_changes = 0;
            hammer_count++;
            if (hammer_count >= 8) {
                redirectToNewActivity();
            }
            textViewStatus.setText("Status: Not hammering");

        }
    }

    private void redirectToNewActivity() {
        System.out.println("redirect");
        if(redirecting){
            return;
        }
        redirecting = true;
        // Play sound effect for transitioning to the next activity
        MediaPlayer nextActivitySound = MediaPlayer.create(this, R.raw.finished);
        if (nextActivitySound != null) {
            nextActivitySound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            nextActivitySound.start();
        }
        Intent intent = new Intent(this, ListeningActivity.class);
        startActivity(intent);
        finish(); // Finish current activity to prevent going back to it on back press
    }
}
