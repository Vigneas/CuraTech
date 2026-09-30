package com.medikiosk.dao;

import com.medikiosk.models.Assessment;
import com.medikiosk.models.Patient;

import java.sql.*;

public class PatientDAO {

    public int insertPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patients (name, age, contact_info, gender, blood_type, allergies, preferred_language) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getContactInfo());
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getBloodType());
            stmt.setString(6, patient.getAllergies());
            stmt.setString(7, patient.getPreferredLanguage());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    patient.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public int insertAssessment(Assessment assessment) throws SQLException {
        String query = "INSERT INTO assessments (patient_id, symptoms, severity_score, priority_tier, status, arrival_time, diagnosis, prescription_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, assessment.getPatientId());
            stmt.setString(2, String.join(",", assessment.getSymptoms()));
            stmt.setInt(3, assessment.getSeverityScore());
            stmt.setString(4, assessment.getPriorityTier());
            stmt.setString(5, assessment.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(assessment.getArrivalTime()));
            stmt.setString(7, assessment.getDiagnosis());
            stmt.setString(8, assessment.getPrescriptionNotes());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    assessment.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public void updateAssessment(Assessment assessment) throws SQLException {
        String query = "UPDATE assessments SET status = ?, diagnosis = ?, prescription_notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, assessment.getStatus());
            stmt.setString(2, assessment.getDiagnosis());
            stmt.setString(3, assessment.getPrescriptionNotes());
            stmt.setInt(4, assessment.getId());
            stmt.executeUpdate();
        }
    }

    // A simple DTO class for returning metrics
    public static class Metrics {
        public int redCount;
        public int yellowCount;
        public int greenCount;
        public int completedCount;
    }

    public Metrics getDailyMetrics() throws SQLException {
        Metrics metrics = new Metrics();
        String query = "SELECT status, priority_tier, COUNT(*) as cnt FROM assessments GROUP BY status, priority_tier";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String status = rs.getString("status");
                String tier = rs.getString("priority_tier");
                int count = rs.getInt("cnt");
                
                if ("COMPLETED".equalsIgnoreCase(status)) {
                    metrics.completedCount += count;
                } else if ("WAITING".equalsIgnoreCase(status)) {
                    if ("RED".equalsIgnoreCase(tier)) metrics.redCount += count;
                    else if ("YELLOW".equalsIgnoreCase(tier)) metrics.yellowCount += count;
                    else if ("GREEN".equalsIgnoreCase(tier)) metrics.greenCount += count;
                }
            }
        }
        return metrics;
    }

    public java.util.List<com.medikiosk.datastructures.QueueEntry> getCompletedConsultations() throws SQLException {
        java.util.List<com.medikiosk.datastructures.QueueEntry> completedList = new java.util.ArrayList<>();
        String query = "SELECT a.*, p.name, p.age, p.contact_info, p.gender, p.blood_type, p.allergies, p.preferred_language FROM assessments a JOIN patients p ON a.patient_id = p.id WHERE a.status = 'COMPLETED' ORDER BY a.arrival_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("contact_info"),
                    rs.getString("gender"),
                    rs.getString("blood_type"),
                    rs.getString("allergies"),
                    rs.getString("preferred_language")
                );

                Assessment assessment = new Assessment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    java.util.Arrays.asList(rs.getString("symptoms").split(",")),
                    rs.getInt("severity_score"),
                    rs.getString("priority_tier"),
                    rs.getString("status"),
                    rs.getTimestamp("arrival_time").toLocalDateTime(),
                    rs.getString("diagnosis"),
                    rs.getString("prescription_notes")
                );
                
                completedList.add(new com.medikiosk.datastructures.QueueEntry(patient, assessment));
            }
        }
        return completedList;
    }
}
