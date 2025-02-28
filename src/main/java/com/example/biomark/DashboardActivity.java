package com.example.biomark;


import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private ImageView profileImage;
    private Button btnMarkAttendance, btnEditProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Bind UI elements from the layout
        profileImage = findViewById(R.id.dashboard_profile_image);
        btnMarkAttendance = findViewById(R.id.dashboard_mark_attendance);
        btnEditProfile = findViewById(R.id.dashboard_edit_profile);
        btnLogout = findViewById(R.id.dashboard_logout);

        // Apply fade-in animation to all elements
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        profileImage.startAnimation(fadeIn);
        btnMarkAttendance.startAnimation(fadeIn);
        btnEditProfile.startAnimation(fadeIn);
        btnLogout.startAnimation(fadeIn);

        // Set click listeners for each button
        btnMarkAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MarkAttendanceActivity.class);
            startActivity(intent);
        });
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
