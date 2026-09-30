package com.medikiosk.services;

import com.medikiosk.ai.TriageEngine;
import com.medikiosk.dao.PatientDAO;
import com.medikiosk.datastructures.PatientQueue;
import com.medikiosk.datastructures.QueueEntry;
import com.medikiosk.models.Assessment;
import com.medikiosk.models.Patient;

import java.sql.SQLException;
import java.util.List;

public class KioskService {
    private PatientDAO patientDAO;
    private PatientQueue patientQueue;
    private TriageEngine triageEngine;

    public KioskService() {
        this.patientDAO = new PatientDAO();
        this.patientQueue = new PatientQueue();
        this.triageEngine = new TriageEngine();
    }

    public void registerPatient(Patient patient, Assessment assessment) throws SQLException {
        // 1. Calculate severity and priority tier
        int score = triageEngine.calculateSeverityScore(assessment.getSymptoms());
        String tier = triageEngine.determinePriorityTier(score);
        
        assessment.setSeverityScore(score);
        assessment.setPriorityTier(tier);
        assessment.setStatus("WAITING");

        // 2. Persist to DB
        int patientId = patientDAO.insertPatient(patient);
        if (patientId != -1) {
            assessment.setPatientId(patientId);
            patientDAO.insertAssessment(assessment);
            
            // 3. Add to live priority queue
            patientQueue.enqueue(new QueueEntry(patient, assessment));
            System.out.println("✅ SUCCESS: Patient '" + patient.getName() + "' (Age " + patient.getAge() + ") was saved to Database and added to Queue as tier: " + tier);
        } else {
            throw new SQLException("Failed to register patient in DB.");
        }
    }

    public List<QueueEntry> getLivePhysicianQueue() {
        return patientQueue.getSnapshot();
    }

    public void completeConsultation(QueueEntry entry) throws SQLException {
        // Remove from DB (update status, diagnosis, prescription)
        entry.getAssessment().setStatus("COMPLETED");
        patientDAO.updateAssessment(entry.getAssessment());
        
        // Assuming the UI calls dequeue() when taking the patient, we just update the DB here.
    }

    // Helper to actually dequeue the next patient for the physician
    public QueueEntry getNextPatientAndRemoveFromQueue() {
        return patientQueue.dequeue();
    }

    public PatientDAO.Metrics getAdminMetrics() throws SQLException {
        return patientDAO.getDailyMetrics();
    }
    
    public List<QueueEntry> getCompletedConsultations() throws SQLException {
        return patientDAO.getCompletedConsultations();
    }
}
