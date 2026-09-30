package com.medikiosk.models;

import java.time.LocalDateTime;
import java.util.List;

public class Assessment {
    private int id;
    private int patientId;
    private List<String> symptoms;
    private int severityScore;
    private String priorityTier;
    private String status; // "WAITING", "COMPLETED"
    private LocalDateTime arrivalTime;
    private String diagnosis;
    private String prescriptionNotes;

    public Assessment() {
        this.arrivalTime = LocalDateTime.now();
    }

    public Assessment(int patientId, List<String> symptoms) {
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.status = "WAITING";
        this.arrivalTime = LocalDateTime.now();
    }

    // Full constructor for loading from DB
    public Assessment(int id, int patientId, List<String> symptoms, int severityScore, String priorityTier, String status, LocalDateTime arrivalTime, String diagnosis, String prescriptionNotes) {
        this.id = id;
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.severityScore = severityScore;
        this.priorityTier = priorityTier;
        this.status = status;
        this.arrivalTime = arrivalTime;
        this.diagnosis = diagnosis;
        this.prescriptionNotes = prescriptionNotes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public List<String> getSymptoms() { return symptoms; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }

    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }

    public String getPriorityTier() { return priorityTier; }
    public void setPriorityTier(String priorityTier) { this.priorityTier = priorityTier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }
}
