
package com.jonfp.birdhouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAccelerometer extends AppCompatActivity implements SensorEventListener {

        private SensorManager sensorManager;
        private Sensor accelerometer;
        private float[] accelerometerReading = new float[3];

        boolean saw_is_extended = false;
        int saw_movement_changes = 0;
        private static final int MOVEMENT_THRESHOLD = 7;
        private static final int movement_changes_THRESHOLD = 5;


        int hammer_movement_changes = 0;
        boolean hammer_is_extended = false;

        private MediaPlayer mediaPlayer;





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_accelerometer);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.saw);
        }
        @Override
        protected void onResume() {
            super.onResume();
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                if(z > 10 || z < -10){
                    saw_movement_changes = 0;
                    hammer_movement_changes = 0;
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    return;
                }

                check_saw_motion(x,y,z);
                check_hammer_motion(x,y,z);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used, but required to implement SensorEventListener
        }

        public void check_saw_motion(float x, float y, float z){

            if(y > MOVEMENT_THRESHOLD){
                saw_is_extended = true;
                saw_movement_changes++;
            } else{
                saw_is_extended = false;
            }
            if(saw_movement_changes > movement_changes_THRESHOLD) {
                System.out.println("YOU ARE SAWING");
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.saw);

                    mediaPlayer.start();
                }
            }

        }

    public void check_hammer_motion(float x, float y, float z){

        if(x > MOVEMENT_THRESHOLD+6){
            hammer_is_extended = true;
            hammer_movement_changes++;
        } else{
            hammer_is_extended = false;
        }

        if(hammer_movement_changes > movement_changes_THRESHOLD+5) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer = MediaPlayer.create(this, R.raw.hammer);

                mediaPlayer.start();
            }
            System.out.println("YOU ARE hammering");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}
