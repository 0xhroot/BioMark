package com.example.biomark.models;


public class Attendance {
    private String name;
    private String enrollment_no;
    private String section;
    private String inTime;
    private String outTime;
    private String status;

    // Empty constructor required for Firestore deserialization
    public Attendance() {}

    public Attendance(String name, String enrollment_no, String section, String inTime, String outTime, String status) {
        this.name = name;
        this.enrollment_no = enrollment_no;
        this.section = section;
        this.inTime = inTime;
        this.outTime = outTime;
        this.status = status;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEnrollment_no() {
        return enrollment_no;
    }
    public void setEnrollment_no(String enrollment_no) {
        this.enrollment_no = enrollment_no;
    }

    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }

    public String getInTime() {
        return inTime;
    }
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }
    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
