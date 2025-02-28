package com.example.biomark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etAdminId, etAdminPassword;
    private TextView tvLogs;  // TextView to show errors and logs on screen
    private DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminId = findViewById(R.id.admin_id);
        etAdminPassword = findViewById(R.id.admin_password);
        Button btnAdminLogin = findViewById(R.id.admin_login_button);
        tvLogs = findViewById(R.id.tvLogs);  // Make sure this ID exists in your layout

        // Initialize the database reference pointing to "Admins/AdminCredentials"
        adminRef = FirebaseDatabase.getInstance().getReference("Admins").child("AdminCredentials");

        showLog("AdminLoginActivity onCreate() started.");
        showLog("Database reference: " + adminRef.toString());

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminIdInput = etAdminId.getText().toString().trim();
                String adminPasswordInput = etAdminPassword.getText().toString().trim();

                showLog("Login button clicked.");
                showLog("Admin ID Input: " + adminIdInput);
                showLog("Admin Password Input: " + adminPasswordInput);

                if (adminIdInput.isEmpty() || adminPasswordInput.isEmpty()){
                    showLog("Input validation failed: One or both fields are empty.");
                    Toast.makeText(AdminLoginActivity.this, "Please enter Admin ID and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                showLog("Fetching stored admin credentials from database...");
                // Retrieve the stored admin credentials from Realtime Database
                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        showLog("onDataChange triggered. Snapshot: " + snapshot.toString()); // Log full data

                        if (snapshot.exists()) {
                            showLog("Snapshot exists. Extracting credentials...");

                            // Log available keys
                            for (DataSnapshot child : snapshot.getChildren()) {
                                showLog("Key: " + child.getKey() + ", Value: " + child.getValue());
                            }

                            String storedAdminId = snapshot.child("adminId").getValue(String.class);
                            String storedPassword = snapshot.child("password").getValue(String.class);

                            showLog("Stored Admin ID: " + storedAdminId);
                            showLog("Stored Password: " + storedPassword);

                            if (adminIdInput.equals(storedAdminId) && adminPasswordInput.equals(storedPassword)) {
                                showLog("Login successful! Redirecting...");
                                Toast.makeText(AdminLoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                                finish();
                            } else {
                                showLog("Login failed: Incorrect credentials.");
                                Toast.makeText(AdminLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showLog("Admin credentials not found in database.");
                            Toast.makeText(AdminLoginActivity.this, "Admin credentials not set up", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        String errorMsg = "onCancelled: Database error: " + error.getMessage();
                        showLog(errorMsg);
                        Log.e("AdminLogin", errorMsg);
                        Toast.makeText(AdminLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Appends the provided message to the tvLogs TextView and also logs it using Log.d.
     */
    private void showLog(String message) {
        Log.d("AdminLogin", message);
        if (tvLogs != null) {
            String currentText = tvLogs.getText().toString();
            tvLogs.setText(currentText + "\n" + message);
        }
    }
}
