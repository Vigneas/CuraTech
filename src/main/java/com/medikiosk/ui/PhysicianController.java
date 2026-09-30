package com.medikiosk.ui;

import com.medikiosk.datastructures.QueueEntry;
import com.medikiosk.services.KioskService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import java.sql.SQLException;
import java.util.List;

public class PhysicianController {
    private KioskService service;
    private VBox view;
    private TableView<QueueEntry> queueTable;
    
    // Consultation Panel Fields
    private QueueEntry activePatient;
    private Label activeNameLbl;
    private Label activeDetailsLbl;
    private TextArea diagnosisArea;
    private TextArea prescriptionArea;
    private VBox consultPanel;

    public PhysicianController(KioskService service) {
        this.service = service;
        buildView();
    }

    private void buildView() {
        view = new VBox(20);
        view.setPadding(new Insets(40));

        Label title = new Label("Physician Dashboard");
        title.getStyleClass().add("title-label");

        // QUEUE SECTION
        VBox queueBox = new VBox(15);
        HBox.setHgrow(queueBox, Priority.ALWAYS);
        
        Label queueTitle = new Label("Waiting Patients");
        queueTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        queueTable = new TableView<>();
        queueTable.getStyleClass().add("table-view");
        
        TableColumn<QueueEntry, String> tierCol = new TableColumn<>("Priority Tier");
        tierCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
            data.getValue() != null ? data.getValue().getAssessment().getPriorityTier() : ""
        ));
        tierCol.setCellFactory(column -> new TableCell<QueueEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label label = new Label(item);
                    if ("Red".equalsIgnoreCase(item)) {
                        label.getStyleClass().add("badge-red");
                    } else if ("Yellow".equalsIgnoreCase(item)) {
                        label.getStyleClass().add("badge-yellow");
                    } else {
                        label.getStyleClass().add("badge-green");
                    }
                    setGraphic(label);
                }
            }
        });
        
        TableColumn<QueueEntry, String> scoreCol = createTextColumn("Severity", entry -> String.valueOf(entry.getAssessment().getSeverityScore()));
        TableColumn<QueueEntry, String> nameCol = createTextColumn("Patient Name", entry -> entry.getPatient().getName());
        TableColumn<QueueEntry, String> ageCol = createTextColumn("Age", entry -> String.valueOf(entry.getPatient().getAge()));
        TableColumn<QueueEntry, String> symptomsCol = createTextColumn("Symptoms", entry -> String.join(", ", entry.getAssessment().getSymptoms()));

        queueTable.getColumns().add(tierCol);
        queueTable.getColumns().add(scoreCol);
        queueTable.getColumns().add(nameCol);
        queueTable.getColumns().add(ageCol);
        queueTable.getColumns().add(symptomsCol);
        queueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button callNextBtn = new Button("Call Next Patient");
        callNextBtn.getStyleClass().add("primary-button");
        callNextBtn.setOnAction(e -> callNextPatient());

        queueBox.getChildren().addAll(queueTitle, queueTable, callNextBtn);

        // CONSULTATION SECTION
        consultPanel = new VBox(15);
        consultPanel.setPadding(new Insets(20));
        consultPanel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12px; -fx-border-color: #e5e7eb; -fx-border-radius: 12px;");
        consultPanel.setMinWidth(400);
        consultPanel.setDisable(true); // Disabled until a patient is called
        
        Label consultTitle = new Label("Active Consultation");
        consultTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        activeNameLbl = new Label("No active patient");
        activeNameLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        
        activeDetailsLbl = new Label("");
        activeDetailsLbl.setWrapText(true);
        activeDetailsLbl.setStyle("-fx-text-fill: #6b7280;");
        
        Label diagnosisLbl = new Label("Diagnosis");
        diagnosisLbl.setStyle("-fx-font-weight: bold;");
        diagnosisArea = new TextArea();
        diagnosisArea.setPromptText("Enter medical diagnosis...");
        diagnosisArea.setPrefRowCount(4);
        
        Label prescriptionLbl = new Label("Prescription Notes");
        prescriptionLbl.setStyle("-fx-font-weight: bold;");
        prescriptionArea = new TextArea();
        prescriptionArea.setPromptText("Enter prescription and care instructions...");
        prescriptionArea.setPrefRowCount(4);
        
        Button markCompleteBtn = new Button("Mark Complete & Save");
        markCompleteBtn.getStyleClass().add("secondary-button");
        markCompleteBtn.setMaxWidth(Double.MAX_VALUE);
        markCompleteBtn.setOnAction(e -> markComplete());
        
        consultPanel.getChildren().addAll(consultTitle, activeNameLbl, activeDetailsLbl, diagnosisLbl, diagnosisArea, prescriptionLbl, prescriptionArea, markCompleteBtn);

        HBox splitLayout = new HBox(30);
        splitLayout.getChildren().addAll(queueBox, consultPanel);

        view.getChildren().addAll(title, splitLayout);
        refreshQueue();
    }

    public void refreshQueue() {
        List<QueueEntry> liveQueue = service.getLivePhysicianQueue();
        System.out.println("🔄 REFRESHING PHYSICIAN UI: Found " + liveQueue.size() + " patients in the live queue.");
        queueTable.setItems(FXCollections.observableArrayList(liveQueue));
    }
    
    private TableColumn<QueueEntry, String> createTextColumn(String title, java.util.function.Function<QueueEntry, String> extractor) {
        TableColumn<QueueEntry, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new ReadOnlyStringWrapper(
            data.getValue() != null ? extractor.apply(data.getValue()) : ""
        ));
        // Explicitly force text rendering to bypass any JavaFX default cell bugs
        col.setCellFactory(column -> new TableCell<QueueEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });
        return col;
    }

    private void callNextPatient() {
        if (activePatient != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please complete the current consultation first.");
            alert.show();
            return;
        }

        QueueEntry next = service.getNextPatientAndRemoveFromQueue();
        if (next != null) {
            activePatient = next;
            
            // Populate UI
            activeNameLbl.setText(activePatient.getPatient().getName() + " (Age: " + activePatient.getPatient().getAge() + ")");
            String details = String.format("Gender: %s | Blood: %s\nAllergies: %s\nContact: %s | Language: %s\n\nSymptoms: %s\nTier: %s (Score: %d)", 
                activePatient.getPatient().getGender(),
                activePatient.getPatient().getBloodType(),
                activePatient.getPatient().getAllergies() == null || activePatient.getPatient().getAllergies().isEmpty() ? "None reported" : activePatient.getPatient().getAllergies(),
                activePatient.getPatient().getContactInfo(),
                activePatient.getPatient().getPreferredLanguage() != null ? activePatient.getPatient().getPreferredLanguage() : "English",
                String.join(", ", activePatient.getAssessment().getSymptoms()),
                activePatient.getAssessment().getPriorityTier(),
                activePatient.getAssessment().getSeverityScore()
            );
            activeDetailsLbl.setText(details);
            
            diagnosisArea.clear();
            prescriptionArea.clear();
            consultPanel.setDisable(false);
            
            refreshQueue();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No patients in queue.");
            alert.show();
        }
    }

    private void markComplete() {
        if (activePatient == null) return;
        
        if (diagnosisArea.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a diagnosis.");
            alert.show();
            return;
        }

        try {
            activePatient.getAssessment().setDiagnosis(diagnosisArea.getText().trim());
            activePatient.getAssessment().setPrescriptionNotes(prescriptionArea.getText().trim());
            
            service.completeConsultation(activePatient);
            
            // Reset UI
            activePatient = null;
            activeNameLbl.setText("No active patient");
            activeDetailsLbl.setText("");
            diagnosisArea.clear();
            prescriptionArea.clear();
            consultPanel.setDisable(true);
            
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Consultation saved successfully!");
            success.show();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save consultation.");
            alert.show();
        }
    }

    public VBox getView() {
        return view;
    }
}
