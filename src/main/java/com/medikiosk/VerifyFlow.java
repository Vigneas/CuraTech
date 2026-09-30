package com.medikiosk;

import com.medikiosk.models.Patient;
import com.medikiosk.models.Assessment;
import com.medikiosk.services.KioskService;
import com.medikiosk.datastructures.QueueEntry;
import com.medikiosk.dao.DatabaseConnection;
import java.util.Arrays;
import java.util.List;

public class VerifyFlow {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing Database...");
        DatabaseConnection.initializeDatabase();
        KioskService service = new KioskService();

        System.out.println("Registering Patient 1 with 'Cough' (Should be Green)...");
        Patient p1 = new Patient("Alice (RED)", 25, "555-1234", "Female", "A+", "None", "English");
        Assessment a1 = new Assessment(0, Arrays.asList("Cough"));
        service.registerPatient(p1, a1);
        
        // Sleep slightly to ensure arrival times are distinct
        Thread.sleep(100); 

        System.out.println("Registering Patient 2 with 'Chest Pain' (Should be Red/Emergency)...");
        Patient p2 = new Patient("Bob (GREEN)", 40, "555-9999", "Male", "O-", "Peanuts", "English");
        Assessment a2 = new Assessment(0, Arrays.asList("Chest Pain"));
        service.registerPatient(p2, a2);
        
        System.out.println("\n=== LIVE PHYSICIAN QUEUE (AI PRIORITY OVERRIDE) ===");
        List<QueueEntry> queue = service.getLivePhysicianQueue();
        for (int i = 0; i < queue.size(); i++) {
            QueueEntry qe = queue.get(i);
            System.out.println((i+1) + ". [Tier: " + qe.getAssessment().getPriorityTier() + "] " 
                + qe.getPatient().getName() + " - Symptoms: " + String.join(", ", qe.getAssessment().getSymptoms()));
        }
        
        System.out.println("\n=== ADMIN DASHBOARD METRICS ===");
        var metrics = service.getAdminMetrics();
        System.out.println("Emergency (Red) Waiting: " + metrics.redCount);
        System.out.println("Urgent (Yellow) Waiting: " + metrics.yellowCount);
        System.out.println("Normal (Green) Waiting: " + metrics.greenCount);
        
        System.exit(0);
    }
}
