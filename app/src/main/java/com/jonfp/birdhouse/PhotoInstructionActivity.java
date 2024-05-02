package com.jonfp.birdhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoInstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_instruction);

        Button startCameraButton = findViewById(R.id.start_camera);
        startCameraButton.setVisibility(View.INVISIBLE);

        startCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CameraActivity
                Intent intent = new Intent(PhotoInstructionActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
