package com.example.biomark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;

public class MarkAttendanceActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 102;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    // Replace with your actual college coordinates
    private static final double COLLEGE_LATITUDE = 12.9716;
    private static final double COLLEGE_LONGITUDE = 77.5946;
    // Acceptable radius in meters (e.g., 100 meters)
    private static final float RADIUS_METERS = 100f;

    private FusedLocationProviderClient fusedLocationClient;
    private Button btnCaptureFace;
    private ImageView ivFacePreview; // Displays captured face in circular form

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        btnCaptureFace = findViewById(R.id.capture_face_button);
        ivFacePreview = findViewById(R.id.captured_face_image);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            verifyLocation();
        }

        btnCaptureFace.setOnClickListener(view -> {
            verifyLocation();
            // Check camera permission before launching camera intent
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure there is a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFacePreview.setImageBitmap(imageBitmap);
            // Instead of just face detection, run recognition using MobileFaceNet
            processFaceRecognition(imageBitmap);
        }
    }

    private void processFaceRecognition(Bitmap bitmap) {
        // First, run ML Kit Face Detection to ensure a face is present.
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();
        FaceDetector detector = FaceDetection.getClient(options);
        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        // If a face is detected, load MobileFaceNet model to get embeddings.
                        try {
                            FaceRecognitionHelper recognitionHelper = new FaceRecognitionHelper(this);
                            float[] embedding = recognitionHelper.getFaceEmbedding(bitmap);
                            // For demo, we use a dummy comparison that always "matches".
                            boolean match = compareEmbeddings(embedding, getStoredEmbedding());
                            if (match) {
                                Toast.makeText(MarkAttendanceActivity.this, "Face recognized successfully! Attendance marked.", Toast.LENGTH_LONG).show();
                                // Here, you can save the attendance record.
                            } else {
                                Toast.makeText(MarkAttendanceActivity.this, "Face not recognized. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(MarkAttendanceActivity.this, "Error loading face model: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MarkAttendanceActivity.this, "No face detected. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MarkAttendanceActivity.this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Dummy method to simulate retrieving a stored face embedding.
     * In a real system, you would retrieve this from your database.
     */
    private float[] getStoredEmbedding() {
        // For demonstration, we return a dummy embedding array of 128 zeros.
        return new float[128];
    }

    /**
     * Dummy comparison method between the new embedding and a stored embedding.
     * In a real system, you would compute cosine similarity or Euclidean distance.
     * Here, we simply return true to simulate a successful match.
     */
    private boolean compareEmbeddings(float[] embedding1, float[] embedding2) {
        // For demo, always return true.
        return true;
    }

    private void verifyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permissions not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        if (isWithinCollegeArea(location)) {
                            Toast.makeText(MarkAttendanceActivity.this, "You are within the college area", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MarkAttendanceActivity.this, "You are not within the college area", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MarkAttendanceActivity.this, "Unable to get your location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MarkAttendanceActivity.this, "Error retrieving location: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean isWithinCollegeArea(Location location) {
        float[] results = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), COLLEGE_LATITUDE, COLLEGE_LONGITUDE, results);
        return results[0] < RADIUS_METERS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                verifyLocation();
            } else {
                Toast.makeText(this, "Location permission is required to mark attendance.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to capture your face.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
