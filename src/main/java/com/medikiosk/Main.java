package com.medikiosk;

import com.medikiosk.dao.DatabaseConnection;
import com.medikiosk.services.KioskService;
import com.medikiosk.ui.AdminController;
import com.medikiosk.ui.PatientController;
import com.medikiosk.ui.PhysicianController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    private KioskService kioskService;

    @Override
    public void init() throws Exception {
        // Initialize DB schema on startup
        DatabaseConnection.initializeDatabase();
        kioskService = new KioskService();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MediKiosk - Java Port");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Patient Tab
        Tab patientTab = new Tab("Patient Intake");
        PatientController patientController = new PatientController(kioskService);
        patientTab.setContent(patientController.getView());

        // Physician Tab
        Tab physicianTab = new Tab("Physician Queue");
        PhysicianController physicianController = new PhysicianController(kioskService);
        physicianTab.setContent(physicianController.getView());

        // Admin Tab
        Tab adminTab = new Tab("Admin Dashboard");
        AdminController adminController = new AdminController(kioskService);
        adminTab.setContent(adminController.getView());

        tabPane.getTabs().addAll(patientTab, physicianTab, adminTab);

        // Add a listener to refresh Admin/Physician tabs when selected
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == physicianTab) {
                physicianController.refreshQueue();
            } else if (newTab == adminTab) {
                adminController.refreshDashboard();
            }
        });

        javafx.scene.control.ToggleButton darkModeToggle = new javafx.scene.control.ToggleButton("🌙 Dark Mode");
        darkModeToggle.getStyleClass().add("secondary-button");
        javafx.scene.layout.HBox topBar = new javafx.scene.layout.HBox(darkModeToggle);
        topBar.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        topBar.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));
        
        javafx.scene.layout.BorderPane rootPane = new javafx.scene.layout.BorderPane();
        rootPane.setTop(topBar);
        rootPane.setCenter(tabPane);

        Scene scene = new Scene(rootPane, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        darkModeToggle.setOnAction(e -> {
            if (darkModeToggle.isSelected()) {
                darkModeToggle.setText("☀️ Light Mode");
                scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
            } else {
                darkModeToggle.setText("🌙 Dark Mode");
                scene.getStylesheets().remove(getClass().getResource("/dark-theme.css").toExternalForm());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        DatabaseConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
