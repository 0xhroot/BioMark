package com.example.biomark;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.biomark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivProfile;
    private EditText etName, etEnrollment, etEmail, etSection;
    private Button btnSave;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        // Bind UI elements
        ivProfile = findViewById(R.id.edit_profile_image);
        etName = findViewById(R.id.edit_name);
        etEnrollment = findViewById(R.id.edit_enrollment);
        etEmail = findViewById(R.id.edit_email);
        etSection = findViewById(R.id.edit_section);
        btnSave = findViewById(R.id.edit_save_button);

        // Apply slide-in animation to all views
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        ivProfile.startAnimation(slideIn);
        etName.startAnimation(slideIn);
        etEnrollment.startAnimation(slideIn);
        etEmail.startAnimation(slideIn);
        etSection.startAnimation(slideIn);
        btnSave.startAnimation(slideIn);

        // Set click listener to allow profile image update
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Save profile on button click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });

        // (Optional) Load existing user data from Firestore if available.
        // loadUserProfile();
    }

    // Open the device's image picker to select a new profile image.
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
        }
    }

    // Save profile data and upload new image (if selected) to Firebase Storage
    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String enrollment = etEnrollment.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String section = etSection.getText().toString().trim();

        if(name.isEmpty() || enrollment.isEmpty() || email.isEmpty() || section.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // If a new image was selected, upload it first.
        if(imageUri != null) {
            StorageReference fileRef = storageRef.child(userId + ".jpg");
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateUserProfile(userId, name, enrollment, email, section, imageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            updateUserProfile(userId, name, enrollment, email, section, null);
        }
    }

    // Update Firestore with new profile data
    private void updateUserProfile(String userId, String name, String enrollment, String email, String section, @Nullable String imageUrl) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("enrollment_no", enrollment);
        profileData.put("email", email);
        profileData.put("section", section);
        if(imageUrl != null) {
            profileData.put("profile_image", imageUrl);
        }

        db.collection("Users").document(userId)
                .update(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Profile update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
