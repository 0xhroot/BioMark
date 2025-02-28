package com.example.biomark;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biomark.adapters.AttendanceAdapter;
import com.example.biomark.models.Attendance;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<Attendance> attendanceList;
    private Button btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerView = findViewById(R.id.admin_attendance_recycler);
        btnExport = findViewById(R.id.export_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Attendance")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Attendance attendance = document.toObject(Attendance.class);
                        attendanceList.add(attendance);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("AdminDashboard", "Error fetching attendance", e));

        btnExport.setOnClickListener(view -> {
            // TODO: Implement export functionality here.
        });
    }
}
