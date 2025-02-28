package com.example.biomark.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biomark.R;
import com.example.biomark.models.Attendance;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Attendance> attendanceList;

    public AttendanceAdapter(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.tvName.setText(attendance.getName());
        holder.tvEnrollment.setText(attendance.getEnrollment_no());
        holder.tvSection.setText(attendance.getSection());
        holder.tvInTime.setText("In: " + attendance.getInTime());
        holder.tvOutTime.setText("Out: " + attendance.getOutTime());
        holder.tvStatus.setText(attendance.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEnrollment, tvSection, tvInTime, tvOutTime, tvStatus;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_name);
            tvEnrollment = itemView.findViewById(R.id.item_enrollment);
            tvSection = itemView.findViewById(R.id.item_section);
            tvInTime = itemView.findViewById(R.id.item_in_time);
            tvOutTime = itemView.findViewById(R.id.item_out_time);
            tvStatus = itemView.findViewById(R.id.item_status);
        }
    }
}
