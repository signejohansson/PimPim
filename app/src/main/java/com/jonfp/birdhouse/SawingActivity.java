package com.jonfp.birdhouse;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SawingActivity extends AccelerometerActivity {

    private ImageView background;
    private Vibrator vibrator;
    private Sensor gyroscope;
    private boolean isCorrectOrientation = false; // to check if the device orientation is right for sawing

    // Constants for the filters and thresholds
    private static final int MOVEMENT_THRESHOLD = 6;
    private static final float ALPHA = 0.8f; // for the low-pass filter
    private float[] gravity = new float[3];
    private float lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawing);
        background = findViewById(R.id.imageView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Register the gyroscope in addition to the accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void setupMedia() {
        mediaPlayer = MediaPlayer.create(this, R.raw.saw_short);
    }

    @Override
    protected void handleMotion(float x, float y, float z) {
        // Apply low-pass filter to isolate the gravity component for y-axis
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * y;

        float filteredY = y - gravity[1];
        if (isCorrectOrientation && Math.abs(filteredY - lastY) > MOVEMENT_THRESHOLD) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                vibrator.vibrate(50); // Vibrate for 50 milliseconds
            }
            lastY = filteredY;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        super.onSensorChanged(event); // Let the base class handle the accelerometer
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Example: Check for a certain range of rotation to ensure correct orientation for sawing
            float rotationRateX = event.values[0]; // Assuming rotation around X-axis indicates the sawing motion
            if (Math.abs(rotationRateX) > 1.0) { // Threshold for gyroscope to decide if the orientation is right
                isCorrectOrientation = true;
            } else {
                isCorrectOrientation = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gyroscope != null) {
            sensorManager.unregisterListener(this, gyroscope);
        }
    }

    private void redirectToNewActivity() {
        Intent intent = new Intent(SawingActivity.this, tool_selection.class);
        startActivity(intent);
        finish();
    }
}
