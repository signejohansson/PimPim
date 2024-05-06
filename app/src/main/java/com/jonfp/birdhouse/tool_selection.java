package com.jonfp.birdhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

public class tool_selection extends AppCompatActivity {
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_selection);
        Intent intent = getIntent();
        String tool = intent.getStringExtra("tool");
        if(tool != null && tool.equals("hammer")){
            textViewStatus = findViewById(R.id.instructionText);
            textViewStatus.setText("Klicka på hammaren");
            findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the new activity

                    Intent intent = new Intent(tool_selection.this, HammeringActivity.class);
                    startActivity(intent);
                }
            });
        }
        else if(tool != null && tool.equals("color")){
            textViewStatus = findViewById(R.id.instructionText);
            textViewStatus.setText("Klicka på färgburken");
            findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the new activity

                    Intent intent = new Intent(tool_selection.this, PhotoInstructionActivity.class);
                    startActivity(intent);
                }
            });
        } else{
            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the new activity

                    Intent intent = new Intent(tool_selection.this, SawingActivity.class);
                    startActivity(intent);
                }
            });
        }







    }
}