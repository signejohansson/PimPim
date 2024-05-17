package com.jonfp.birdhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class ToolSelectionActivity extends AppCompatActivity {
    private TextView textViewStatus;
    Vibrator vibrator;
    private void vibrate(){

        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26
                vibrator.vibrate(500);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_selection);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent intent = getIntent();
        String tool = intent.getStringExtra("tool");
        textViewStatus = findViewById(R.id.instructionText);
        if(tool != null && tool.equals("hammer")){
            textViewStatus.setText("Klicka på hammaren");
        }
        else if(tool != null && tool.equals("color")){
            textViewStatus.setText("Klicka på färgburken");

        }

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assume 'tool' is a String variable defined elsewhere in your code
                if (tool != null && tool.equals("color")) {
                    Intent intent = new Intent(ToolSelectionActivity.this, PhotoInstructionActivity.class);
                    startActivity(intent);
                } else {
                    // Vibrate the phone for 500 milliseconds
                    vibrate();
                }
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the new activity


                if (tool != null && tool.equals("hammer")) {
                    Intent intent = new Intent(ToolSelectionActivity.this, HammeringActivity.class);
                    startActivity(intent);
                } else {
                    // Vibrate the phone for 500 milliseconds
                    vibrate();
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the new activity
                if (tool == null) {
                    Intent intent = new Intent(ToolSelectionActivity.this, SawingActivity.class);
                    startActivity(intent);
                } else {
                    // Vibrate the phone for 500 milliseconds
                    vibrate();
                }

            }
        });

    }
}