package com.example.biomark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.biomark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLoginRedirect;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        etEmail = findViewById(R.id.signup_email);
        etPassword = findViewById(R.id.signup_password);
        etConfirmPassword = findViewById(R.id.signup_confirm_password);
        btnSignup = findViewById(R.id.signup_button);
        tvLoginRedirect = findViewById(R.id.login_redirect);
        progressBar = findViewById(R.id.signup_progress);

        // Apply slide-in animation to form elements
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        etEmail.startAnimation(slideIn);
        etPassword.startAnimation(slideIn);
        etConfirmPassword.startAnimation(slideIn);
        btnSignup.startAnimation(slideIn);
        tvLoginRedirect.startAnimation(slideIn);

        // Handle Signup Button click
        btnSignup.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Optionally, store additional user data in Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            // Additional fields (e.g., name, enrollment_no) can be added later in the profile
                            if (user != null) {
                                db.collection("Users").document(user.getUid()).set(userData);
                            }
                            // Navigate to Dashboard
                            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Redirect to Login screen if the user already has an account
        tvLoginRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
