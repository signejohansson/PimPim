package com.jonfp.birdhouse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.google.common.util.concurrent.ListenableFuture;
import com.jonfp.birdhouse.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private TextView textViewColor;
    private ImageView capturedImageView;
    Button captureButton;
    Button captureDoneButton;
    private final Handler handler = new Handler();
    private boolean redirecting = false;
    private ImageView background;
    private ImageView backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        textViewColor = findViewById(R.id.textView_color);
        capturedImageView = findViewById(R.id.captured_image);
        backgroundColor = findViewById(R.id.backgroundColor);
        captureButton = findViewById(R.id.captureBtn);
        captureDoneButton = findViewById(R.id.captureDoneBtn);
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }
        background = findViewById(R.id.backgroundImage);
        background.setImageResource(R.drawable.house_painted);
        background.setVisibility(View.INVISIBLE);
        backgroundColor.setVisibility(View.INVISIBLE);
        captureButton.setOnClickListener(v -> takePhoto());
        captureDoneButton.setVisibility(View.INVISIBLE);
    }
    private void resetCamera() {
        // Clear the previous image and text
        capturedImageView.setImageDrawable(null);
        textViewColor.setText("");

        // Rebind camera use cases
        startCamera();
    }
    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll(); // Important to unbind all use cases before rebinding

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setTargetResolution(new Size(1920, 1080))
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        File photoFile = new File(getExternalFilesDir(null), "photo.jpg");

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = outputFileResults.getSavedUri();
                runOnUiThread(() -> {

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), savedUri);
                        extractColor(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(CameraActivity.this, "Failed to load the photo from storage.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraActivity.this, "Photo Capture Failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void extractColor(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
            Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();

            // Choose the most vibrant swatch available, prioritizing them
            Palette.Swatch chosenSwatch = vibrantSwatch != null ? vibrantSwatch :
                    darkVibrantSwatch != null ? darkVibrantSwatch :
                            lightVibrantSwatch;

            if (chosenSwatch != null) {
                int dominantColor = chosenSwatch.getRgb();
                textViewColor.setBackgroundColor(dominantColor);
                Log.d("ColorInfo", "Chosen Color RGB: " + Integer.toHexString(dominantColor));
                textViewColor.setText("Vald färg");
                captureDoneButton.setVisibility(View.VISIBLE);
                captureDoneButton.setOnClickListener(v -> redirect(dominantColor));

            } else {
                textViewColor.setText("Hittade ingen färg");
            }
        });
    }


    public void redirect(int color){

        if (redirecting) {
            return;
        }
        redirecting = true;

        background.setVisibility(View.VISIBLE);
        backgroundColor.setVisibility(View.VISIBLE);
        backgroundColor.setBackgroundColor(color); // Set any color you desire

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play sound effect for transitioning to the next activity

                Intent intent = new Intent(CameraActivity.this, ListeningActivity.class);
                intent.putExtra("color", String.valueOf(color)); // variableValue is the value you want to send
                startActivity(intent);
                finish(); // Finish current activity to prevent going back to it on back press
            }
        }, 1500);
    }

    private String getColorName(int color) {
        float[] hsb = new float[3];
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        // Convert RGB to HSB
        Color.RGBToHSV(red, green, blue, hsb);
        float hue = hsb[0];
        float saturation = hsb[1];
        float brightness = hsb[2];

        // Define color names based on hue, saturation, and brightness values
        if (saturation < 0.1 && brightness < 0.1) return "Black";
        if (saturation < 0.1 && brightness > 0.9) return "White";
        if (saturation < 0.1) return "Gray";

        if (hue < 30) return "Red";
        if (hue < 60) return "Orange";
        if (hue < 90) return "Yellow";
        if (hue < 150) return "Green";
        if (hue < 210) return "Cyan";
        if (hue < 270) return "Blue";
        if (hue < 330) return "Magenta";
        return "Red"; // Red wraps around.
    }


    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
        }
    }
}
