package com.medikiosk;

import com.medikiosk.dao.DatabaseConnection;
import com.medikiosk.services.KioskService;
import com.medikiosk.ui.PhysicianController;
import com.medikiosk.datastructures.QueueEntry;
import javafx.application.Platform;
import javafx.scene.control.TableView;

import java.util.concurrent.CountDownLatch;

public class HeadlessUITest {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Headless UI Test...");
        DatabaseConnection.initializeDatabase();
        KioskService service = new KioskService();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                System.out.println("JavaFX Platform started.");
                PhysicianController physicianController = new PhysicianController(service);
                
                System.out.println("Current Physician Queue UI Table Size: " + physicianController.getView().getChildren().get(1).getClass().getName());
                @SuppressWarnings("unchecked")
                TableView<QueueEntry> table = (TableView<QueueEntry>) physicianController.getView().getChildren().get(1);
                
                System.out.println("Table items count before: " + table.getItems().size());
                
                // Manually add directly to service
                service.registerPatient(
                    new com.medikiosk.models.Patient("Test", 20, "123", "Unknown", "Unknown", "None", "English"), 
                    new com.medikiosk.models.Assessment(0, java.util.Arrays.asList("Cough"))
                );
                
                physicianController.refreshQueue();
                System.out.println("Table items count after refresh: " + table.getItems().size());
                if (table.getItems().size() > 0) {
                    System.out.println("Item 0 Patient Name: " + table.getItems().get(0).getPatient().getName());
                }
                
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        
        latch.await();
        System.out.println("Test Complete.");
        System.exit(0);
    }
}
