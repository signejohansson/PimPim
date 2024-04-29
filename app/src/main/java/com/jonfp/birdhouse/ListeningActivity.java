package com.jonfp.birdhouse;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ListeningActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private MediaPlayer mediaPlayer;
    private ImageView background;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.birdnoise);
        background = findViewById(R.id.imageView2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register proximity sensor listener
        if (proximitySensor != null)
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister proximity sensor listener to avoid battery drain
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            if (distance < proximitySensor.getMaximumRange()) {
                // User is near the proximity sensor, play sound
                playSound();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    private void playSound() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            background.setImageResource(R.drawable.finished);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play sound effect for transitioning to the next activity

                Intent intent = new Intent(ListeningActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish current activity to prevent going back to it on back press
            }
        }, 3200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release(); // Release the media player
    }

}
